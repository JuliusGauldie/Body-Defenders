/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 11/08/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.List;

public class GameplayPanel extends JPanel implements MouseListener, MouseMotionListener
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 520;

    // Images
    private static BufferedImage mapImage;
    private static BufferedImage buildTowerMenuImage;
    private static BufferedImage upgradeTowerMenuImage;

    // Images
    private String mapImageName = "resources/assets/map";

    // Try to assign images
    static {
        try 
        {
            buildTowerMenuImage = ImageIO.read(new File("resources/assets/buildTowerMenu.png"));
            upgradeTowerMenuImage = ImageIO.read(new File("resources/assets/UpgradeTowerMenu.png"));
        } 
        catch (IOException e) {e.printStackTrace();}
    }

    // Arrays
    public ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Tower> towers = new ArrayList<>();
    ArrayList<Projectile> projectiles = new ArrayList<>();
    ArrayList<MenuButton> activeMenuButtons = new ArrayList<>();
    ArrayList<BloodSplatter> bloodSplatter = new ArrayList<>();

    // Point arrays
    ArrayList<Point> path = new ArrayList<>();
    ArrayList<Point> places = new ArrayList<>();

    // Buttons
    MenuButton hoveredButton = null;

    // Towers
    private Tower selectedTower = null;
    private WaveInfo currentWave = null;

    private GameLayerPanel panel;

    // JLabel
    JLabel infoLabel;

    // Menus
    int x, y, radius;
    Runnable onClick;

    // Timer
    java.util.Timer timer;

    // Dificulty
    private float difficultyLevel = 1; // 0.5 - Easy, 1 - Normal, 2 - Hard

    StaticVariables sVariables = new StaticVariables();
    GameManager gameManager = new GameManager();
    private java.util.Timer enemySpawnTimer = null;

    private boolean spawningWave = false;

    // Score
    private int totalKills = 0;
    private int currentWaveNumber = 0;

    /**
     * Constructor for objects of class MainGamePanel
     */
    public GameplayPanel(GameLayerPanel panel) 
    { 
        this.panel = panel;

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        addMouseListener(this);
        addMouseMotionListener(this);

        super.repaint();
    }

    public void newGame(int levelIndex)
    {
        totalKills = 0;

        try {
            mapImage = ImageIO.read(new File(mapImageName + levelIndex + ".png"));
        } 
        catch (IOException e) { e.printStackTrace(); }

        selectedTower = null;

        for (Tower t : towers)
        {
            if (t.isBuilt())
            {
                t.resetTower();
            }
        }

        enemies.clear();

        bloodSplatter.clear();

        
        // Add waypoints for enemies to follow
        if (path.isEmpty())
        {
            for (int i = 0; i < sVariables.getAmountOfMapPoints(); i++)
            {
                int coord1 = sVariables.getCoord(i, 0);
                int coord2 = sVariables.getCoord(i, 1);

                path.add(new Point(coord1, coord2));
            }
        }

        // add building spots for towers
        if (towers.isEmpty())
        {
            for (int i = 0; i < sVariables.getAmountOfTowerSpots(); i++)
            {
                int coord1 = sVariables.getTCoord(i, 0);
                int coord2 = sVariables.getTCoord(i, 1);

                createTower(coord1, coord2);
            }
        }

        // Works as update function (runs 60 times a second - 60fps)
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, 1000 / 60); // Update at 60fps
    }

    class UpdateTask extends TimerTask // Runs 60 times a second
    {
        public void run() 
        {
            // Update all built towers
            for (Tower t : towers) {
                if (t.isBuilt()) t.update();
            }

            // Update all projectiles and handle projectile effects
            Iterator<Projectile> iter = projectiles.iterator();
            while (iter.hasNext()) 
            {
                Projectile p = iter.next();
                p.update();

                // Piercing projectile logic 
                if (p.getProjectileIndex() == 3) 
                {
                    for (Enemy e : new ArrayList<>(enemies)) 
                    {
                        if (!e.isAlive() || p.hitEnemies.contains(e)) 
                            break;

                        float ex = e.xLocation - p.xLocation;
                        float ey = e.yLocation - p.yLocation;
                        float dist = (float)Math.sqrt(ex * ex + ey * ey);

                        if (dist < 25f) 
                        {
                            float totalDamage = p.damage;

                            if (p.parentTower.hasAbility2() && e.above80PercentHealth()) // Tower 3 - Ability 2: 50% Increase in damage if enemy above 80% health
                                totalDamage *= 1.5f;

                            if (e.enemyType() == 2) // Enemy 2 - Ability: Takes 50% less damage from area attacks
                                totalDamage /= 2;
                            
                            e.hit(totalDamage);
                            p.decreasePierceCount();

                            p.hitEnemies.add(e);
                            
                            if (p.getPierceCount() <= 0) 
                                p.active = false;
                        }
                    }
                }
                
                if (!p.isActive()) 
                {
                    if (p.getProjectileIndex() == 1 && p.parentTower.hasAbility2() && !p.hasRebounded()) // Tower 1 - Ability 2: Hit extra enemy
                    {
                        Enemy closestEnemy = null;
                        float smallestFoundDistance = Float.MAX_VALUE;

                        for (Enemy e : enemies)
                        {
                            if (e.isAlive() && e != p.target)
                            {
                                float dx = e.xLocation - p.xLocation;
                                float dy = e.yLocation - p.yLocation;
                                float dist = dx * dx + dy * dy; 

                                if (dist < smallestFoundDistance) 
                                {
                                    smallestFoundDistance = dist;
                                    closestEnemy = e;
                                }
                            }
                        }

                        if (closestEnemy != null)
                        {
                            p.updateTarget(closestEnemy);
                            break;
                        }
                    }
                    else if (p.getProjectileIndex() == 2) // Area splash damage (projectileIndex 2)
                    {
                        int areaRadius = (p.parentTower.hasAbility2() ? 80 : 60); // Tower 2 - Ability 2: Increase area 

                        for (Enemy e : new ArrayList<>(enemies)) 
                        {
                            float ex = e.xLocation - p.xLocation;
                            float ey = e.yLocation - p.yLocation;
                            float edist = (float)Math.sqrt(ex * ex + ey * ey);

                            if (edist <= areaRadius) 
                            {
                                
                                if (p.parentTower.hasAbility1()) // Tower 2 - Ability 1: Slow enemies
                                    e.slowEnemy();

                                e.hit(p.damage);
                            }
                        }
                    }

                    iter.remove();
                }
            }

            // Update all enemies and handle deaths/end
            Iterator<Enemy> enemyIter = enemies.iterator();

            ArrayList<Enemy> enemiesToSpawn = new ArrayList<>();
            while (enemyIter.hasNext()) 
            {
                Enemy e = enemyIter.next();
                e.update();
            
                if (e.enemyType() == 5) // Enemy 5 - Ability: Spawn 2 parasites every 8 seconds
                {
                    if (e.parasiteSpawnable())
                    {
                        for (int i = 0; i < 2; i++) 
                        {
                            Enemy parasite = (new Enemy(path, sVariables.getEnemyImage(1), 1, sVariables.getEnemyHealth(1), sVariables.getEnemySpeed(1), sVariables.getEnemyDamage(1), sVariables.getEnemyArmor(1)));

                            // Set position to match the Queen
                            parasite.xPos = e.xPos + Math.cos((2 * Math.PI / 3) * i) * 20;
                            parasite.yPos = e.yPos + Math.sin((2 * Math.PI / 3) * i) * 35;
                            parasite.xLocation = (int) Math.round(parasite.xPos);
                            parasite.yLocation = (int) Math.round(parasite.yPos);

                            // Match the same waypoint so they continue along the same path
                            parasite.currentWaypoint = e.currentWaypoint;

                            enemiesToSpawn.add(parasite);
                        }

                        e.resetParasiteSpawnTimer();
                    }
                }

                if (!e.isAlive()) 
                {
                    if (e.enemyType() == 4) // Enemy 4 - Ability: Spawn 2 smaller enemies on death
                    {
                        for (int i = 0; i < 2; i++) 
                        {
                            Enemy parasite = (new Enemy(path, sVariables.getEnemyImage(1), 1, sVariables.getEnemyHealth(1), sVariables.getEnemySpeed(1), sVariables.getEnemyDamage(1), sVariables.getEnemyArmor(1)));

                            // Set position to match the initial enemy
                            parasite.xPos = e.xPos + Math.cos((2 * Math.PI / 3) * i) * 10;
                            parasite.yPos = e.yPos + Math.sin((2 * Math.PI / 3) * i) * 20;
                            parasite.xLocation = (int) Math.round(parasite.xPos);
                            parasite.yLocation = (int) Math.round(parasite.yPos);

                            // Match the same waypoint so they continue along the same path
                            parasite.currentWaypoint = e.currentWaypoint;

                            enemiesToSpawn.add(parasite);
                        }
                    }

                    bloodSplatter.add(new BloodSplatter(e.xLocation, e.yLocation));
                    panel.gainMoney(sVariables.getEnemyReward(e.enemyType()));

                    totalKills++;
                    enemyIter.remove();
                } 
                else if (e.madeToEnd()) 
                {
                    panel.takeDamage(e.getDamage());
                    enemyIter.remove();
                }
            }

            for (Enemy e : enemiesToSpawn)
            {
                enemies.add(e);
            }

            enemiesToSpawn.clear();

            // 4. Repaint the panel
            repaint();
        }
    }

    public void spawnEnemy(int xLocation, int yLocation)
    {
        Enemy parasite = (new Enemy(path, sVariables.getEnemyImage(1), 1, sVariables.getEnemyHealth(1), sVariables.getEnemySpeed(1), sVariables.getEnemyDamage(1), sVariables.getEnemyArmor(1)));

        parasite.xLocation = xLocation;
        parasite.yLocation = yLocation;

        enemies.add(parasite);
    }

    public void newWave(int currentWaveNumber) 
    {
        this.currentWaveNumber = currentWaveNumber;

        if (currentWaveNumber > 50) // Max 50 waves
        {
            panel.gameOver(false);
            return;
        }

        bloodSplatter.clear();

        currentWave = gameManager.getWave(currentWaveNumber);

        spawningWave = true;

        // Create a queue/list of enemy types to spawn
        List<Integer> spawnQueue = new ArrayList<>();
        for (int i = 0; i < currentWave.getEnemy1Count(); i++) spawnQueue.add(1);
        for (int i = 0; i < currentWave.getEnemy2Count(); i++) spawnQueue.add(2);
        for (int i = 0; i < currentWave.getEnemy3Count(); i++) spawnQueue.add(3);
        for (int i = 0; i < currentWave.getEnemy4Count(); i++) spawnQueue.add(4);
        for (int i = 0; i < currentWave.getEnemy5Count(); i++) spawnQueue.add(5);

        Collections.shuffle(spawnQueue);

        enemySpawnTimer = new java.util.Timer();
        enemySpawnTimer.scheduleAtFixedRate(new TimerTask() {
        int spawnIndex = 0;

        @Override
        public void run() 
        {
            if (spawnIndex < spawnQueue.size()) 
            {
                int enemyType = spawnQueue.get(spawnIndex);

                // Spawn correct enemy type
                enemies.add(new Enemy(path, sVariables.getEnemyImage(enemyType), enemyType, sVariables.getEnemyHealth(enemyType), sVariables.getEnemySpeed(enemyType), sVariables.getEnemyDamage(enemyType), sVariables.getEnemyArmor(enemyType )));

                spawnIndex++;
                repaint();
            } 
            else 
            {
                spawningWave = false;
                enemySpawnTimer.cancel();
            }
        }
        }, 0, 500); // spawn every 500ms)
    }

    public int calculateScore()
    {
        int totalScore = 0;

        totalScore += (totalKills * 10);
        totalScore += ((currentWaveNumber) * 250);

        if (panel.getCurrentHealth() > 0) // Game Won
        {
            totalScore += (panel.getCurrentHealth() * 100);
            totalScore += ((panel.getCurrentMoney() * 5));
        }

        totalScore *= difficultyLevel;

        return totalScore;
    }

    public void mouseClicked(MouseEvent e)
    {
        // Get current cursor coordinates
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) // If left click
        {
            for (MenuButton b : activeMenuButtons)
            {
                if (b.isClicked(x, y))
                {
                    b.onClick.run();
                    repaint();
                    return;
                }
            }    

            for (Tower t : towers)
            {
                if (x > t.xLocation && x < t.xLocation + (t.initialTower.getIconWidth())) // If inside X of tower
                {
                    if (y > t.yLocation && y < t.yLocation + (t.initialTower.getIconHeight())) // If inside Y of tower
                    {
                        towerSelected(t);

                        return;
                    }
                }
            }
        }

        unselectTower();

        super.repaint();
    }

    public void mouseEntered(MouseEvent e)  {}

    public void mousePressed (MouseEvent e) {}

    public void mouseReleased (MouseEvent e) {}

    public void mouseExited (MouseEvent e) {}

    public void mouseMoved(MouseEvent e)
    {

        int x = e.getX();
        int y = e.getY();

        MenuButton prevHovered = hoveredButton;
        hoveredButton = null;
        for (MenuButton b : activeMenuButtons)
        {
            if (x > b.x && x < b.x + b.width && y > b.y && y < b.y + b.height)
            {
                hoveredButton = b;
                break;
            }
        }
        if (prevHovered != hoveredButton) {
            repaint();
        }

    }

    public void mouseDragged(MouseEvent e) {}

    public void paint (Graphics g)
    {  
        super.paint(g);

        g.drawImage(mapImage, 0, 0, null);

        for (BloodSplatter b : bloodSplatter)
        {
            b.bloodSplatter.paintIcon(this, g, b.x, b.y);
        }

        if (selectedTower != null)
        {
            // Tower range
            g.setColor(new Color(160, 160, 160, 128));
            g.fillOval((selectedTower.xLocation + selectedTower.initialTower.getIconWidth() / 2) - (selectedTower.getRange()), (selectedTower.yLocation + selectedTower.initialTower.getIconHeight() / 2) - (selectedTower.getRange()), selectedTower.getRange() * 2, selectedTower.getRange() * 2);

            // Tower Selection
            if (!selectedTower.isBuilt()) // If not selected yet
            {
                g.drawImage(
                    buildTowerMenuImage,
                    (selectedTower.xLocation + selectedTower.initialTower.getIconWidth() / 2) - buildTowerMenuImage.getWidth() / 2,
                    (selectedTower.yLocation + selectedTower.initialTower.getIconHeight() / 2) - buildTowerMenuImage.getHeight() / 2,
                    null
                );
            }
            else if (selectedTower.isBuilt())
            {
                g.drawImage(
                    upgradeTowerMenuImage,
                    (selectedTower.xLocation + selectedTower.image.getIconWidth() / 2) - upgradeTowerMenuImage.getWidth() / 2,
                    (selectedTower.yLocation + selectedTower.image.getIconHeight() / 2) - upgradeTowerMenuImage.getHeight() / 2,
                    null
                );
            }
        }

        for (Projectile p : projectiles)
        {
            if (p.active)
                p.image.paintIcon(this, g, p.xLocation, p.yLocation);
        }

        for (Enemy e : enemies)
        {   
            if (e.isAlive()) 
            {
                if (!e.isTunneled())
                    e.image.paintIcon(this, g, e.xLocation, e.yLocation);

                // Draw health bar above enemy
                int barWidth = 40; // total width of health bar
                int barHeight = 6;
                int barY = e.yLocation - 10; // 10 pixels above enemy
                int barX = e.xLocation + e.image.getIconWidth() / 2 - barWidth / 2;
                float healthPercent = Math.max(0f, Math.min(1f, e.getHealth() / e.getStartingHealth()));
                int greenWidth = (int)(barWidth * healthPercent);
                int greenX = barX + (barWidth - greenWidth) / 2;

                // Background Bar
                g.setColor(Color.DARK_GRAY);
                g.fillRect(barX, barY, barWidth, barHeight);

                // Health
                g.setColor(Color.GREEN);
                g.fillRect(greenX, barY, greenWidth, barHeight);

                // Border
                g.setColor(Color.BLACK);
                g.drawRect(barX, barY, barWidth, barHeight);
            }
        }

        for (Tower t : towers)
        {
            if (!t.isBuilt())
                t.initialTower.paintIcon(this, g, t.xLocation, t.yLocation);
            else
                t.image.paintIcon(this, g, t.xLocation, t.yLocation);
        }

        // TOOL TIP
        if (hoveredButton != null && selectedTower != null) {
            int buttonIndex = activeMenuButtons.indexOf(hoveredButton) + 1;
            int width = 260;
            int tooltipX, tooltipY;
            tooltipY = hoveredButton.y;

            if (!selectedTower.onRightSide())
                tooltipX = hoveredButton.x + hoveredButton.width + 10;
            else
                tooltipX = hoveredButton.x - width - 10;

            String[] lines;
            if (!selectedTower.isBuilt()) // Show stats
            {
                String name = sVariables.getTowerName(buttonIndex);
                int damage = (int) sVariables.getTowerDamage(buttonIndex);
                int range = sVariables.getTowerRange(buttonIndex);
                float fireRate = sVariables.getTowerFirerate(buttonIndex);
                String towerInfo = sVariables.getTowerAbility(buttonIndex);

                lines = new String[] {
                    name,
                    damage + " attack damage",
                    range + " attack range",
                    String.format("Fires every %.1f sec", 1f / fireRate),
                    towerInfo
                };

                if (buttonIndex == 4) 
                {
                    lines[1] = "3 damages / second";
                    lines[3] = "Constant fire";
                }

                if (buttonIndex == 5) 
                {
                    lines[3] = "20% firerate increase";
                }
            } 
            else // Show upgrades if tower built
            {
                if (buttonIndex == 1) // Tower Level Up
                { 
                    int nextLevel = selectedTower.getCurrentLevel() + 1;
                    if (nextLevel <= 3) 
                    {
                        int upgradeCost = (nextLevel == 2 ? sVariables.getTowerCostUpgrade1(selectedTower.getTowerIndex()) : sVariables.getTowerCostUpgrade2(selectedTower.getTowerIndex()));
                        float newDmg = (nextLevel == 2) ? sVariables.getTowerDamageUpgrade1(selectedTower.getTowerIndex()) : sVariables.getTowerDamageUpgrade2(selectedTower.getTowerIndex());
                        int newRange = (nextLevel == 2) ? sVariables.getTowerRangeUpgrade1(selectedTower.getTowerIndex()) : sVariables.getTowerRangeUpgrade2(selectedTower.getTowerIndex());
                        float newFR = (nextLevel == 2) ? sVariables.getTowerFirerateUpgrade1(selectedTower.getTowerIndex()) : sVariables.getTowerFirerateUpgrade2(selectedTower.getTowerIndex());
                        lines = new String[] 
                        {
                            "Upgrade to Level " + nextLevel + " (" + upgradeCost + ")",
                            "Damage: " + selectedTower.getDamage() + " -> " + newDmg,
                            "Range: " + selectedTower.getRange() + " -> " + newRange,
                            String.format("Fire Rate: %.2f -> %.2f", selectedTower.getFireRate(), newFR)
                        };
                    }
                     else 
                        lines = new String[] {"Max Level"};
                } 
                else if (buttonIndex == 2) // Ability 2
                { 
                    String abilityInfo = sVariables.getAbility2Info(selectedTower.getTowerIndex());
                    String abilityDescription = sVariables.getAbility2Description(selectedTower.getTowerIndex());
                    int abilityCost = sVariables.getAbility2Cost(selectedTower.getTowerIndex());

                    lines = new String[] 
                    {
                        abilityInfo,
                        abilityDescription,
                        "Cost: " + abilityCost
                    };
                    
                    if (selectedTower.hasAbility2())
                        lines[2] = "Cost: ALREADY OWNED";
                } 
                else if (buttonIndex == 3) // Refund Tower
                { 
                    int refund = (int)(0.85 * selectedTower.getCost());
                    lines = new String[] {"Refund Tower", "You will get: " + refund + " DNA"};
                } 
                else if (buttonIndex == 4) // Ability 1
                { 
                    String abilityInfo = sVariables.getAbility1Info(selectedTower.getTowerIndex());
                    String abilityDescription = sVariables.getAbility1Description(selectedTower.getTowerIndex());
                    int abilityCost = sVariables.getAbility1Cost(selectedTower.getTowerIndex());

                    lines = new String[] 
                    {
                        abilityInfo,
                        abilityDescription,
                        "Cost: " + abilityCost
                    };
                                        
                    if (selectedTower.hasAbility1())
                        lines[2] = "Cost: ALREADY OWNED";
                }
                else 
                {
                    lines = new String[] {""};
                }
            }

            int height = 20 + lines.length * 18;
            Graphics2D g2 = (Graphics2D) g.create();

            // Background
            g2.setColor(new Color(30, 30, 60, 220));
            g2.fillRoundRect(tooltipX, tooltipY, width, height, 16, 16);
            g2.setColor(Color.WHITE);

            // Tower Name
            g2.setFont(new Font("Bell MT", Font.BOLD, 17));
            g2.drawString(lines[0], tooltipX + 10, tooltipY + 25);

            g2.setFont(new Font("Calibri", Font.PLAIN, 13));
            for (int i = 1; i < lines.length; i++) 
            {
                if (i == lines.length - 1 && !selectedTower.isBuilt()) 
                    g2.setFont(new Font("Calibri", Font.PLAIN, 12));

                g2.drawString(lines[i], tooltipX + 10, tooltipY + 25 + i * 16);
            }
            
            g2.dispose();
        }

        if (selectedTower != null && selectedTower.isBuilt())
        {
            g.setColor(Color.RED);
            //for (MenuButton b : activeMenuButtons) 
            //{
                //g.drawRect(b.x, b.y, b.width, b.height);
            //}
        }
    }

    private void towerSelected(Tower tower)
    {
        selectedTower = tower;

        panel.towerSelected();

        activeMenuButtons.clear();
        int centerX = tower.xLocation + tower.initialTower.getIconWidth() / 2;
        int centerY = tower.yLocation + tower.initialTower.getIconHeight() / 2;

        int radius = 45;  // distance from tower center to square center
        int squareSize = 40; // size of each square button
        if (!tower.isBuilt())
        {
            for (int i = 0; i < 5; i++) {
                double angle = Math.toRadians(-90 + i * 72); // -90 to start at top, then every 72 degrees

                int x = (int)(centerX + Math.cos(angle) * radius) - squareSize / 2;
                int y = (int)(centerY + Math.sin(angle) * radius) - squareSize / 2;

                final int buttonIndex = i + 1;

                activeMenuButtons.add(new MenuButton(x, y, squareSize, squareSize, () -> {buildTower(buttonIndex);}));
            }
        }
        else
        {
            radius = 50;
            squareSize = 45;

            for (int i = 0; i < 4; i++) {
                double angle = Math.toRadians(-90 + i * 90); // -90 to start at top, then every 90 degrees

                int x = (int)(centerX + Math.cos(angle) * radius) - squareSize / 2;
                int y = (int)(centerY + Math.sin(angle) * radius) - squareSize / 2;

                final int buttonIndex = i + 1;

                activeMenuButtons.add(new MenuButton(x, y, squareSize, squareSize, () -> {upgradeTower(buttonIndex);}));
            }
        }
    }

    private void buildTower(int buttonIndex)
    {   
        if (selectedTower != null && !selectedTower.isBuilt())
        {
            if (panel.getCurrentMoney() >= sVariables.getTowerCost(buttonIndex))
            {
                selectedTower.setTowerStats(sVariables.getTowerDamage(buttonIndex), sVariables.getTowerRange(buttonIndex), sVariables.getTowerFirerate(buttonIndex), sVariables.getTowerImage(buttonIndex), sVariables.getTowerName(buttonIndex), sVariables.getTowerCost(buttonIndex), buttonIndex);

                selectedTower.built();

                panel.spendMoney(sVariables.getTowerCost(buttonIndex));

                panel.towerSelected();

                if (buttonIndex == 5) // Adrenaline Tower
                {
                    for (Tower t : towers) 
                    {
                        if (selectedTower != t)
                        {
                            for (Tower buffTower : t.buffTowers)
                            {
                                if (buffTower == selectedTower) // If already buffed
                                    break;
                            }

                            float ex = t.xLocation - selectedTower.xLocation;
                            float ey = t.yLocation - selectedTower.yLocation;
                            float edist = (float)Math.sqrt(ex * ex + ey * ey);

                            if (edist <= selectedTower.getRange()) 
                            {
                                t.receiveBuff(selectedTower); // Apply damage to enemies in area 
                            }
                        }
                    }             
                }
            }
        }

        unselectTower();
    }
   
    private void upgradeTower(int buttonIndex)
    {
        int towerIndex = selectedTower.getTowerIndex();

        int towerLevel = selectedTower.getCurrentLevel();
        int upgradeTowerCost = (towerLevel == 1 ? sVariables.getTowerCostUpgrade1(towerIndex) : sVariables.getTowerCostUpgrade2(towerIndex));

        switch (buttonIndex)
        {   
            case 1: // Upgrading tower to next level
                if (selectedTower != null && selectedTower.isBuilt() && towerLevel < 3)
                {
                    if (panel.getCurrentMoney() >= upgradeTowerCost)
                    {
                        panel.spendMoney(upgradeTowerCost);

                        if (selectedTower.getCurrentLevel() == 1) // Upgrade to level 2
                            selectedTower.upgradeTower(sVariables.getTowerDamageUpgrade1(towerIndex), sVariables.getTowerRangeUpgrade1(towerIndex), sVariables.getTowerFirerateUpgrade1(towerIndex));
                        else if (selectedTower.getCurrentLevel() == 2) // Upgrade to level 3
                            selectedTower.upgradeTower(sVariables.getTowerDamageUpgrade2(towerIndex), sVariables.getTowerRangeUpgrade2(towerIndex), sVariables.getTowerFirerateUpgrade2(towerIndex));    

                        unselectTower();    
                    }
                }
            
                break;

            case 2: // Ability 2
                if (panel.getCurrentMoney() >= sVariables.getAbility2Cost(towerIndex) && !selectedTower.hasAbility2())
                {
                    selectedTower.getAbility2();

                    panel.spendMoney(sVariables.getAbility2Cost(towerIndex));

                    unselectTower();
                }

                break;

            case 3: // Refund tower
                if (selectedTower != null && selectedTower.isBuilt())
                {
                    if (towerIndex == 5)
                    {
                        for (Tower t : towers)
                        {
                            for (Tower buffTower : t.buffTowers)
                            {
                                if (selectedTower == buffTower) // If already buffed
                                {
                                    t.removeBuff(selectedTower);
                                    break;
                                }
                            } 
                        }
                    }

                    panel.gainMoney((int) (0.85 * selectedTower.getCost()));
                    selectedTower.resetTower();

                    unselectTower();
                }

                break;

            case 4: // Ability 1
                if (panel.getCurrentMoney() >= sVariables.getAbility1Cost(towerIndex) && !selectedTower.hasAbility1())
                {
                    selectedTower.getAbility1();

                    panel.spendMoney(sVariables.getAbility1Cost(towerIndex));

                    unselectTower();
                }

                break;
        }
    }

    private void createTower(int xLocation, int yLocation)
    {
        towers.add(new Tower(this, xLocation, yLocation));
    }

    public Tower getSelectedTower()
    {
        return selectedTower;
    }

    public void MenuButton(int x, int y, int radius, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.onClick = onClick;
    }

    public boolean isClicked(int mx, int my) {
        return Math.hypot(mx - x, my - y) <= radius;
    }

    private void unselectTower()
    {
        selectedTower = null;
        activeMenuButtons.clear();
        panel.towerSelected();
    }

    public boolean isSpawningWave() 
    {
        return spawningWave;
    }

    public void stopUpdate()
    {
        timer.cancel();
        enemySpawnTimer.cancel();
    }
}
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 07/08/25
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
        try {
            buildTowerMenuImage = ImageIO.read(new File("resources/assets/buildTowerMenu.png"));
            upgradeTowerMenuImage = ImageIO.read(new File("resources/assets/UpgradeTowerMenu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    StaticVariables sVariables = new StaticVariables();
    GameManager gameManager = new GameManager();
    private java.util.Timer enemySpawnTimer = null;

    private boolean spawningWave = false;

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
        try {
            mapImage = ImageIO.read(new File(mapImageName + levelIndex + ".png"));
        } catch (IOException e) { e.printStackTrace(); }

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

        
        // add waypoints for enemies to follow
        for (int i = 0; i < sVariables.getAmountOfMapPoints(); i++)
        {
            int coord1 = sVariables.getCoord(i, 0);
            int coord2 = sVariables.getCoord(i, 1);

            path.add(new Point(coord1, coord2));
        }

        // add building spots for towers
        for (int i = 0; i < sVariables.getAmountOfTowerSpots(); i++)
        {
            int coord1 = sVariables.getTCoord(i, 0);
            int coord2 = sVariables.getTCoord(i, 1);

            createTower(coord1, coord2);
        }

        // Works as update function (runs 60 times a second - 60fps)
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, 1000 / 60); // Update at 60fps
    }

    class UpdateTask extends TimerTask // Runs 60 times a second
    {
        public void run() 
        {
            for (Tower t : towers)
            {
                if (t.isBuilt())
                    t.update();
            }

            Iterator<Projectile> iter = projectiles.iterator();
            while (iter.hasNext())
            {
                Projectile p = iter.next();
                p.update();

                if (p.getProjectileIndex() == 3) // If piercing projectile
                { 
                    for (Enemy e : new ArrayList<>(enemies)) // iterate over a copy to avoid concurrent modification
                    {
                        if (!e.isAlive() || p.hitEnemies.contains(e)) 
                            break;

                        float ex = e.xLocation - p.xLocation;
                        float ey = e.yLocation - p.yLocation;
                        float dist = (float)Math.sqrt(ex * ex + ey * ey);

                        if (dist < 15f) 
                        {
                            e.hit(p.damage);
                            p.decreasePierceCount();

                            p.hitEnemies.add(e);

                            if (p.getPierceCount() <= 0)
                            {
                                p.active = false;
                            }
                        }
                    }
                }

                if (!p.isActive()) 
                {
                    // Area slow effect for projectileIndex 2
                    if (p.getProjectileIndex() == 2) 
                    {
                        int areaRadius = 60; // match with projectile's area radius
                        for (Enemy e : new ArrayList<>(enemies)) // Iterate over a copy to avoid crashes
                        {
                            float ex = e.xLocation - p.xLocation;
                            float ey = e.yLocation - p.yLocation;
                            float edist = (float)Math.sqrt(ex * ex + ey * ey);
                            if (edist <= areaRadius) 
                            {
                                e.hit(p.damage); // Apply damage to enemies in area
                            }
                        }
                    }

                    iter.remove();
                }
            }

            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext())
            {
                Enemy e = enemyIter.next();
                e.update();

                if (!e.isAlive())
                {
                    bloodSplatter.add(new BloodSplatter(e.xLocation, e.yLocation));

                    panel.gainMoney(sVariables.getEnemyReward(e.enemyType()));

                    enemyIter.remove();  
                }
                else if (e.madeToEnd())
                {
                    panel.takeDamage(e.getDamage());

                    enemyIter.remove();
                }
            }

            repaint();
        }
    }

    public void newWave(int currentWaveNumber) 
    {
        if (currentWaveNumber > 50) // Max 50 waves
        {
            levelWon();
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
                enemies.add(new Enemy(path, sVariables.getEnemyImage(enemyType), enemyType, sVariables.getEnemyHealth(enemyType), sVariables.getEnemySpeed(enemyType), sVariables.getEnemyDamage(enemyType)));

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

    private void levelWon()
    {
        timer.cancel();
        enemySpawnTimer.cancel();


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
            if (e.isAlive()) {
                e.image.paintIcon(this, g, e.xLocation, e.yLocation);

                // Draw health bar above enemy
                int barWidth = 40; // total width of health bar
                int barHeight = 6;
                int barY = e.yLocation - 10; // 10 pixels above enemy
                int barX = e.xLocation + e.image.getIconWidth() / 2 - barWidth / 2;
                float healthPercent = Math.max(0f, Math.min(1f, e.getHealth() / e.getStartingHealth()));
                int greenWidth = (int)(barWidth * healthPercent);
                int greenX = barX + (barWidth - greenWidth) / 2;

                // Draw background bar
                g.setColor(Color.DARK_GRAY);
                g.fillRect(barX, barY, barWidth, barHeight);
                // Draw green health
                g.setColor(Color.GREEN);
                g.fillRect(greenX, barY, greenWidth, barHeight);
                // Optional: draw border
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

        if (hoveredButton != null && selectedTower != null && !selectedTower.isBuilt())
        {
            int buttonIndex = activeMenuButtons.indexOf(hoveredButton) + 1;

            String name = sVariables.getTowerName(buttonIndex);
            int damage = (int) sVariables.getTowerDamage(buttonIndex);
            int range = sVariables.getTowerRange(buttonIndex);
            float fireRate = sVariables.getTowerFirerate(buttonIndex);

            // Tooltip text lines
            String[] lines = 
            {
                name,
                damage + " attack damage",
                range + " attack range",
                String.format("Fires every %.1f sec", 1f / fireRate)
            };

            if (buttonIndex == 5)
            {
                lines[3] = "20% firerate increase";
            }

            // Tooltip position (right of button)
            int tooltipX = hoveredButton.x + hoveredButton.width + 10;
            int tooltipY = hoveredButton.y;

            // Tooltip size
            int width = 180;
            int height = 20 + lines.length * 18;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(30, 30, 60, 220));
            g2.fillRoundRect(tooltipX, tooltipY, width, height, 16, 16);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(lines[0], tooltipX + 10, tooltipY + 25);
            g2.setFont(new Font("Arial", Font.PLAIN, 13));
            for (int i = 1; i < lines.length; i++) 
            {
                g2.drawString(lines[i], tooltipX + 10, tooltipY + 25 + i * 16);
            }

            g2.dispose();
        }

        if (selectedTower != null && selectedTower.isBuilt())
        {
        g.setColor(Color.RED);
        for (MenuButton b : activeMenuButtons) 
        {
            g.drawRect(b.x, b.y, b.width, b.height);
        }
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
                            float ex = t.xLocation - selectedTower.xLocation;
                            float ey = t.yLocation - selectedTower.yLocation;
                            float edist = (float)Math.sqrt(ex * ex + ey * ey);
                            if (edist <= selectedTower.getRange()) 
                            {
                                t.receiveBuff(1.2f); // Apply damage to enemies in area 
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
        switch (buttonIndex)
        {       
            case 2:
                if (panel.getCurrentMoney() >= 300)
                {
                    panel.spendMoney(300);
                    selectedTower.upgradeTower(2);
                }

                break;
            case 3:
                if (selectedTower != null && selectedTower.isBuilt())
                {
                    if (selectedTower.getTowerIndex() == 5)
                    {
                        for (Tower t : towers)
                        {
                            t.removeBuff();
                        }
                    }

                    panel.gainMoney((int) (0.85 * selectedTower.getCost()));
                    selectedTower.refund();

                    unselectTower();
                }

                break;
            case 4:
                if (panel.getCurrentMoney() >= 200)
                {
                    panel.spendMoney(200);
                    selectedTower.upgradeTower(1);
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
    }
}
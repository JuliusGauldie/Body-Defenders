
/**
 * Represents the main gameplay panel for the tower defense game.
 * Handles updating and rendering enemies, towers, projectiles, waves, menus, and player interactions.
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class GameplayPanel extends JPanel implements MouseListener, MouseMotionListener 
{
    // Canvas size
    public int CANVAS_WIDTH = 800; // Game board width
    public int CANVAS_HEIGHT = 520; // Game board height

    // Images
    private static BufferedImage mapImage;
    private static BufferedImage buildTowerMenuImage;
    private static BufferedImage upgradeTowerMenuImage;
    private String mapImageName = "resources/assets/map";

    // Load static menu images
    static {
        try {
            buildTowerMenuImage = ImageIO.read(new File("resources/assets/buildTowerMenu.png"));
            upgradeTowerMenuImage = ImageIO.read(new File("resources/assets/UpgradeTowerMenu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Arraylists
    public ArrayList<Enemy> enemies = new ArrayList<>(); // Holds enemies
    private ArrayList<Tower> towers = new ArrayList<>(); // Holds towers
    public ArrayList<Projectile> projectiles = new ArrayList<>(); // Holds projectiles
    private ArrayList<MenuButton> activeMenuButtons = new ArrayList<>(); // Holds buttons
    private ArrayList<BloodSplatter> bloodSplatter = new ArrayList<>(); // Holds bloodsplatters

    // Path and tower locations
    ArrayList<Point> path = new ArrayList<>();
    ArrayList<Point> places = new ArrayList<>();

    // Tower/menu selection
    MenuButton hoveredButton = null;
    private Tower selectedTower = null;
    private WaveInfo currentWave = null;
    MenuButton returnButton;
    int x, y, radius; // Temporary variables for circular buttons
    Runnable onClick;

    // Game control
    private GameLayerPanel panel;
    JLabel infoLabel; // Optional UI info
    java.util.Timer timer;
    java.util.Timer enemySpawnTimer = null;
    private boolean spawningWave = false;
    private boolean lastWaveSpawned = false;

    // Difficulty & score
    private float difficultyLevel = 1; // 0.5 = Easy, 1 = Normal, 2 = Hard
    private int totalKills = 0;
    private int currentWaveNumber = 0;

    // Classes
    StaticVariables sVariables = new StaticVariables();
    GameManager gameManager = new GameManager();

    /**
     * Constructor for GameplayPanel.
     * 
     * @param panel Reference to parent GameLayerPanel
     */
    public GameplayPanel(GameLayerPanel panel) {
        this.panel = panel;

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        addMouseListener(this);
        addMouseMotionListener(this);

        // Return button at top-right
        returnButton = new MenuButton(705, 2, 70, 58, () -> {
            timer.cancel();
            if (enemySpawnTimer != null)
                enemySpawnTimer.cancel();
            spawningWave = false;
            panel.returnToMenu();
        });

        super.repaint();
    }

    /**
     * Starts a new game, resetting enemies, towers, and path
     * 
     * @param levelIndex      Index of the map to load
     * @param difficultyIndex Difficulty multiplier
     */
    public void newGame(int levelIndex, float difficultyIndex) {
        // Clear old variables
        totalKills = 0;
        enemies.clear();
        bloodSplatter.clear();
        this.difficultyLevel = difficultyIndex;
        selectedTower = null;
        lastWaveSpawned = false;

        // Reset towers
        for (Tower t : towers)
            if (t.isBuilt())
                t.resetTower();

        // Load map
        try {
            mapImage = ImageIO.read(new File(mapImageName + levelIndex + ".png"));
        } catch (IOException e) {
            System.out.println("ERROR - Map image not found!");
        }

        // Enemy path points
        if (path.isEmpty()) {
            for (int i = 0; i < sVariables.getAmountOfMapPoints(); i++)
                path.add(new Point(sVariables.getCoord(i, 0), sVariables.getCoord(i, 1)));
        }

        // Tower build locations
        if (towers.isEmpty()) {
            for (int i = 0; i < sVariables.getAmountOfTowerSpots(); i++)
                createTower(sVariables.getTCoord(i, 0), sVariables.getTCoord(i, 1));
        }

        // Start update loop (60 FPS)
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, 1000 / 60);
    }

    /**
     * Task run 60 times per second to update towers, projectiles, enemies, and
     * handle deaths/abilities.
     */
    class UpdateTask extends TimerTask {
        public void run() {
            // Check if won
            if (enemies.isEmpty() && lastWaveSpawned)
                panel.gameOver(false);


            // Update towers
            for (Tower t : towers)
                if (t.isBuilt())
                    t.update();

            // Update projectiles and apply effects
            Iterator<Projectile> iter = projectiles.iterator();
            while (iter.hasNext()) {
                Projectile p = iter.next();
                p.update();

                handleProjectileLogic(p, iter);
            }

            // Update enemies and handle deaths/spawning
            updateEnemies();

            // Repaint panel
            repaint();
        }
    }

    /**
     * Handles the logic for a single projectile, including piercing, area damage,
     * special tower abilities, and removing inactive projectiles.
     * 
     * @param p    The projectile being updated
     * @param iter Iterator over the projectiles list for removal if needed
     */
    private void handleProjectileLogic(Projectile p, Iterator<Projectile> iter) 
    {
        // Handle piercing projectiles 
        if (p.getPierceCount() > 0) 
        {
            for (Enemy e : new ArrayList<>(enemies)) 
            {
                if (!e.isAlive() || p.hitEnemies.contains(e))
                    break;

                float dx = e.xLocation - p.xLocation;
                float dy = e.yLocation - p.yLocation;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);

                if (dist < 25f) {
                    float totalDamage = p.damage;

                    // Tower 3 - Ability 2: +50% damage if enemy above 80% health
                    if (p.parentTower.hasAbility2() && e.above80PercentHealth())
                        totalDamage *= 1.5f;

                    e.hit(totalDamage, p.parentTower);
                    p.decreasePierceCount();
                    p.hitEnemies.add(e);

                    if (p.getPierceCount() <= 0)
                        p.active = false;
                }
            }
        }

        // Handle inactive projectiles
        if (!p.isActive()) {
            // Tower 1 - Ability 2: Hit extra enemy if reboundable
            if (p.getProjectileIndex() == 1 && p.parentTower.hasAbility2() && !p.hasRebounded()) {
                Enemy closestEnemy = null;
                float smallestDistance = Float.MAX_VALUE;

                for (Enemy e : new ArrayList<>(enemies)) {
                    if (e.isAlive() && e != p.target) {
                        float dx = e.xLocation - p.xLocation;
                        float dy = e.yLocation - p.yLocation;
                        float dist = dx * dx + dy * dy;

                        if (dist < smallestDistance) {
                            smallestDistance = dist;
                            closestEnemy = e;
                        }
                    }
                }

                if (closestEnemy != null) {
                    p.updateTarget(closestEnemy);
                    return; // Do not remove projectile yet
                }
            }
            // Tower 2 - Area splash damage (projectileIndex 2)
            else if (p.getProjectileIndex() == 2) 
            {
                int areaRadius = p.parentTower.hasAbility2() ? 80 : 60;

                for (Enemy e : new ArrayList<>(enemies)) 
                {
                    float dx = e.xLocation - p.xLocation;
                    float dy = e.yLocation - p.yLocation;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);

                    if (dist <= areaRadius) {
                        // Tower 2 - Ability 1: Slow enemies
                        if (p.parentTower.hasAbility1())
                            e.slowEnemy();

                        // Enemy 2 - Ability: Takes 50% less damage from area attack
                        e.hit(e.enemyType() == 2 ? p.damage / 2 : p.damage, p.parentTower);
                    }
                }
            }

            // Remove projectile from list
            iter.remove();
        }
    }

    /**
     * Updates all enemies, handles death events, special enemy abilities,
     * spawning parasites/splits, and reaching the end of the path.
     */
    private void updateEnemies() {
        Iterator<Enemy> enemyIter = enemies.iterator();
        ArrayList<Enemy> enemiesToSpawn = new ArrayList<>();

        while (enemyIter.hasNext()) {
            Enemy e = enemyIter.next();
            e.update();

            // Enemy 5 - Queen: Spawn 2 parasites every 8 seconds
            if (e.enemyType() == 5 && e.parasiteSpawnable()) {
                for (int i = 0; i < 2; i++) {
                    Enemy parasite = new Enemy(path, sVariables.getEnemyImage(1), 1,
                            sVariables.getEnemyHealth(1), sVariables.getEnemySpeed(1),
                            sVariables.getEnemyDamage(1), sVariables.getEnemyArmor(1));

                    parasite.xLocation = e.xLocation + (float) Math.cos((2 * Math.PI / 3) * i) * 20;
                    parasite.yLocation = e.yLocation + (float) Math.sin((2 * Math.PI / 3) * i) * 35;
                    parasite.currentWaypoint = e.currentWaypoint;

                    enemiesToSpawn.add(parasite);
                }
                e.resetParasiteSpawnTimer();
            }

            // If enemy is defeated
            if (!e.isAlive()) {
                // Enemy 4: Splits into 2 smaller enemies on death
                if (e.enemyType() == 4) {
                    for (int i = 0; i < 2; i++) {
                        Enemy smallEnemy = new Enemy(path, sVariables.getEnemyImage(1), 1,
                                sVariables.getEnemyHealth(1), sVariables.getEnemySpeed(1),
                                sVariables.getEnemyDamage(1), sVariables.getEnemyArmor(1));

                        smallEnemy.xLocation = e.xLocation + (float) Math.cos((2 * Math.PI / 3) * i) * 10;
                        smallEnemy.yLocation = e.yLocation + (float) Math.sin((2 * Math.PI / 3) * i) * 20;
                        smallEnemy.currentWaypoint = e.currentWaypoint;

                        enemiesToSpawn.add(smallEnemy);
                    }
                }

                // Add blood effect
                bloodSplatter.add(new BloodSplatter(e.xLocation, e.yLocation));

                // Reward money
                if (e.getKilledByTower().checkAbility1BuffedTowers())
                    panel.gainMoney(sVariables.getEnemyReward(e.enemyType()) * 2);
                else
                    panel.gainMoney(sVariables.getEnemyReward(e.enemyType()));
                totalKills++;
                enemyIter.remove();
            }
            // Enemy reached end of path
            else if (e.madeToEnd()) {
                panel.takeDamage(e.getDamage());
                enemyIter.remove();
            }
        }

        // Add any newly spawned enemies from abilities
        enemies.addAll(enemiesToSpawn);
        enemiesToSpawn.clear();
    }

    /**
     * Spawns a new wave of enemies.
     * 
     * @param currentWaveNumber Index of the current wave
     */
    public void newWave(int currentWaveNumber) {
        if (currentWaveNumber > 15)
            return;

        this.currentWaveNumber = currentWaveNumber;

        bloodSplatter.clear();

        currentWave = gameManager.getWave(currentWaveNumber);
        spawningWave = true;

        // Create spawn queue
        ArrayList<Integer> spawnQueue = new ArrayList<>();
        for (int i = 0; i < currentWave.getEnemy1Count(); i++)
            spawnQueue.add(1);
        for (int i = 0; i < currentWave.getEnemy2Count(); i++)
            spawnQueue.add(2);
        for (int i = 0; i < currentWave.getEnemy3Count(); i++)
            spawnQueue.add(3);
        for (int i = 0; i < currentWave.getEnemy4Count(); i++)
            spawnQueue.add(4);
        for (int i = 0; i < currentWave.getEnemy5Count(); i++)
            spawnQueue.add(5);

        Collections.shuffle(spawnQueue);

        enemySpawnTimer = new java.util.Timer();
        enemySpawnTimer.scheduleAtFixedRate(new TimerTask() {
            int spawnIndex = 0;

            @Override
            public void run() {
                if (spawnIndex < spawnQueue.size()) {
                    int enemyType = spawnQueue.get(spawnIndex);
                    enemies.add(new Enemy(path, sVariables.getEnemyImage(enemyType), enemyType,
                            sVariables.getEnemyHealth(enemyType) * (difficultyLevel == 0.5 ? 0.8f : difficultyLevel == 1 ? 1f : 1.2f),
                            sVariables.getEnemySpeed(enemyType) * (difficultyLevel == 0.5 ? 0.8f : difficultyLevel == 1 ? 1f : 1.2f),
                            sVariables.getEnemyDamage(enemyType) * (difficultyLevel == 0.5 ? 0.8f : difficultyLevel == 1 ? 1f : 1.2f),
                            sVariables.getEnemyArmor(enemyType)));

                    spawnIndex++;
                    repaint();
                } else {
                    spawningWave = false;

                    if (currentWaveNumber >= 15)
                        lastWaveSpawned = true;

                    enemySpawnTimer.cancel();
                }
            }
        }, 0, 500); // spawn every 500ms
    }

    /**
     * Calculates the total score for the current game.
     * Score is based on kills, waves, remaining health, money, and difficulty.
     * 
     * @return Total score
     */
    public int calculateScore() {
        int totalScore = totalKills * 10 + currentWaveNumber * 250;

        if (panel.getCurrentHealth() > 0) {
            totalScore += panel.getCurrentHealth() * 100;
            totalScore += panel.getCurrentMoney() * 5;
        }

        totalScore *= difficultyLevel;
        return totalScore;
    }

    // Mouse Events
    public void mouseEntered(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    /**
     * Handles mouse click events on towers, menu buttons, and return button.
     */
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            // Check if any menu button was clicked
            for (MenuButton b : activeMenuButtons) {
                if (b.isClicked(x, y)) {
                    b.onClick.run();
                    repaint();
                    return;
                }
            }

            // Check if return button was clicked
            if (returnButton.isClicked(x, y)) {
                returnButton.onClick.run();
                return;
            }

            // Check if a tower was clicked
            for (Tower t : towers) {
                if (x > t.xLocation && x < t.xLocation + t.initialTower.getIconWidth() &&
                        y > t.yLocation && y < t.yLocation + t.initialTower.getIconHeight()) {
                    towerSelected(t); // Select the tower
                    return;
                }
            }
        }

        // If nothing was clicked, unselect tower
        unselectTower();
        repaint();
    }

    /**
     * Handles mouse movement to detect hover over menu buttons.
     */
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        MenuButton prevHovered = hoveredButton;
        hoveredButton = null;

        // Check if hovering over any active menu buttons
        for (MenuButton b : activeMenuButtons) {
            if (x > b.x && x < b.x + b.width && y > b.y && y < b.y + b.height) {
                hoveredButton = b;
                break;
            }
        }

        // Only repaint if hover state changed
        if (prevHovered != hoveredButton) {
            repaint();
        }
    }

    /**
     * Paints the entire game panel
     */
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the map
        g.drawImage(mapImage, 0, 0, null);

        // Draw blood splatter effects
        for (BloodSplatter b : bloodSplatter) {
            b.bloodSplatter.paintIcon(this, g, (int) b.x, (int) b.y);
        }

        // Draw tower range overlay if a tower is selected
        if (selectedTower != null) {
            g.setColor(new Color(160, 160, 160, 128));
            int range = selectedTower.getRange();
            int centerX = selectedTower.xLocation + selectedTower.initialTower.getIconWidth() / 2;
            int centerY = selectedTower.yLocation + selectedTower.initialTower.getIconHeight() / 2;
            g.fillOval(centerX - range, centerY - range, range * 2, range * 2);

            // Draw tower selection menu image
            if (!selectedTower.isBuilt()) {
                g.drawImage(buildTowerMenuImage,
                        centerX - buildTowerMenuImage.getWidth() / 2,
                        centerY - buildTowerMenuImage.getHeight() / 2,
                        null);
            } else {
                g.drawImage(upgradeTowerMenuImage,
                        centerX - upgradeTowerMenuImage.getWidth() / 2,
                        centerY - upgradeTowerMenuImage.getHeight() / 2,
                        null);
            }
        }

        // Draw active projectiles
        for (Projectile p : new ArrayList<>(projectiles)) {
            if (p.active)
                p.image.paintIcon(this, g, p.xLocation, p.yLocation);
        }

        // Draw enemies and their health bars
        for (Enemy e : new ArrayList<>(enemies)) 
        {
            if (!e.isAlive())
                continue;

            if (!e.isTunneled())
                e.image.paintIcon(this, g, (int) e.xLocation, (int) e.yLocation);

            // Draw health bar above enemy
            int barWidth = 40;
            int barHeight = 6;
            int barX = (int) e.xLocation + e.image.getIconWidth() / 2 - barWidth / 2;
            int barY = (int) e.yLocation - 10;
            float healthPercent = Math.max(0f, Math.min(1f, e.getHealth() / e.getStartingHealth()));
            int greenWidth = (int) (barWidth * healthPercent);
            int greenX = barX + (barWidth - greenWidth) / 2;

            g.setColor(Color.DARK_GRAY);
            g.fillRect(barX, barY, barWidth, barHeight);

            g.setColor(Color.GREEN);
            g.fillRect(greenX, barY, greenWidth, barHeight);

            g.setColor(Color.BLACK);
            g.drawRect(barX, barY, barWidth, barHeight);
        }

        // Draw all towers
        for (Tower t : towers) {
            if (!t.isBuilt())
                t.initialTower.paintIcon(this, g, t.xLocation, t.yLocation);
            else
                t.image.paintIcon(this, g, t.xLocation, t.yLocation);
        }

        // Draw tooltip for hovered menu button
        if (hoveredButton != null && selectedTower != null) {
            // Figure out which button is hovered
            int buttonIndex = activeMenuButtons.indexOf(hoveredButton) + 1;

            // Tooltip width & position
            int tooltipWidth = 260;
            int tooltipX, tooltipY = hoveredButton.y;

            // Decide if tooltip should appear to the left or right of the button
            tooltipX = (!selectedTower.onRightSide() ? hoveredButton.x + hoveredButton.width + 10 : hoveredButton.x - tooltipWidth - 10);

            String[] lines; // Lines of text to display in the tooltip

            // --- SHOW TOOLTIP CONTENT ---
            if (!selectedTower.isBuilt()) {
                // If the tower is NOT built yet — show tower build info
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

                // Special rules for certain towers
                if (buttonIndex == 4) {
                    lines[1] = "3 damage / second";
                    lines[3] = "Constant fire";
                }
                if (buttonIndex == 5) {
                    lines[3] = "20% firerate increase";
                }

            } else {
                // If the tower IS built — show upgrade / sell / ability info
                if (buttonIndex == 1) {
                    // --- UPGRADE TOWER ---
                    int nextLevel = selectedTower.getCurrentLevel() + 1;

                    if (nextLevel <= 3) {
                        // Choose upgrade values based on next level
                        int upgradeCost = (nextLevel == 2)
                                ? sVariables.getTowerCostUpgrade1(selectedTower.getTowerIndex())
                                : sVariables.getTowerCostUpgrade2(selectedTower.getTowerIndex());

                        float newDamage = (nextLevel == 2)
                                ? sVariables.getTowerDamageUpgrade1(selectedTower.getTowerIndex())
                                : sVariables.getTowerDamageUpgrade2(selectedTower.getTowerIndex());

                        int newRange = (nextLevel == 2)
                                ? sVariables.getTowerRangeUpgrade1(selectedTower.getTowerIndex())
                                : sVariables.getTowerRangeUpgrade2(selectedTower.getTowerIndex());

                        float newFireRate = (nextLevel == 2)
                                ? sVariables.getTowerFirerateUpgrade1(selectedTower.getTowerIndex())
                                : sVariables.getTowerFirerateUpgrade2(selectedTower.getTowerIndex());

                        lines = new String[] {
                                "Upgrade to Level " + nextLevel + " (" + upgradeCost + ")",
                                "Damage: " + selectedTower.getDamage() + " -> " + newDamage,
                                "Range: " + selectedTower.getRange() + " -> " + newRange,
                                String.format("Fire Rate: %.2f -> %.2f", selectedTower.getFireRate(), newFireRate)
                        };
                    } else {
                        lines = new String[] { "Max Level" };
                    }

                } else if (buttonIndex == 2) {
                    // --- ABILITY 2 ---
                    String abilityInfo = sVariables.getAbility2Info(selectedTower.getTowerIndex());
                    String abilityDescription = sVariables.getAbility2Description(selectedTower.getTowerIndex());
                    int abilityCost = sVariables.getAbility2Cost(selectedTower.getTowerIndex());

                    lines = new String[] {
                            abilityInfo,
                            abilityDescription,
                            "Cost: " + abilityCost
                    };

                    if (selectedTower.hasAbility2()) {
                        lines[2] = "Cost: ALREADY OWNED";
                    }

                } else if (buttonIndex == 3) {
                    // --- REFUND TOWER ---
                    int refundAmount = (int) (0.85 * selectedTower.getCost());
                    lines = new String[] {
                            "Refund Tower",
                            "You will get: " + refundAmount + " DNA"
                    };

                } else if (buttonIndex == 4) {
                    // --- ABILITY 1 ---
                    String abilityInfo = sVariables.getAbility1Info(selectedTower.getTowerIndex());
                    String abilityDescription = sVariables.getAbility1Description(selectedTower.getTowerIndex());
                    int abilityCost = sVariables.getAbility1Cost(selectedTower.getTowerIndex());

                    lines = new String[] {
                            abilityInfo,
                            abilityDescription,
                            "Cost: " + abilityCost
                    };

                    if (selectedTower.hasAbility1()) {
                        lines[2] = "Cost: ALREADY OWNED";
                    }

                } else {
                    // No valid option
                    lines = new String[] { "" };
                }
            }

            // Calculate tooltip height based on number of lines
            int tooltipHeight = 20 + lines.length * 18;

            // Draw tooltip
            Graphics2D g2 = (Graphics2D) g.create();

            // Background
            g2.setColor(new Color(30, 30, 60, 220));
            g2.fillRoundRect(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 16, 16);

            // First line (title)
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Bell MT", Font.BOLD, 17));
            g2.drawString(lines[0], tooltipX + 10, tooltipY + 25);

            // Other lines
            g2.setFont(new Font("Calibri", Font.PLAIN, 13));
            for (int i = 1; i < lines.length; i++) {
                if (i == lines.length - 1 && !selectedTower.isBuilt()) {
                    g2.setFont(new Font("Calibri", Font.PLAIN, 12));
                }
                g2.drawString(lines[i], tooltipX + 10, tooltipY + 25 + i * 16);
            }

            g2.dispose();
        }

    }

    // Called when a tower is clicked/selected
    private void towerSelected(Tower tower) {
        selectedTower = tower;
        panel.towerSelected();

        activeMenuButtons.clear();

        // Calculate the tower's center point (used for menu positioning)
        int centerX = tower.xLocation + tower.initialTower.getIconWidth() / 2;
        int centerY = tower.yLocation + tower.initialTower.getIconHeight() / 2;

        int radius; // Distance from tower center to button center
        int squareSize; // Size of each menu button

        if (!tower.isBuilt()) {
            // If tower is not built
            radius = 45;
            squareSize = 40;

            for (int i = 0; i < 5; i++) {
                double angle = Math.toRadians(-90 + i * 72); // Show 5 buttons

                int x = (int) (centerX + Math.cos(angle) * radius) - squareSize / 2;
                int y = (int) (centerY + Math.sin(angle) * radius) - squareSize / 2;

                final int buttonIndex = i + 1;

                // Add button with buildTower action
                activeMenuButtons.add(new MenuButton(x, y, squareSize, squareSize, () -> {
                    buildTower(buttonIndex);
                }));
            }
        } else {
            // If tower is built
            radius = 50;
            squareSize = 45;

            for (int i = 0; i < 4; i++) {
                double angle = Math.toRadians(-90 + i * 90); // Show 4 buttons

                int x = (int) (centerX + Math.cos(angle) * radius) - squareSize / 2;
                int y = (int) (centerY + Math.sin(angle) * radius) - squareSize / 2;

                final int buttonIndex = i + 1;

                // Add button with upgradeTower action
                activeMenuButtons.add(new MenuButton(x, y, squareSize, squareSize, () -> {
                    upgradeTower(buttonIndex);
                }));
            }
        }
    }

    // If tower is built
    private void buildTower(int buttonIndex) {
        if (selectedTower != null && !selectedTower.isBuilt()) {
            if (panel.getCurrentMoney() >= sVariables.getTowerCost(buttonIndex)) {
                // Set tower stats from static variables
                selectedTower.setTowerStats(
                        sVariables.getTowerDamage(buttonIndex),
                        sVariables.getTowerRange(buttonIndex),
                        sVariables.getTowerFirerate(buttonIndex),
                        sVariables.getTowerImage(buttonIndex),
                        sVariables.getTowerName(buttonIndex),
                        sVariables.getTowerCost(buttonIndex),
                        buttonIndex);

                selectedTower.built();
                panel.spendMoney(sVariables.getTowerCost(buttonIndex));
                panel.towerSelected();

                // Adrenaline Pump Tower
                if (buttonIndex == 5) {
                    for (Tower t : towers) {
                        if (selectedTower != t) {
                            for (Tower buffTower : t.buffTowers) {
                                if (buffTower == selectedTower) // Already buffed
                                    break;
                            }

                            float ex = t.xLocation - selectedTower.xLocation;
                            float ey = t.yLocation - selectedTower.yLocation;
                            float edist = (float) Math.sqrt(ex * ex + ey * ey);

                            if (edist <= selectedTower.getRange()) {
                                t.receiveBuff(selectedTower);
                            }
                        }
                    }
                }
            }
        }

        unselectTower();
    }

    // Handles upgrading, selling, and ability purchases
    private void upgradeTower(int buttonIndex) {
        int towerIndex = selectedTower.getTowerIndex();
        int towerLevel = selectedTower.getCurrentLevel();

        // Determine upgrade cost based on current tower level
        int upgradeTowerCost = (towerLevel == 1)
                ? sVariables.getTowerCostUpgrade1(towerIndex)
                : sVariables.getTowerCostUpgrade2(towerIndex);

        switch (buttonIndex) {
            case 1: // Upgrade to next level
                if (selectedTower != null && selectedTower.isBuilt() && towerLevel < 3) {
                    if (panel.getCurrentMoney() >= upgradeTowerCost) {
                        panel.spendMoney(upgradeTowerCost);

                        if (towerLevel == 1) {
                            selectedTower.upgradeTower(
                                    sVariables.getTowerDamageUpgrade1(towerIndex),
                                    sVariables.getTowerRangeUpgrade1(towerIndex),
                                    sVariables.getTowerFirerateUpgrade1(towerIndex));
                        } else if (towerLevel == 2) {
                            selectedTower.upgradeTower(
                                    sVariables.getTowerDamageUpgrade2(towerIndex),
                                    sVariables.getTowerRangeUpgrade2(towerIndex),
                                    sVariables.getTowerFirerateUpgrade2(towerIndex));
                        }

                        unselectTower();
                    }
                }
                break;

            case 2: // Purchase Ability 2
                if (panel.getCurrentMoney() >= sVariables.getAbility2Cost(towerIndex) && !selectedTower.hasAbility2()) {
                    selectedTower.getAbility2();
                    panel.spendMoney(sVariables.getAbility2Cost(towerIndex));
                    unselectTower();
                }
                break;

            case 3: // Sell/refund tower
                if (selectedTower != null && selectedTower.isBuilt()) {
                    if (towerIndex == 5) { // Remove buffs if Adrenaline Tower
                        for (Tower t : towers) {
                            for (Tower buffTower : t.buffTowers) {
                                if (selectedTower == buffTower) {
                                    t.removeBuff(selectedTower);
                                    break;
                                }
                            }
                        }
                    }

                    panel.gainMoney((int) (0.85 * selectedTower.getCost())); // Refund 85%
                    selectedTower.resetTower();
                    unselectTower();
                }
                break;

            case 4: // Purchase Ability 1
                if (panel.getCurrentMoney() >= sVariables.getAbility1Cost(towerIndex) && !selectedTower.hasAbility1()) {
                    selectedTower.getAbility1();
                    panel.spendMoney(sVariables.getAbility1Cost(towerIndex));
                    unselectTower();
                }
                break;
        }
    }

    // Creates a tower object at given location
    private void createTower(int xLocation, int yLocation) {
        towers.add(new Tower(this, xLocation, yLocation));
    }

    // Returns the currently selected tower
    public Tower getSelectedTower() {
        return selectedTower;
    }

    // MenuButton constructor for circular menu buttons
    public void MenuButton(int x, int y, int radius, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.onClick = onClick;
    }

    // Checks if a click is within the button's radius
    public boolean isClicked(int mx, int my) {
        return Math.hypot(mx - x, my - y) <= radius;
    }

    // Deselects current tower and clears menu buttons
    private void unselectTower() {
        selectedTower = null;
        activeMenuButtons.clear();
        panel.towerSelected();
    }

    // Returns whether a wave is currently spawning
    public boolean isSpawningWave() {
        return spawningWave;
    }

    // Stops all game timers
    public void stopUpdate() {
        timer.cancel();
        enemySpawnTimer.cancel();
    }

    // Returns total kills in the game
    public int getTotalKills() {
        return totalKills;
    }

    // Returns current wave number
    public int getCurrentWave() {
        return currentWaveNumber;
    }

    // Returns current difficulty level
    public float getDifficultyLevel() {
        return difficultyLevel;
    }

}
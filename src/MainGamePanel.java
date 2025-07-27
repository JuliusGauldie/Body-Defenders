/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 28/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class MainGamePanel extends JPanel implements MouseListener
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 500;

    // Images
    private static BufferedImage mapImage;
    private static BufferedImage buildTowerMenuImage;
    private ImageIcon timeControlMenu = new ImageIcon("../assets/timeControlMenu.png");

    // Try to assign images
    static {
        try {
            mapImage = ImageIO.read(new File("../assets/map.png"));
            buildTowerMenuImage = ImageIO.read(new File("../assets/BuildTowerMenu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Arrays
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Tower> towers = new ArrayList<>();
    ArrayList<Projectile> projectiles = new ArrayList<>();
    ArrayList<MenuButton> activeMenuButtons = new ArrayList<>();
    ArrayList<BloodSplatter> bloodSplatter = new ArrayList<>();

    // Point arrays
    ArrayList<Point> path = new ArrayList<>();
    ArrayList<Point> places = new ArrayList<>();

    // Towers
    private Tower selectedTower = null;

    private GamePanel panel;

    // JLabel
    JLabel infoLabel;

    // Menus
    int x, y, radius;
    Runnable onClick;

    TowerVariables tVariables = new TowerVariables();
    
    private TimeManager tManager;

    /**
     * Constructor for objects of class MainGamePanel
     */
    public MainGamePanel(GamePanel panel) 
    { 
        this.panel = panel;
        
        tManager = new TimeManager();

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        addMouseListener(this);

        
        //JLabel imageLabel = new JLabel();
        //imageLabel.setIcon(new ImageIcon(mapImage));
        //super.add(imageLabel);

        super.repaint();

        // Corners on map for enemies
        path.add(new Point(10, 260));
        path.add(new Point(120, 260));
        path.add(new Point(120, 110));
        path.add(new Point(280, 110));
        path.add(new Point(280, 315));
        path.add(new Point(495, 315));
        path.add(new Point(495, 210));
        path.add(new Point(800, 210));

        // Points for player to place towers
        createTower(100, 300);
        createTower(100, 48);
        createTower(250, 48);
        createTower(300, 350);
        createTower(500, 350);

        // Works as update function (runs 60 times a second - 60fps)
        java.util.Timer timer = new java.util.Timer();
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

                if (!p.isActive())
                    iter.remove();
            }

            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext())
            {
                Enemy e = enemyIter.next();
                e.update();

                if (!e.isAlive())
                {
                    bloodSplatter.add(new BloodSplatter(e.xLocation, e.yLocation));
                    
                    enemyIter.remove();  
                }
                else if (e.madeToEnd())
                {
                    panel.loseLife();

                    enemyIter.remove();
                }
            }

            repaint();
        }
    }

    public void newWave()
    {
        enemies.add(new Enemy(path));
        
        panel.gainMoney(25);

        repaint();
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
                if (x > t.xLocation && x < t.xLocation + (t.image.getIconWidth())) // If inside X of tower
                {
                    if (y > t.yLocation && y < t.yLocation + (t.image.getIconHeight())) // If inside Y of tower
                    {
                        towerSelected(t);

                        return;
                    }
                }
            }
        }

        selectedTower = null;
        activeMenuButtons.clear();
        panel.towerSelected();

        super.repaint();
    }

    public void mouseEntered(MouseEvent e) 
    {
        // Get current cursor coordinates
        int x = e.getX();
        int y = e.getY();
    }

    public void mousePressed (MouseEvent e) {}

    public void mouseReleased (MouseEvent e) {}

    public void mouseExited (MouseEvent e) {}

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
            g.fillOval((selectedTower.xLocation + selectedTower.image.getIconWidth() / 2) - (selectedTower.getRange()), (selectedTower.yLocation + selectedTower.image.getIconHeight() / 2) - (selectedTower.getRange()), selectedTower.getRange() * 2, selectedTower.getRange() * 2);

            // Tower Selection
            if (!selectedTower.isBuilt()) // If not selected yet
            {
                g.drawImage(
                    buildTowerMenuImage,
                    (selectedTower.xLocation + selectedTower.image.getIconWidth() / 2) - buildTowerMenuImage.getWidth() / 2,
                    (selectedTower.yLocation + selectedTower.image.getIconHeight() / 2) - buildTowerMenuImage.getHeight() / 2,
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
                e.image.paintIcon(this, g, e.xLocation, e.yLocation);
        }

        for (Tower t : towers)
        {
            if (!t.isBuilt())
                t.initialTower.paintIcon(this, g, t.xLocation, t.yLocation);
            else
                t.image.paintIcon(this, g, t.xLocation, t.yLocation);
        }

        g.setColor(Color.RED);
        for (MenuButton b : activeMenuButtons) 
        {
            if (!selectedTower.isBuilt())
                g.drawRect(b.x, b.y, b.width, b.height);
        }
        
        //timeControlMenu.paintIcon(this, g, 250, 0);
    }

    private void towerSelected(Tower tower)
    {
        selectedTower = tower;

        panel.towerSelected();

        activeMenuButtons.clear();
        int centerX = tower.xLocation + tower.image.getIconWidth() / 2;
        int centerY = tower.yLocation + tower.image.getIconHeight() / 2;

        int radius = 45;  // distance from tower center to square center
        int squareSize = 32; // size of each square button

        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(-90 + i * 72); // -90 to start at top, then every 72 degrees

            int x = (int)(centerX + Math.cos(angle) * radius) - squareSize / 2;
            int y = (int)(centerY + Math.sin(angle) * radius) - squareSize / 2;

            final int buttonIndex = i + 1;

            activeMenuButtons.add(new MenuButton(x, y, squareSize, squareSize, () -> {buildTower(buttonIndex);}));
        }
    }

    private void buildTower(int buttonIndex)
    {   
        if (buttonIndex > 2)
            return;
        
        if (selectedTower != null && !selectedTower.isBuilt())
        {
            if (panel.getCurrentMoney() >= tVariables.getTowerCost(buttonIndex))
            {
                selectedTower.setTowerStats(tVariables.getTowerDamage(buttonIndex), tVariables.getTowerRange(buttonIndex), tVariables.getTowerFirerate(buttonIndex), tVariables.getTowerName(buttonIndex));

                selectedTower.built();

                panel.spendMoney(tVariables.getTowerCost(buttonIndex));

                panel.towerSelected();
            }
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

    public void newGame()
    {
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
    }

    public void upgradeTower(int upgradeVariable) // 1 - Damage, 2 - Range, 3 - Firerate
    {
        if (selectedTower != null)
        {
            selectedTower.upgradeTower(upgradeVariable);
        }
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

}
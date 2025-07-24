
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 24/07/25
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
    public int CANVAS_WIDTH = 650; //Game Board widht/height
    public int CANVAS_HEIGHT = 500;

    // Images
    private static BufferedImage mapImage;
    private ImageIcon selectedTowerImage;
    private ImageIcon towerSelectionImage = new ImageIcon("../assets/TowerSelection.png");

    // Try to assign images
    static {
        try {
            mapImage = ImageIO.read(new File("../assets/map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Arrays
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Tower> towers = new ArrayList<>();
    ArrayList<Projectile> projectiles = new ArrayList<>();

    // Point arrays
    ArrayList<Point> path = new ArrayList<>();
    ArrayList<Point> places = new ArrayList<>();

    // Towers
    private Tower selectedTower = null;
    private int towerSelectedRange = 0;
    private int towerSelectedCost;

    // Boolean
    private boolean mouseInPanel = false;

    private int mouseX, mouseY;

    private GamePanel panel;
    
    // JLabel
    JLabel infoLabel;

    /**
     * Constructor for objects of class MainGamePanel
     */
    public MainGamePanel(GamePanel panel) 
    { 
        this.panel = panel;

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        addMouseListener(this);

        // Add map
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(mapImage));
        super.add(imageLabel);

        super.repaint();

        // Corners on map for enemies
        path.add(new Point(10, 235));
        path.add(new Point(90, 235));
        path.add(new Point(90, 100));
        path.add(new Point(225, 100));
        path.add(new Point(225, 280));
        path.add(new Point(400, 280));
        path.add(new Point(400, 190));
        path.add(new Point(650, 190));

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
                    enemyIter.remove();
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
        panel.newWave();
        
        enemies.add(new Enemy(path));

        repaint();
    }

    public void mouseClicked(MouseEvent e)
    {
        // Get current cursor coordinates
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) // If left click
        {
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
        panel.towerSelected();
        
        super.repaint();
    }

    public void mouseEntered(MouseEvent e)
    {
        this.requestFocus();

        mouseInPanel = true;
    }

    public void mousePressed (MouseEvent e) {}

    public void mouseReleased (MouseEvent e) {}

    public void mouseExited (MouseEvent e) {mouseInPanel = false;}

    public void paint (Graphics g)
    {  
        super.paint(g);

        if (selectedTower != null)
        {
            // Tower range
            g.setColor(new Color(160, 160, 160, 128));
            g.fillOval((selectedTower.xLocation + selectedTower.image.getIconWidth() / 2) - (selectedTower.getRange()), (selectedTower.yLocation + selectedTower.image.getIconHeight() / 2) - (selectedTower.getRange()), selectedTower.getRange() * 2, selectedTower.getRange() * 2);
            
            // Tower Selection
            if (!selectedTower.isBuilt()) // If not selected yet
            {
                towerSelectionImage.paintIcon(this, g, (selectedTower.xLocation - selectedTower.image.getIconWidth()), (selectedTower.yLocation - selectedTower.image.getIconHeight()));
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
    }

    private void towerSelected(Tower tower)
    {
        selectedTower = tower;
        
        panel.towerSelected();
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
        
        for (Enemy e : enemies)
        {
            e.hit(99999);
        }
    }
    
    public void upgradeTower(int upgradeVariable) // 1 - Damage, 2 - Range, 3 - Firerate
    {
        if (selectedTower != null)
        {
            selectedTower.upgradeTower(upgradeVariable);
        }
    }

}
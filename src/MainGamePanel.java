
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 27/06/25
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
    public int CANVAS_HEIGHT = 450;

    // Images
    private static BufferedImage mapImage;
    private ImageIcon selectedTowerImage;

    // Try to assign images
    static {
        try {
            mapImage = ImageIO.read(new File("../assets/map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Tower> towers = new ArrayList<>();
    ArrayList<Projectile> projectiles = new ArrayList<>();

    ArrayList<Point> path = new ArrayList<>();

    // Towers
    private boolean towerSelected = false;
    private int towerSelectedRange = 0;

    // Boolean
    private boolean mouseInPanel = false;

    private int mouseX, mouseY;

    /**
     * Constructor for objects of class MainGamePanel
     */
    public MainGamePanel() 
    { 
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        addMouseListener(this);

        // Add map
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(mapImage));
        super.add(imageLabel);

        super.repaint();

        // Corners on map 
        path.add(new Point(10, 235));
        path.add(new Point(90, 235));
        path.add(new Point(90, 100));
        path.add(new Point(225, 100));
        path.add(new Point(225, 280));
        path.add(new Point(400, 280));
        path.add(new Point(400, 190));
        path.add(new Point(650, 190));

        // Works ass update function (runs 60 times a second - 60fps)
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, 1000 / 60); // Update at 60fps

        // Add a MouseMotionListener to the frame
        addMouseMotionListener(new MouseMotionAdapter() 
            {
                @Override
                public void mouseMoved(MouseEvent e) 
                {
                    // Get current cursor coordinates
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            });
    }

    class UpdateTask extends TimerTask // Runs 60 times a second
    {
        public void run() {
            for (Tower t : towers)
            {
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
            }

            repaint();
        }
    }

    public void newWave()
    {
        enemies.add(new Enemy(path));

        repaint();
    }

    public void towerSelected(boolean selected, ImageIcon selectedImage, int range)
    {
        towerSelected = selected;
        selectedTowerImage = selectedImage;

        this.towerSelectedRange = range;
    }

    public void mouseClicked(MouseEvent e)
    {
        // Get current cursor coordinates
        int x = e.getX();
        int y = e.getY();

        if (towerSelected)
        {
            towers.add(new Tower(this, x - (selectedTowerImage.getIconWidth() / 2), y - (selectedTowerImage.getIconHeight() / 2)));

            towerSelected = false;
        }

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

        if (towerSelected && mouseInPanel)
        {
            g.setColor(new Color(160, 160, 160, 128));
            g.fillOval(mouseX - (towerSelectedRange), mouseY - (towerSelectedRange), towerSelectedRange * 2, towerSelectedRange * 2);

            selectedTowerImage.paintIcon(this, g, mouseX - (selectedTowerImage.getIconWidth() / 2), mouseY - (selectedTowerImage.getIconHeight() / 2));
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
            t.image.paintIcon(this, g, t.xLocation, t.yLocation);
        }
    }
}

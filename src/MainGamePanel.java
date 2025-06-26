
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 26/06/25
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
    private JLabel selectedLabel = new JLabel();

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
    
    // Boolean
    private boolean mouseInPanel = false;

    /**
     * Constructor for objects of class MainGamePanel
     */
    public MainGamePanel() 
    { 
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        addMouseListener(this);
            
        super.setLayout(new OverlayLayout(this));
        
        selectedLabel.setVisible(false);
        super.add(selectedLabel);

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(mapImage));
        super.add(imageLabel);

        super.repaint();

        path.add(new Point(10, 235));
        path.add(new Point(90, 235));
        path.add(new Point(90, 100));
        path.add(new Point(225, 100));
        path.add(new Point(225, 280));
        path.add(new Point(400, 280));
        path.add(new Point(400, 190));
        path.add(new Point(650, 190));

        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, 1000 / 60); // Update at 60fps

        // Add a MouseMotionListener to the frame
        addMouseMotionListener(new MouseMotionAdapter() 
        {
                @Override
                public void mouseMoved(MouseEvent e) 
                {
                    // Get current cursor coordinates
                    int x = e.getX();
                    int y = e.getY();

                    // Set the image label's location to the cursor's position
                    if (towerSelected && mouseInPanel)
                    {
                        selectedLabel.setVisible(true);
                        selectedLabel.setLocation(x, y);
                    }

                    if (x == 0 && y == 0)
                        selectedLabel.setLocation(100, 100);
                }
        });
    }

    class UpdateTask extends TimerTask // Runs every 5 second
    {
        public void run() {
            for (Tower t : towers)
            {
                t.update(enemies, projectiles);
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
            
            if (!towerSelected)
                selectedLabel.setVisible(false);

            repaint();
        }
    }

    public void newWave()
    {
        enemies.add(new Enemy(path));

        repaint();
    }

    public void towerSelected(boolean selected, ImageIcon selectedImage)
    {
        towerSelected = selected;

        selectedLabel.setIcon(selectedImage);
    }

    public void mouseClicked(MouseEvent e)
    {
        // Get current cursor coordinates
        int x = e.getX();
        int y = e.getY();

        if (towerSelected)
        {
            towers.add(new Tower(this, x, y));

            towerSelected = false;
            selectedLabel.setVisible(false);
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

        for (Enemy a : enemies)
        {
            a.image.paintIcon(this, g, a.xLocation, a.yLocation);
        }

        for (Tower t : towers)
        {
            t.image.paintIcon(this, g, t.xLocation, t.yLocation);
        }

        for (Projectile p : projectiles)
        {
            p.image.paintIcon(this, g, p.xLocation, p.yLocation);
        }
    }
}

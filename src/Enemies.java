
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 13/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class Enemies extends JFrame
{
    private int health = 100;
    private int damage = 0;
    private int speed = 2;

    public int xLocation;
    public int yLocation;

    private boolean alive = true;

    // Images
    ImageIcon image = new ImageIcon("../assets/enemy.png");

    /**
     * Constructor for objects of class Enemies
     */
    public Enemies(int xSpawn, int ySpawn)
    {
        this.xLocation = xSpawn;
        this.yLocation = ySpawn;
        
        repaint();
        super.repaint();
    }

    public void update()
    {
        xLocation += 2;
    }

    private BufferedImage offScreenImage;
    public void paint (Graphics g)
    {
        super.paint(g);
        
        image.paintIcon(this, g, xLocation, yLocation);
    }
}

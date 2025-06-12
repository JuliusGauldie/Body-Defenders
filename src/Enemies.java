
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

    private int xLocation;
    private int yLocation;

    private boolean alive = true;

    // Images
    ImageIcon image = new ImageIcon("../assets/robot.png");

    /**
     * Constructor for objects of class Enemies
     */
    public Enemies(int xSpawn, int ySpawn)
    {
        this.xLocation = xSpawn;
        this.yLocation = ySpawn;

        while (alive)
        {
            update();

            try {
                // to sleep 10 seconds
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }

    public void update()
    {
        xLocation += 2;
    }

    private BufferedImage offScreenImage;
    public void paint (Graphics g)
    {
        super.paint(g);

        if (offScreenImage == null)
            offScreenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = (Graphics2D) offScreenImage.getGraphics();
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        image.paintIcon(this, g2, xLocation, yLocation); 

        g.drawImage(offScreenImage, 0, 0, null);
    }
}


/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 19/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class MainGamePanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 650; //Game Board widht/height
    public int CANVAS_HEIGHT = 450;

    // Images
    private static BufferedImage mapImage;

    // Try to assign images
    static {
        try {
            mapImage = ImageIO.read(new File("../assets/map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Enemies> enemy = new ArrayList<>();
    java.util.List<Point> path = new ArrayList<>();

    /**
     * Constructor for objects of class MainGamePanel
     */
    public MainGamePanel() 
    { 
        setLayout(new FlowLayout());  

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(mapImage));
        imageLabel.setLayout(new BorderLayout());
        super.add(imageLabel);

        super.repaint();

        path.add(new Point(10, 235));
        path.add(new 
            Point(90, 235));
        path.add(new 
            Point(90, 100));
        path.add(new 
            Point(225, 100));
        path.add(new 
            Point(225, 280));
        path.add(new 
            Point(400, 280));
        path.add(new 
            Point(400, 190));
        path.add(new 
            Point(650, 190));

        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, 10);
    }

    class UpdateTask extends TimerTask 
    {
        public void run() {
            for (Enemies a : enemy)
            {
                a.update();
            }

            repaint();
        }
    }

    public void newWave()
    {
        enemy.add(new Enemies(path));

        repaint();
    }

    public void paint (Graphics g)
    {
        super.paint(g);

        for (Enemies a : enemy)
        {
            a.image.paintIcon(this, g, a.xLocation, a.yLocation);
        }
    }
}

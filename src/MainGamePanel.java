
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 16/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.ArrayList;

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

    GameManager gManager;

    ArrayList<Enemies> enemy = new ArrayList<>();

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
        
        enemy.add(new Enemies(10, 235));
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


/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 09/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class InfoPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 1920; //Game Board widht/height
    public int CANVAS_HEIGHT = 1080;
    

    /**
     * Constructor for objects of class InfoPanel
     */
    public InfoPanel() 
    {
        super(new CardLayout()); // Use CardLayout for the panel
        
        // Create game panel
        this.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}

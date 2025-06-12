
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 12/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DetailPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 150;

    /**
     * Constructor for objects of class DetailPanel
     */
    public DetailPanel() 
    {
        setLayout(new FlowLayout());  

        JLabel label = new JLabel("This is the detail panel");
        add(label);
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }
}

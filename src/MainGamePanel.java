
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 12/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MainGamePanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 650; //Game Board widht/height
    public int CANVAS_HEIGHT = 650;

    /**
     * Constructor for objects of class MainGamePanel
     */
    public MainGamePanel() 
    {
        setLayout(new FlowLayout());  
        
        JLabel label = new JLabel("This is the main Game");
        add(label);
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }
}


/**
 * Write a description of class TimeManager here.
 *
 * @author Julius Gauldie
 * @version 03/08/25
 */
import javax.swing.*;

public class TimeManager extends JPanel
{
    // Buttons
    JButton pauseButton;
    JButton normalSpeedButton;
    JButton fastSpeedButton;

    // Size
    public int CANVAS_WIDTH = 300; //Game Board widht/height
    public int CANVAS_HEIGHT = 50;

    /**
     * Constructor for objects of class TimeManager
     */
    public TimeManager()
    {
        // Wave Button
        pauseButton = new JButton("New Wave");
        pauseButton.addActionListener(e -> pause());
        add(pauseButton); 
        
        normalSpeedButton = new JButton("Normal Speed");
        normalSpeedButton.addActionListener(e -> normalSpeed());
        add(normalSpeedButton);
        
        fastSpeedButton = new JButton("Fast Speed");
        fastSpeedButton.addActionListener(e -> fastSpeed());
        add(fastSpeedButton);
    }
    
    public void pause()
    {

    }

    public void normalSpeed()
    {

    }

    public void fastSpeed()
    {

    }
}
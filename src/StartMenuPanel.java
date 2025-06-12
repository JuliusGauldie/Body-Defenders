
/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 12/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class StartMenuPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    /**
     * Constructor for objects of class StartMenuPanel
     */
    public StartMenuPanel(PanelManager manager) 
    {
        setLayout(new FlowLayout());
        
        JButton startButton = new JButton("Start Game");
        add(startButton);
        startButton.addActionListener(e -> manager.showMainPanel());
        
        JButton exitButton = new JButton("Quit Game");
        add(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        
        super.revalidate();
        super.repaint();
    }
}

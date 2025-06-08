
/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 09/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class StartMenuPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 1920 / 2; //Game Board widht/height
    public int CANVAS_HEIGHT = 1080 / 2;

    /**
     * Constructor for objects of class StartMenuPanel
     */
    public StartMenuPanel(PanelManager manager) 
    {
        setLayout(null);
        JButton startButton = new JButton("Start Game");

        startButton.setBounds(300, 250, 200, 50);
        add(startButton);

        startButton.addActionListener(e -> manager.showGamePanel());
    }
}

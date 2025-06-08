
/**
 * Panel showing pause menu in game
 *
 * @author Julius Gauldie
 * @version 09/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class PauseMenuPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 1920; //Game Board widht/height
    public int CANVAS_HEIGHT = 1080;

    /**
     * Constructor for objects of class PauseMenuPanel
     */
    public PauseMenuPanel(PanelManager manager) 
    {
        setLayout(null);
        JButton startButton = new JButton("Back to menu");

        startButton.setBounds(300, 250, 200, 50);
        add(startButton);

        startButton.addActionListener(e -> manager.showStartMenu());
    }
}

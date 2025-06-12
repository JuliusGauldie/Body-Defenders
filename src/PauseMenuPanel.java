
/**
 * Panel showing pause menu in game
 *
 * @author Julius Gauldie
 * @version 12/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class PauseMenuPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;
    
    MainPanel main;

    /**
     * Constructor for objects of class PauseMenuPanel
     */
    public PauseMenuPanel(MainPanel main) 
    {
        this.main = main;
        
        setLayout(new FlowLayout());
        
        JButton menuButton = new JButton("Back to menu");
        menuButton.setBounds(300, 250, 200, 50);
        add(menuButton);
        menuButton.addActionListener(e -> main.showStartMenu());

        JButton unpauseButton = new JButton("Back to game");
        unpauseButton.setBounds(100, 350, 200, 50);
        add(unpauseButton);
        unpauseButton.addActionListener(e -> this.setVisible(false));
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        
        super.revalidate();
        super.repaint();
    }
}

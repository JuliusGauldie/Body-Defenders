
/**
 * Panel showing pause menu in game
 *
 * @author Julius Gauldie
 * @version 03/08/25
 */
import java.awt.*;
import javax.swing.*;
public class GameOverPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;
    
    MainPanel main;

    /**
     * Constructor for objects of class PauseMenuPanel
     */
    public GameOverPanel(MainPanel main) 
    {
        this.main = main;
        
        setLayout(new FlowLayout());
        
        JButton menuButton = new JButton("Back to menu");
        menuButton.setBounds(300, 250, 200, 50);
        add(menuButton);
        menuButton.addActionListener(e -> main.showStartMenu());

        JButton unpauseButton = new JButton("New Game");
        unpauseButton.setBounds(100, 350, 200, 50);
        add(unpauseButton);
        unpauseButton.addActionListener(e -> main.newGame());
        
        super.setPreferredSize(new Dimension(650, 450));
        
        super.revalidate();
        super.repaint();
    }
}

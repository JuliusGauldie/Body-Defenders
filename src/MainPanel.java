
/**
 * Panel showing main game and game manager
 *
 * @author Julius Gauldie
 * @version 18/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MainPanel extends JPanel 
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu
    private JLayeredPane layeredPane;
    GameOverPanel gameOverPanel;
    GamePanel gamePanel;

    // Manager
    PanelManager manager;

    /**
     * Constructor for objects of class MainPanel
     */
    public MainPanel(PanelManager manager) 
    {
        this.manager = manager;

        this.setLayout(new CardLayout());

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Create Game Panel
        gamePanel = new GamePanel(this);
        gamePanel.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        // Create pause menu pane
        gameOverPanel = new GameOverPanel(this);
        gameOverPanel.setBounds(200, 100, 400, 300); // Centered
        gameOverPanel.setOpaque(true);
        gameOverPanel.setVisible(false); // Initially hidden
        layeredPane.add(gameOverPanel, JLayeredPane.PALETTE_LAYER); // On top

        super.add(layeredPane, "Main Panel");

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    public void showGameOver()
    {
        gameOverPanel.setVisible(true);

        super.revalidate();
        super.repaint();
    }

    public void hideGameOver()
    {
        gameOverPanel.setVisible(false);

        super.revalidate();
        super.repaint();
    }

    public void showStartMenu()
    {
        gameOverPanel.setVisible(false);
        manager.showStartMenu();
    }
    
    public void newGame()
    {
        gameOverPanel.setVisible(false);
        
        gamePanel.newGame();
    }

}


/**
 * Panel showing main game and game manager
 *
 * @author Julius Gauldie
 * @version 11/08/25
 */
import java.awt.*;
import javax.swing.*;
public class GameScreenPanel extends JPanel 
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu
    private JLayeredPane layeredPane;
    GameOverPanel gameOverPanel;
    GameLayerPanel gamePanel;

    // Manager
    PanelManager manager;

    private int currentLevelIndex = 0;

    /**
     * Constructor for objects of class MainPanel
     */
    public GameScreenPanel(PanelManager manager) 
    {
        this.manager = manager;

        this.setLayout(new CardLayout());

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Create Game Panel
        gamePanel = new GameLayerPanel(this);
        gamePanel.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        // Create game over panel
        gameOverPanel = new GameOverPanel(this);
        gameOverPanel.setBounds(40, 10, 700, 500); // Centered
        gameOverPanel.setOpaque(true);
        gameOverPanel.setVisible(false); // Initially hidden
        layeredPane.add(gameOverPanel, JLayeredPane.PALETTE_LAYER); // On top

        super.add(layeredPane, "Main Panel");

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    public void showGameOver(boolean lostGame, int score)
    {
        gameOverPanel.updateLabel(lostGame, score);

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
    
    public void newGame(int levelIndex)
    {
        gameOverPanel.setVisible(false);
        
        currentLevelIndex = levelIndex;

        gamePanel.newGame(currentLevelIndex);
    }

    public int getCurrentLevelIndex()
    {
        return currentLevelIndex;
    }
}

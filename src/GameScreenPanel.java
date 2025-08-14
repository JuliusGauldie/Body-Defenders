
/**
 * Panel showing the main game and acting as the game manager
 * Handles layering between gameplay and game-over screen
 *
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.awt.*;
import javax.swing.*;

public class GameScreenPanel extends JPanel 
{
    // Panel size
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;

    // Panels
    private JLayeredPane layeredPane;
    private GameOverPanel gameOverPanel;
    private GameLayerPanel gamePanel;
    private PanelManager manager;

    private int currentLevelIndex = 0; // Current level being played

    private float currentDifficultyIndex = 1; // Current difficulty multiplier

    /**
     * Constructor — sets up the main gameplay and overlay panels
     * 
     * @param manager The main panel manager
     */
    public GameScreenPanel(PanelManager manager) {
        this.manager = manager;

        this.setLayout(new CardLayout());

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Create main game panel
        gamePanel = new GameLayerPanel(this);
        gamePanel.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        // Create game over panel
        gameOverPanel = new GameOverPanel(this);
        gameOverPanel.setBounds(40, 10, 700, 500); // Slightly inset from edges
        gameOverPanel.setOpaque(true);
        gameOverPanel.setVisible(false);
        layeredPane.add(gameOverPanel, JLayeredPane.PALETTE_LAYER);

        super.add(layeredPane, "Main Panel");

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    /**
     * Displays the game over screen with the player's results
     */
    public void showGameOver(boolean lostGame, int score, int totalKills, int wave, float healthLeft, int dnaLeft,
            float difficultyLevel) {
        gameOverPanel.updateLabel(lostGame, score, totalKills, wave, healthLeft, dnaLeft, difficultyLevel);
        gameOverPanel.setVisible(true);
        super.revalidate();
        super.repaint();
    }

    /**
     * Hides the game over screen
     */
    public void hideGameOver() {
        gameOverPanel.setVisible(false);
        super.revalidate();
        super.repaint();
    }

    /**
     * Switches to the start menu
     */
    public void showStartMenu() {
        gameOverPanel.setVisible(false);
        manager.showStartMenu();
    }

    /**
     * Starts a new game at the given level & difficulty
     */
    public void newGame(int levelIndex, float difficultyIndex) {
        gameOverPanel.setVisible(false);
        currentLevelIndex = levelIndex;
        currentDifficultyIndex = difficultyIndex;
        gamePanel.newGame(currentLevelIndex, difficultyIndex);
    }

    // Return the current level index
    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    /// Return the current difficulty multiplier
    public float getCurrentDifficultyIndex() {
        return currentDifficultyIndex;
    }
}

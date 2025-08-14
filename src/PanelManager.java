
/**
 * Manages which panel the player sees in the JFrame.
 * 
 * Author: Julius Gauldie
 * Version: 14/08/25
 */
import javax.swing.*;

public class PanelManager 
{
    // Instance variables
    private JFrame frame; // Main game window
    private MainMenuPanel mainMenuPanel; // Start menu panel
    private LevelSelectionPanel levelSelectionPanel; // Level/difficulty selection panel
    private GameScreenPanel gamePanel; // Main game panel

    /**
     * Constructor for PanelManager.
     * Initializes all panels.
     * 
     * @param frame The JFrame this manager controls.
     */
    public PanelManager(JFrame frame) {
        this.frame = frame;
        mainMenuPanel = new MainMenuPanel(this);
        levelSelectionPanel = new LevelSelectionPanel(this);
        gamePanel = new GameScreenPanel(this);
    }

    /**
     * Returns the start menu panel.
     * 
     * @return MainMenuPanel instance.
     */
    public MainMenuPanel getStartMenu() {
        return mainMenuPanel;
    }

    /**
     * Shows the start menu panel in the JFrame.
     */
    public void showStartMenu() {
        frame.setContentPane(mainMenuPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Shows the level selection panel and updates its high score.
     */
    public void showDifficultySelection() {
        levelSelectionPanel.updateHighScore();
        frame.setContentPane(levelSelectionPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Starts a new game on the game panel.
     * 
     * @param gameSettings String "level,difficulty".
     */
    public void startGame(String gameSettings) {
        frame.setContentPane(gamePanel);

        // Parse level and difficulty
        String[] values = gameSettings.split(","); // values[0] = Level Index, values[1] = Difficulty Index
        int levelIndex = Integer.parseInt(values[0]);
        float difficultyIndex = Float.parseFloat(values[1]);

        gamePanel.newGame(levelIndex, difficultyIndex);

        frame.revalidate();
        frame.repaint();
    }
}

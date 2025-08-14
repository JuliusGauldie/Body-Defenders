
/**
 * Panel that displays the game over screen with score, breakdown, and highscore.
 * Also allows the player to play again or return to the main menu.
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel 
{
    // Panel size
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 500;

    // Labels
    private JLabel outcomeLabel; // Displays "YOU WIN" or "YOU LOSE"
    private JLabel scoreLabel; // Displays total score
    private JLabel scoreBreakdownLabel; // Displays breakdown of score components
    private JLabel highscoreLabel; // Displays highscore

    // Highscore manager
    private HighScoreManager scoreManager = new HighScoreManager();

    // Reference to main game screen
    private GameScreenPanel main;

    /**
     * Constructor for GameOverPanel
     * 
     * @param main Reference to the main game screen panel
     */
    public GameOverPanel(GameScreenPanel main) {
        this.main = main;

        // Set panel size and layout
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setLayout(null);

        // Outcome label
        outcomeLabel = new JLabel("", SwingConstants.CENTER);
        outcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 64));
        outcomeLabel.setBounds(0, 100, CANVAS_WIDTH, 80);
        outcomeLabel.setForeground(Color.WHITE);
        add(outcomeLabel);

        // Total score label
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setBounds(0, 220, CANVAS_WIDTH, 40);
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        add(scoreLabel);

        // Score breakdown label
        scoreBreakdownLabel = new JLabel("", SwingConstants.CENTER);
        scoreBreakdownLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreBreakdownLabel.setBounds(0, 260, CANVAS_WIDTH, 40);
        scoreBreakdownLabel.setForeground(Color.GRAY);
        add(scoreBreakdownLabel);

        // Highscore label
        highscoreLabel = new JLabel("", SwingConstants.CENTER);
        highscoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        highscoreLabel.setBounds(0, 300, CANVAS_WIDTH, 40);
        highscoreLabel.setForeground(Color.BLACK);
        add(highscoreLabel);

        // "Play Again" button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 20));
        playAgainButton.setBounds(CANVAS_WIDTH / 2 - 110, 350, 220, 50);
        playAgainButton
                .addActionListener(e -> main.newGame(main.getCurrentLevelIndex(), main.getCurrentDifficultyIndex()));
        add(playAgainButton);

        // "Back to Menu" button
        JButton menuButton = new JButton("Back to Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 20));
        menuButton.setBounds(CANVAS_WIDTH / 2 - 110, 420, 220, 50);
        menuButton.addActionListener(e -> main.showStartMenu());
        add(menuButton);
    }

    /**
     * Updates all labels with the latest game results
     * 
     * @param lostGame        true if player lost
     * @param score           Total score
     * @param totalKills      Number of kills
     * @param waves           Number of waves completed
     * @param healthLeft      Player health left
     * @param dnaLeft         Player money/DNA left
     * @param difficultyLevel Difficulty multiplier
     */
    public void updateLabel(boolean lostGame, int score, int totalKills, int waves, float healthLeft, int dnaLeft,
            float difficultyLevel) {
        // Outcome label
        if (lostGame) {
            outcomeLabel.setText("YOU LOSE");
            outcomeLabel.setForeground(Color.RED);
        } else {
            outcomeLabel.setText("YOU WIN");
            outcomeLabel.setForeground(new Color(255, 215, 0)); // Gold color
        }

        // Total score label
        scoreLabel.setText("Score: " + score);

        // Ensure healthLeft is not negative
        if (healthLeft < 0)
            healthLeft = 0;

        // Score breakdown
        String breakdownText = String.format(
                "Kills: %d -- Waves: %d -- Health Bonus: %.0f -- Money Bonus: %d -- Difficulty Multiplier: %.1f",
                totalKills, waves, healthLeft, dnaLeft, difficultyLevel);

        scoreBreakdownLabel.setText(breakdownText);

        // Save highscore if current score is higher
        if (score > getHighScore() || getHighScore() == Integer.MAX_VALUE) {
            scoreManager.setScore(main.getCurrentLevelIndex(), score);
            scoreManager.saveScores();
        }

        // Update highscore label
        if (getHighScore() != Integer.MAX_VALUE)
            highscoreLabel.setText("Highscore: " + getHighScore());
        else
            highscoreLabel.setText("Highscore: -");
    }

    // Returns the current highscore for the level
    private int getHighScore() {
        return scoreManager.getScore(main.getCurrentLevelIndex());
    }
}

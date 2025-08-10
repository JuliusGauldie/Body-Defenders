import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel 
{
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 500;

    private JLabel outcomeLabel;
    private JLabel scoreLabel;
    private JLabel highscoreLabel;


    public GameOverPanel(GameScreenPanel main) 
    {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setLayout(null); 

        outcomeLabel = new JLabel("", SwingConstants.CENTER);
        outcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 64));
        outcomeLabel.setBounds(0, 100, CANVAS_WIDTH, 80);
        outcomeLabel.setForeground(Color.WHITE);
        add(outcomeLabel);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setBounds(0, 220, CANVAS_WIDTH, 40);
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        add(scoreLabel);

        highscoreLabel = new JLabel("", SwingConstants.CENTER);
        highscoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        highscoreLabel.setBounds(0, 260, CANVAS_WIDTH, 40);
        highscoreLabel.setForeground(Color.GRAY);
        add(highscoreLabel);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 20));
        playAgainButton.setBounds(CANVAS_WIDTH/2 - 110, 350, 220, 50);
        playAgainButton.addActionListener(e -> main.newGame(main.getCurrentLevelIndex()));
        add(playAgainButton);

        JButton menuButton = new JButton("Back to Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 20));
        menuButton.setBounds(CANVAS_WIDTH/2 - 110, 420, 220, 50);
        menuButton.addActionListener(e -> main.showStartMenu());
        add(menuButton);
    }

    public void updateLabel(boolean lostGame, int score) 
    {
        if (lostGame) 
        {
            outcomeLabel.setText("YOU LOSE");
            outcomeLabel.setForeground(Color.RED);
        } 
        else 
        {
            outcomeLabel.setText("YOU WIN");
            outcomeLabel.setForeground(new Color(255, 215, 0)); // Gold
        }

        scoreLabel.setText("Score: " + score);
        highscoreLabel.setText("Highscore: " + getHighScore()); 
    }

    private int getHighScore() 
    {
        return 1;
    }
}

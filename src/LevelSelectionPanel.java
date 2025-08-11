
/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 11/08/25
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LevelSelectionPanel extends JPanel 
{
    private HighScoreManager scoreManager = new HighScoreManager();

    private JLabel[] highScoreLabel = new JLabel[3];

    public LevelSelectionPanel(PanelManager manager) 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(180, 235, 220)); // Same as main menu
        setBorder(new EmptyBorder(40, 40, 40, 40)); // Same padding as main menu

        // Title
        add(Box.createVerticalStrut(40));
        JLabel title = new JLabel("Select Level and Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(20, 60, 50)); // Same title color
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(30));

        // Map data
        String[] maps = {"Level 1: Heart", "Level 2: Brain", "Level 3: Stomach"};
        String[] difficulties = {"Easy", "Medium", "Hard"};

        // Map panels
        for (int i = 0; i < maps.length; i++) 
        {
            JPanel mapPanel = new JPanel(new GridBagLayout());
            mapPanel.setOpaque(false); // Transparent so background shows

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.gridy = 0;

            // Map name label
            JLabel mapLabel = new JLabel(maps[i]);
            mapLabel.setFont(new Font("Arial", Font.BOLD, 20));
            mapLabel.setForeground(new Color(50, 90, 80));
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            mapPanel.add(mapLabel, gbc);

            // Difficulty buttons
            for (int j = 0; j < difficulties.length; j++) 
            {
                JButton btn = new JButton(difficulties[j]);
                styleButton(btn);
                btn.setActionCommand((i + 1) + "," + (j + 1)); // Level number, Difficulty number

                btn.addActionListener(e -> 
                {
                    String choice = ((JButton) e.getSource()).getActionCommand();
                    manager.startGame(choice);
                });

                gbc.gridx = j + 1;
                gbc.anchor = GridBagConstraints.CENTER;
                mapPanel.add(btn, gbc);
            }

            // High score label
            highScoreLabel[i] = new JLabel("Highscore: " + (scoreManager.getScore(i + 1) == Integer.MAX_VALUE ? "-" : scoreManager.getScore(i + 1)));
            highScoreLabel[i].setFont(new Font("Arial", Font.PLAIN, 16));
            highScoreLabel[i].setForeground(new Color(80, 80, 80));
            gbc.gridx = difficulties.length + 1;
            gbc.anchor = GridBagConstraints.EAST;
            mapPanel.add(highScoreLabel[i], gbc);

            // Center alignment for the mapPanel itself
            mapPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(mapPanel);
            add(Box.createVerticalStrut(15));
        }

        add(Box.createVerticalGlue());

        // Return button at bottom
        JButton backButton = new JButton("Return");
        styleButton(backButton);
        backButton.addActionListener(e -> manager.showStartMenu());
        add(backButton);

        add(Box.createVerticalStrut(30));
    }

    // Matches main menu button style
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(100, 200, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(140, 40));
        button.setMaximumSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public void updateHighScore()
    {
        scoreManager.loadScores();

        for (int i = 0; i < 3; i++)
        {
            highScoreLabel[i].setText("Highscore: " + (scoreManager.getScore(i + 1) == Integer.MAX_VALUE ? "-" : scoreManager.getScore(i + 1)));
        }
    }
}


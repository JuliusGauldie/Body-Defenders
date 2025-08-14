
/**
 * Main menu panel displayed at startup.
 * 
 * Shows the game title, credits, and options to 
 * start the game or quit.
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainMenuPanel extends JPanel 
{
    // Constants
    private static final int CANVAS_WIDTH = 800; // Width of the panel
    private static final int CANVAS_HEIGHT = 600; // Height of the panel

    /**
     * Constructor - sets up the main menu 
     * 
     * @param manager The PanelManager to switch between screens.
     */
    public MainMenuPanel(PanelManager manager) {
        // Use vertical layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set background color 
        setBackground(new Color(180, 235, 220));

        // Add padding around the panel
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // Title
        JLabel title = new JLabel("Body Defenders");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(20, 60, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(50)); // Space above
        add(title);

        // Credits
        JLabel credits = new JLabel("Made by Julius Gauldie");
        credits.setFont(new Font("Arial", Font.ITALIC, 18));
        credits.setForeground(new Color(50, 90, 80));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10)); // Small gap
        add(credits);

        add(Box.createVerticalStrut(60)); // Space before buttons

        // Start button
        JButton startButton = new JButton("Start Game");
        styleButton(startButton);
        startButton.addActionListener(e -> manager.showDifficultySelection());
        add(startButton);

        add(Box.createVerticalStrut(20)); // Gap between buttons

        // Quit button
        JButton exitButton = new JButton("Quit Game");
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        add(Box.createVerticalGlue());
    }

    /**
     * Styles a JButton with consistent formatting.
     * 
     * @param button The button to style.
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(new Color(100, 200, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(300, 60));
        button.setMaximumSize(new Dimension(300, 60));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}

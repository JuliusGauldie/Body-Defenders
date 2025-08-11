
/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 11/08/25
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainMenuPanel extends JPanel {

    public int CANVAS_WIDTH = 800;
    public int CANVAS_HEIGHT = 600;

    public MainMenuPanel(PanelManager manager) 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Background color (light medical green-blue)
        setBackground(new Color(180, 235, 220));

        // Add padding around everything
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // Title
        JLabel title = new JLabel("Body Defenders");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(20, 60, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(50));
        add(title);

        // Credits
        JLabel credits = new JLabel("Made by Julius Gauldie");
        credits.setFont(new Font("Arial", Font.ITALIC, 18));
        credits.setForeground(new Color(50, 90, 80));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(credits);

        add(Box.createVerticalStrut(60));

        // Start button
        JButton startButton = new JButton("Start Game");
        styleButton(startButton);
        startButton.addActionListener(e -> manager.showDifficultySelection());
        add(startButton);

        add(Box.createVerticalStrut(20));

        // Quit button
        JButton exitButton = new JButton("Quit Game");
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        add(Box.createVerticalGlue());
    }

    private void styleButton(JButton button) 
    {
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

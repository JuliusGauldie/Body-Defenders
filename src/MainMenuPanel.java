
/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 07/08/25
 */
import java.awt.*;
import javax.swing.*;
public class MainMenuPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    /**
     * Constructor for objects of class StartMenuPanel
     */
    public MainMenuPanel(PanelManager manager) 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Body Defenders");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(40));
        add(title);

        JLabel credits = new JLabel("Made by Julius Gauldie");
        credits.setFont(new Font("Arial", Font.ITALIC, 16));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(credits);

        add(Box.createVerticalStrut(40));

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(startButton);
        startButton.addActionListener(e -> manager.showDifficultySelection());

        add(Box.createVerticalStrut(10));

        JButton exitButton = new JButton("Quit Game");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(exitButton);
        exitButton.addActionListener(e -> System.exit(0));

        add(Box.createVerticalGlue());

        super.revalidate();
        super.repaint();
    }
}

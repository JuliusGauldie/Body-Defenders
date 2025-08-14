
/**
 * Main class to launch the Body Defenders game.
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import javax.swing.*;

public class Main extends JFrame 
{
    // Constants
    private static final int CANVAS_WIDTH = 800; // Window width in pixels
    private static final int CANVAS_HEIGHT = 600; // Window height in pixels

    // Panels
    private PanelManager manager; // Handles switching between panels

    /**
     * Constructor - sets up the main game window.
     */
    public Main() {
        setTitle("Body Defenders"); // Game title
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close on exit
        setResizable(false); // Disable resizing
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT); // Set window size

        // Create PanelManager
        manager = new PanelManager(this);

        // Set start menu as the first panel displayed
        setContentPane(manager.getStartMenu());

        // Make the window visible
        setVisible(true);
    }

    /**
     * Main method (For VS Code)
     */
    public static void main(String[] args) {
        new Main();
    }
}


/**
 * Panel showing main game and game manager
 *
 * @author Julius Gauldie
 * @version 13/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MainPanel extends JPanel 
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu
    private JLayeredPane layeredPane;
    PauseMenuPanel pausePanel;
    GamePanel gamePanel;

    // Manager
    PanelManager manager;

    private int enemyXSpawn = 50;
    private int enemyYSpawn = 50;

    Enemies enemy;

    /**
     * Constructor for objects of class MainPanel
     */
    public MainPanel(PanelManager manager) 
    {
        this.manager = manager;

        this.setLayout(new CardLayout());

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Create Game Panel
        gamePanel = new GamePanel(this);
        gamePanel.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        // Create pause menu pane
        pausePanel = new PauseMenuPanel(this);
        pausePanel.setBounds(200, 100, 400, 300); // Centered
        pausePanel.setOpaque(true);
        pausePanel.setVisible(false); // Initially hidden
        layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER); // On top

        super.add(layeredPane, "Main Panel");

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    public void showPauseMenu()
    {
        pausePanel.setVisible(true);

        super.revalidate();
        super.repaint();
    }

    public void hidePauseMenu()
    {
        pausePanel.setVisible(false);

        super.revalidate();
        super.repaint();
    }

    public void showStartMenu()
    {
        pausePanel.setVisible(false);
        manager.showStartMenu();
    }

    public void newWave()
    {
        for (int i = 0; i < 10; i++)
        {
            new Enemies(enemyXSpawn, enemyYSpawn);
        }
    }
}

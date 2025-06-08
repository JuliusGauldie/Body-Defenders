
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 09/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MainBoardPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 1920; //Game Board widht/height
    public int CANVAS_HEIGHT = 1080;
    
    // Pause Menu
    private JLayeredPane layeredPane;
    PauseMenuPanel pausePanel;

    /**
     * Constructor for objects of class MainBoardPanel
     */
    public MainBoardPanel(PanelManager manager) 
    {
        setLayout(new BorderLayout());

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(960, 540));
        add(layeredPane, BorderLayout.CENTER);
        
        // Create pause menu pane
        pausePanel = new PauseMenuPanel(manager);
        pausePanel.setBounds(240, 135, 480, 270); // Centered
        pausePanel.setOpaque(true);
        pausePanel.setVisible(false); // Initially hidden
        layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER); // On top
        
        setLayout(null);
        JButton startButton = new JButton("Pause");

        startButton.setBounds(300, 250, 200, 50);
        add(startButton);

        startButton.addActionListener(e -> pausePanel.setVisible(true));
    }
}

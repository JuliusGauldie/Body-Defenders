
/**
 * Class to start up game
 *
 * @author Julius Gauldie
 * @version 09/06/25
 */
import java.awt.*;
import javax.swing.*;

public class Main extends JFrame
{
    //JPanels
    PanelManager manager = new PanelManager(this);
    
    // CONSTANTS
    private int CANVAS_WIDTH = 800;
    private int CANVAS_HEIGHT = 600;

    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        setTitle("Tower Defense Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        
        setContentPane(manager.getStartMenu());

        setVisible(true);
    }
}

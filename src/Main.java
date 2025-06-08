
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

    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        setTitle("Tower Defense Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        
        setContentPane(manager.getStartMenu());

        setVisible(true);
    }
}

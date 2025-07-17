
/**
 * Manages panel player sees
 *
 * @author Julius Gauldie
 * @version 18/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class PanelManager
{
    public JFrame frame;
    private StartMenuPanel startPanel;
    private MainPanel mainPanel;
    
    /**
     * Constructor for objects of class PanelManager
     */
    public PanelManager(JFrame frame) 
    {
        this.frame = frame;
        startPanel = new StartMenuPanel(this);
        mainPanel = new MainPanel(this);
    }
    
    public StartMenuPanel getStartMenu()
    {
        return this.startPanel;
    }
    
    public void showStartMenu()
    {
        frame.setContentPane(startPanel);

        frame.revalidate();
    }
    
    public void showMainPanel()
    {
        frame.setContentPane(mainPanel);
        
        mainPanel.newGame();
        
        frame.revalidate();
    }
}

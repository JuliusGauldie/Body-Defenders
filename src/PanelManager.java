
/**
 * Manages panel player sees
 *
 * @author Julius Gauldie
 * @version 09/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class PanelManager
{
    private JFrame frame;
    private StartMenuPanel startPanel;
    private PauseMenuPanel pausePanel;
    private MainBoardPanel gamePanel;
    
    /**
     * Constructor for objects of class PanelManager
     */
    public PanelManager(JFrame frame) 
    {
        this.frame = frame;
        startPanel = new StartMenuPanel(this);
        gamePanel = new MainBoardPanel(this);
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
    
    public void showGamePanel()
    {
        frame.setContentPane(gamePanel);
        frame.revalidate();
    }
}

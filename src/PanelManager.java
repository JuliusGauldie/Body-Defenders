
/**
 * Manages panel player sees
 *
 * @author Julius Gauldie
 * @version 11/08/25
 */
import javax.swing.*;
public class PanelManager
{
    public JFrame frame;
    private MainMenuPanel mainMenuPanel;
    private LevelSelectionPanel levelSelectionPanel;
    private GameScreenPanel gamePanel;
    
    /**
     * Constructor for objects of class PanelManager
     */
    public PanelManager(JFrame frame) 
    {
        this.frame = frame;
        mainMenuPanel = new MainMenuPanel(this);
        levelSelectionPanel = new LevelSelectionPanel(this);
        gamePanel = new GameScreenPanel(this);
    }
    
    public MainMenuPanel getStartMenu()
    {
        return this.mainMenuPanel;
    }
    
    public void showStartMenu()
    {
        frame.setContentPane(mainMenuPanel);

        frame.revalidate();
    }

    public void showDifficultySelection()
    {
        levelSelectionPanel.updateHighScore();

        frame.setContentPane(levelSelectionPanel);

        frame.revalidate();
    }
    
    public void startGame(String gameSettings)
    {
        frame.setContentPane(gamePanel);

        String values[] = gameSettings.split(","); // Values[0] = Level Index (1 - 3), Values[1] = Difficulty Index (1 - 3)
        
        gamePanel.newGame(Integer.valueOf(values[0]));
        
        frame.revalidate();
    }
}

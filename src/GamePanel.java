
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 18/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GamePanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu 
    private JLayeredPane layeredPane;
    MainPanel main;

    // Side Panels
    private MainGamePanel mainGameP;
    private InfoPanel infoP;
    private DetailPanel detailP;
    
    // Charachter stats
    private static int STARTING_LIVES = 5;
    private int lives = STARTING_LIVES;
    
    private static int STARTING_MONEY = 300;
    private int money = STARTING_MONEY;
    
    // Playing Game
    private boolean playingGame = true;
    boolean isPlaying() { return playingGame; }

    /**
     * Constructor for objects of class MainBoardPanel
     */
    public GamePanel(MainPanel main) 
    {
        this.main = main;
        
        mainGameP = new MainGamePanel(this);
        infoP = new InfoPanel(this, mainGameP);
        detailP = new DetailPanel();

        setLayout(new BorderLayout());

        add(mainGameP, BorderLayout.CENTER);
        add(infoP, BorderLayout.EAST);
        add(detailP, BorderLayout.SOUTH);

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }
       
    public void loseLife()
    {
        this.lives--;
        
        if (lives <= 0)
        {
            gameOver();
            infoP.setLives("DEAD!");
        }
        else if (lives >= 1)
            infoP.setLives(String.valueOf(lives));
    }
    
    public int getCurrentMoney()
    {
        return this.money;
    }
    
    public void spendMoney(int spentMoney)
    {
        this.money -= spentMoney;
        
        infoP.setMoney(money);
    }
    
    public void gainMoney(int gainedMoney)
    {
        this.money += gainedMoney;
        
       infoP.setMoney(money);
    }
    
    public void towerSelected()
    {   
        Tower tower = mainGameP.getSelectedTower();
        
        if (tower != null && detailP != null && tower.isBuilt())
            detailP.towerSelected(tower);
        else
            detailP.towerUnSelected();
    }
    
    private void gameOver()
    {
        main.showGameOver();
        
        playingGame = false;
    }
    
    public void newGame()
    {
        playingGame = true;
        
        money = STARTING_MONEY;
        infoP.setMoney(money);
        
        lives = STARTING_LIVES;
        infoP.setLives(String.valueOf(lives));
        
        if (mainGameP != null)
            mainGameP.newGame();
    }
}

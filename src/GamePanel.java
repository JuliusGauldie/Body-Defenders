/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 25/07/25
 */
import java.awt.*;
import javax.swing.*;
public class GamePanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu 
    MainPanel main;

    // Side Panels
    private MainGamePanel mainGameP;
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
        detailP = new DetailPanel(this);

        setLayout(new BorderLayout());

        add(mainGameP, BorderLayout.CENTER);
        add(detailP, BorderLayout.SOUTH);

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    public void loseLife()
    {
        this.lives--;
        
        if (this.lives <= 0)
            gameOver();
    }

    public int getCurrentMoney()
    {
        return this.money;
    }

    public void spendMoney(int spentMoney)
    {
        this.money -= spentMoney;

        detailP.setMoney(money);
    }

    public void gainMoney(int gainedMoney)
    {
        this.money += gainedMoney;

        detailP.setMoney(money);
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
        detailP.setMoney(money);

        lives = STARTING_LIVES;
        // Set lives counter

        if (mainGameP != null)
            mainGameP.newGame();

        detailP.newGame();

        towerSelected();
    }

    public void upgradeTower(int upgradeVariable) // 1 - Damage, 2 - Range, 3 - Firerate
    { 
        mainGameP.upgradeTower(upgradeVariable);

        detailP.towerSelected(mainGameP.getSelectedTower());
    }

    public void newWave()
    {
        mainGameP.newWave();
    }
}
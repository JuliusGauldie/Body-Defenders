/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 10/08/25
 */
import java.awt.*;
import javax.swing.*;
public class GameLayerPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu 
    GameScreenPanel main;

    // Side Panels
    private GameplayPanel mainGameP;
    private DetailPanel detailP;

    // Charachter stats
    private static int STARTING_HEALTH = 1000;
    private int playerHealth = STARTING_HEALTH;

    private static int STARTING_MONEY = 300;
    private int money = STARTING_MONEY;

    // Playing Game
    private boolean playingGame = true;
    boolean isPlaying() { return playingGame; }

    /**
     * Constructor for objects of class MainBoardPanel
     */
    public GameLayerPanel(GameScreenPanel main) 
    {
        this.main = main;

        mainGameP = new GameplayPanel(this);
        detailP = new DetailPanel(this);

        setLayout(new BorderLayout());

        add(mainGameP, BorderLayout.CENTER);
        add(detailP, BorderLayout.SOUTH);

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    public void takeDamage(int damage)
    {
        this.playerHealth -= damage;
        
        if (this.playerHealth <= 0)
            gameOver(true);

        detailP.setHealth(playerHealth);
    }

    public int getCurrentHealth()
    {
        return this.playerHealth;
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

    public void gameOver(boolean lostGame)
    {
        main.showGameOver(lostGame, mainGameP.calculateScore());

        mainGameP.stopUpdate();

        playingGame = false;
    }

    public void newGame(int levelIndex)
    {
        playingGame = true;

        money = STARTING_MONEY;
        detailP.setMoney(money);

        // Set lives counter
        playerHealth = STARTING_HEALTH;
        
        mainGameP.newGame(levelIndex);
        detailP.newGame();

        towerSelected();
    }

    public void newWave(int currentWave)
    {
        mainGameP.newWave(currentWave);
    }

    public boolean spawningWave()
    {
        return mainGameP.isSpawningWave();
    }
}
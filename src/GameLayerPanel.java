
/**
 * Panel showing the main game board.
 * Handles the game display and player stats
 *
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.awt.*;
import javax.swing.*;

public class GameLayerPanel extends JPanel 
{
    // Canvas size
    public int CANVAS_WIDTH = 800; // Game board width
    public int CANVAS_HEIGHT = 600; // Game board height

    // Reference to main game screen panelf
    private GameScreenPanel main;

    // Panels within the game layer
    private GameplayPanel mainGameP; // The main game area
    private DetailPanel detailP; // Bottom panel showing stats

    // Player health stats
    private static final float STARTING_HEALTH = 200;
    private float playerHealth = STARTING_HEALTH;

    // Player money stats
    private static final int STARTING_MONEY = 300;
    private int money = STARTING_MONEY;

    // Game state
    private boolean playingGame = true;

    // Returns true if the game is currently running
    boolean isPlaying() {
        return playingGame;
    }

    /**
     * Constructor for GameLayerPanel
     * 
     * @param main The main game screen panel that manages menus and transitions
     */
    public GameLayerPanel(GameScreenPanel main) {
        this.main = main;

        // Initialize gameplay and detail panels
        mainGameP = new GameplayPanel(this);
        detailP = new DetailPanel(this);

        // Set layout and add panels
        setLayout(new BorderLayout());
        add(mainGameP, BorderLayout.CENTER);
        add(detailP, BorderLayout.SOUTH);

        // Set panel size and refresh display
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        revalidate();
        repaint();
    }

    /**
     * Decreases player health by the specified amount.
     * Calls gameOver if health drops to 0 or below.
     */
    public void takeDamage(float damage) {
        playerHealth -= damage;

        if (playerHealth <= 0)
            gameOver(true);

        detailP.setHealth(playerHealth);
    }

    // Returns the player's current health
    public float getCurrentHealth() {
        return playerHealth;
    }

    // Returns the player's current money
    public int getCurrentMoney() {
        return money;
    }

    // Deducts money when a tower or upgrade is purchased
    public void spendMoney(int spentMoney) {
        money -= spentMoney;
        detailP.setMoney(money);
    }

    // Adds money when enemies are defeated
    public void gainMoney(int gainedMoney) {
        money += gainedMoney;
        detailP.setMoney(money);
    }

    /**
     * Updates the detail panel with information about the currently selected tower.
     */
    public void towerSelected() {
        Tower tower = mainGameP.getSelectedTower();

        if (tower != null && detailP != null && tower.isBuilt())
            detailP.towerSelected(tower);
        else
            detailP.towerUnSelected();
    }

    /**
     * Ends the game and displays the Game Over screen.
     * 
     * @param lostGame True if the player lost
     */
    public void gameOver(boolean lostGame) {
        main.showGameOver(
                lostGame,
                mainGameP.calculateScore(),
                mainGameP.getTotalKills(),
                mainGameP.getCurrentWave(),
                playerHealth,
                money,
                mainGameP.getDifficultyLevel());

        mainGameP.stopUpdate();
        playingGame = false;
    }

    /**
     * Starts a new game with the given level and difficulty.
     */
    public void newGame(int levelIndex, float difficultyIndex) {
        playingGame = true;

        // Reset player stats
        money = STARTING_MONEY;
        playerHealth = STARTING_HEALTH * ((float) 1 / difficultyIndex);

        // Update detail panel
        detailP.setMoney(money);
        detailP.setHealth(playerHealth);
        detailP.newGame();

        // Start new gameplay
        mainGameP.newGame(levelIndex, difficultyIndex);

        // Update tower info
        towerSelected();
    }

    // Starts a new wave
    public void newWave(int currentWave) {
        mainGameP.newWave(currentWave);
    }

    // Returns true if a wave is currently spawning
    public boolean spawningWave() {
        return mainGameP.isSpawningWave();
    }

    // Returns to the main menu screen
    public void returnToMenu() {
        main.showStartMenu();
    }
}

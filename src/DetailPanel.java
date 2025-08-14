
/**
 * DetailPanel is the user interface panel showing game stats and tower information.
 * 
 * This panel displays:
 * - Current round/wave
 * - Player money
 * - Player health
 * - Selected tower stats (name, damage, range, fire rate)
 * - Button to start next wave
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.awt.*;
import javax.swing.*;

public class DetailPanel extends JPanel 
{
    // Panel size
    public int CANVAS_WIDTH = 800;
    public int CANVAS_HEIGHT = 80;

    // Labels for display
    private JLabel moneyLabel;
    private JLabel healthLabel;
    private JLabel towerNameLabel;
    private JLabel damageLabel;
    private JLabel rangeLabel;
    private JLabel fireRateLabel;
    private JLabel waveLabel;

    // Button to start a new wave
    private JButton newWaveButton;

    // Tracks current wave
    private int currentWave = 1;

    // Reference to the main game panel for interaction
    private GameLayerPanel main;

    /**
     * Constructor for DetailPanel.
     * Initializes all labels, buttons, and layout.
     * 
     * @param main Reference to the main GameLayerPanel for starting waves and
     *             checking state
     */
    public DetailPanel(GameLayerPanel main) {
        this.main = main;
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Left-align components
        setFocusable(false);

        // Wave label
        waveLabel = new JLabel("ROUND 1 OF 15");
        add(waveLabel);

        // Money label (starts with 300 DNA)
        moneyLabel = new JLabel("DNA: " + main.getCurrentMoney());
        add(moneyLabel);

        // Health label (starts at 1000)
        healthLabel = new JLabel("Health: " + main.getCurrentHealth());
        add(healthLabel);

        // New wave button
        newWaveButton = new JButton("New Wave");
        newWaveButton.addActionListener(e -> newWave()); // Trigger newWave() on click
        add(newWaveButton);

        // Tower information labels (empty initially)
        towerNameLabel = new JLabel("");
        add(towerNameLabel);

        damageLabel = new JLabel("");
        add(damageLabel);

        rangeLabel = new JLabel("");
        add(rangeLabel);

        fireRateLabel = new JLabel("");
        add(fireRateLabel);

        // Set panel preferred size
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    /**
     * Updates the detail panel to show stats of the selected tower.
     * 
     * @param tower The tower currently selected
     */
    public void towerSelected(Tower tower) {
        towerNameLabel.setText(tower.getName() + " ||");
        damageLabel.setText("DAMAGE: " + tower.getDamage());
        rangeLabel.setText("RANGE: " + tower.getRange());
        fireRateLabel.setText(String.format("FIRERATE: %.1f", tower.getFireRate()));
    }

    /**
     * Clears tower info when no tower is selected.
     */
    public void towerUnSelected() {
        towerNameLabel.setText("");
        damageLabel.setText("");
        rangeLabel.setText("");
        fireRateLabel.setText("");
    }

    /**
     * Updates the money label to show the current amount.
     * 
     * @param money Current player DNA/money
     */
    public void setMoney(int money) {
        moneyLabel.setText("DNA: " + money);
    }

    /**
     * Updates the health label to show current player health.
     * Displays "DEAD!" if health reaches 0 or below.
     * 
     * @param health Current player health
     */
    public void setHealth(float health) {
        if (health > 0)
            healthLabel.setText("Health: " + health);
        else
            healthLabel.setText("Health: DEAD!");
    }

    /**
     * Starts a new wave if the game is not already spawning a wave.
     */
    public void newWave() {
        if (!main.spawningWave()) {
            currentWave++;
            waveLabel.setText("ROUND " + currentWave + " OF 15");
            main.newWave(currentWave);
        }
    }

    /**
     * Resets the panel for a new game.
     */
    public void newGame() {
        currentWave = 1;
        waveLabel.setText("ROUND " + currentWave + " OF 15");
        healthLabel.setText("Health: " + (int) main.getCurrentHealth());
        moneyLabel.setText("DNA: " + (int) main.getCurrentMoney());
        towerUnSelected(); // Clear tower info
    }
}

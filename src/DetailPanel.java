/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 10/08/25
 */
import java.awt.*;
import javax.swing.*;
public class DetailPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 80;

    // LABELS
    // Money
    JLabel moneyLabel;

    // Health
    JLabel healthLabel;

    // Tower Stats
    JLabel towerNameLabel;
    JLabel damageLabel;
    JLabel rangeLabel;
    JLabel fireRateLabel;

    // Wave Info
    JLabel waveLabel;
    private int currentWave = 1;
    JButton newWaveButton;

    GameLayerPanel main;

    /**
     * Constructor for objects of class DetailPanel
     */
    public DetailPanel(GameLayerPanel main) 
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));  

        this.main = main;

        this.setFocusable(false);

        // Wave
        waveLabel = new JLabel("ROUND 1 OF 50");
        add(waveLabel);

        // Money
        moneyLabel = new JLabel("Money: 300");
        add(moneyLabel);

        // Health
        healthLabel = new JLabel("Health: 1000");
        add(healthLabel);

        // Wave Button
        newWaveButton = new JButton("New Wave");
        newWaveButton.addActionListener(e -> newWave());
        add(newWaveButton);

        // Tower Name
        towerNameLabel = new JLabel("");
        super.add(towerNameLabel);

        // Damage
        damageLabel = new JLabel();
        add(damageLabel);

        // Range
        rangeLabel = new JLabel();
        add(rangeLabel);

        // Firerate
        fireRateLabel = new JLabel();
        add(fireRateLabel);

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    public void towerSelected(Tower tower)
    {
        towerNameLabel.setText(tower.getName() + " ||");
        damageLabel.setText("DAMAGE: " + tower.getDamage());
        rangeLabel.setText("RANGE: " + tower.getRange());
        fireRateLabel.setText(String.format("FIRERATE: %.1f", tower.getFireRate()));
    }

    public void towerUnSelected()
    {
        towerNameLabel.setText("");
        damageLabel.setText("");
        rangeLabel.setText("");
        fireRateLabel.setText("");
    }

    public void setMoney(int money)
    {
        moneyLabel.setText("DNA: " + String.valueOf(money));
    }

    public void setHealth(int health)
    {
        if (health > 0)
            healthLabel.setText("Health: " + String.valueOf(health));
        else
            healthLabel.setText("Health: DEAD!");
    }

    public void newWave()
    {
        if (!main.spawningWave())
        {
            currentWave++;

            waveLabel.setText("ROUND " + currentWave + " OF 50");

            main.newWave(currentWave);
        }
    }

    public void newGame()
    {
        currentWave = 1;

        waveLabel.setText("ROUND " + currentWave + " OF 50");
        healthLabel.setText("Health: 1000");

    }
}

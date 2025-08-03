/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 03/08/25
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

    // Tower Stats
    JLabel towerNameLabel;
    JLabel damageLabel;
    JLabel rangeLabel;
    JLabel fireRateLabel;

    // Wave Info
    JLabel waveLabel;
    private int currentWave = 1;
    JButton newWaveButton;

    GamePanel main;

    /**
     * Constructor for objects of class DetailPanel
     */
    public DetailPanel(GamePanel main) 
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));  

        this.main = main;

        this.setFocusable(false);

        // Wave
        waveLabel = new JLabel("ROUND 1 OF 50");
        add(waveLabel);

        // Money
        moneyLabel = new JLabel("Money: 300");
        super.add(moneyLabel);

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
        towerNameLabel.setText(tower.getName());
        damageLabel.setText("DAMAGE: " + tower.getDamage());
        rangeLabel.setText("RANGE: " + tower.getRange());
        fireRateLabel.setText("FIRERATE: " + tower.getFireRate());
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
        moneyLabel.setText("Money: " + String.valueOf(money));
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
    }
}

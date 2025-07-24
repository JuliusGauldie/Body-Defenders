/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 24/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DetailPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 100;

    // LABELS
    // Money
    JLabel moneyLabel;
    private int money = 300;

    // Tower Stats
    JLabel damageLabel;
    JLabel rangeLabel;

    // Tower Upgrades
    JButton upgradeTowerDamage;
    JButton upgradeTowerRange;

    // Wave Info
    JLabel waveLabel;
    private int currentWave = 1;

    // Boolean 
    private boolean hasTowerSelected = false;

    GamePanel main;

    /**
     * Constructor for objects of class DetailPanel
     */
    public DetailPanel(GamePanel main) 
    {
        setLayout(new FlowLayout());  

        this.main = main;

        this.setFocusable(false);

        // Wave
        waveLabel = new JLabel("ROUND 1 OF 50");
        add(waveLabel);

        // Money
        moneyLabel = new JLabel("Money: 300");
        super.add(moneyLabel);
        
        // Unit
        
        
        // Damage
        damageLabel = new JLabel();
        add(damageLabel);

        // Range
        rangeLabel = new JLabel();
        add(rangeLabel);

        // Firerate

        upgradeTowerDamage = new JButton("");
        upgradeTowerDamage.addActionListener(e -> upgradeTower(1));
        add(upgradeTowerDamage);

        upgradeTowerRange = new JButton("");
        upgradeTowerRange.addActionListener(e -> upgradeTower(2));
        add(upgradeTowerRange);


        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    public void towerSelected(Tower tower)
    {
        damageLabel.setText("DAMAGE: " + tower.getDamage());
        rangeLabel.setText("RANGE: " + tower.getRange());
        upgradeTowerDamage.setText("UPGRADE TOWER DAMAGE");
        upgradeTowerRange.setText("UPGRADE TOWER RANGE");

        hasTowerSelected = true;
    }

    public void towerUnSelected()
    {
        damageLabel.setText("");
        rangeLabel.setText("");
        upgradeTowerDamage.setText("");
        upgradeTowerRange.setText("");

        hasTowerSelected = false;
    }

    public void setMoney(int money)
    {
        this.money = money;
        
        moneyLabel.setText("Money: " + String.valueOf(money));
    }

    public void newWave()
    {
        currentWave++;

        waveLabel.setText("ROUND " + currentWave + " OF 50");
    }

    private void upgradeTower(int upgradeVariable) // 1 - Damage, 2 - Range, 3 - Firerate
    {
        switch (upgradeVariable)
        {
            case 1:
                if (main.getCurrentMoney() >= 200 && hasTowerSelected)
                {
                    main.upgradeTower(1);
                    main.spendMoney(200);
                }

            case 2:
                if (main.getCurrentMoney() >= 300 && hasTowerSelected)
                {
                    main.upgradeTower(2);
                    main.spendMoney(300);
                }

                break;
        }
    }

    public void newGame()
    {
        currentWave = 1;

        waveLabel.setText("ROUND " + currentWave + " OF 50");
    }
}

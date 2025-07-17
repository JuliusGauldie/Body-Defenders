
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 17/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class InfoPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 150; //Game Board widht/height
    public int CANVAS_HEIGHT = 650;
    
    MainGamePanel mainPanel;
    
    private Icon tower1Icon = new ImageIcon("../assets/tower.png");
    ImageIcon selectedImage = new ImageIcon("../assets/towerSelected.png");
    
    // JLabels in infopanel
    JLabel livesLabel;
    JLabel currencyLabel;
    
    // Charachter stats
    private int money = 300;
    
    GamePanel main;
    
    /**
     * Constructor for objects of class InfoPanel
     */
    public InfoPanel(GamePanel main, MainGamePanel panel) 
    {
        setLayout(new GridLayout(4, 1));  
        
        this.main = main;
        this.mainPanel = panel;
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        this.setFocusable(false);
        
        currencyLabel = new JLabel("Money: 300");
        currencyLabel.setPreferredSize(new Dimension (150, 50));
        super.add(currencyLabel);
        
        livesLabel = new JLabel("Lives: 5");
        livesLabel.setPreferredSize(new Dimension (150, 50));
        super.add(livesLabel);
        
        JPanel towerPanel = new JPanel();
        towerPanel.setLayout(new GridLayout(4, 2, 10, 10));
        towerPanel.setPreferredSize(new Dimension (150, 450));
        
        JButton tower1Button = new JButton(tower1Icon);
        tower1Button.addActionListener(e -> towerSelected(50));
        tower1Button.setPreferredSize(new Dimension (150, 50));
        towerPanel.add(tower1Button);
        
        super.add(towerPanel);
        
        JButton newWaveButton = new JButton("New Wave");
        newWaveButton.addActionListener(e -> newWave());
        super.add(newWaveButton);
    }
    
    public void newWave()
    {
        mainPanel.newWave();
        
        main.gainMoney(25);
    }
    
    private void towerSelected(int towerCost)
    {
        if (mainPanel.getSelectedTower() != null && !mainPanel.getSelectedTower().isBuilt())
        {
            if (main.getCurrentMoney() >= towerCost)
            {
                mainPanel.getSelectedTower().built();
                
                main.spendMoney(towerCost);
                
                main.towerSelected();
            }
        }
    }
        
    public void setLives(String lives)
    {
        livesLabel.setText("Lives: " + lives);
    }
    
    public void setMoney(int money)
    {
        currencyLabel.setText("Money: " + String.valueOf(money));
        
        this.money = money;
    }
}

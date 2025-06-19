
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 19/06/25
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

    /**
     * Constructor for objects of class InfoPanel
     */
    public InfoPanel(MainGamePanel panel) 
    {
        setLayout(new GridLayout(4, 1));  
        
        mainPanel = panel;
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        
        JLabel currencyLabel = new JLabel("Money: ");
        currencyLabel.setMaximumSize(new Dimension (150, 50));
        super.add(currencyLabel);
        
        JLabel livesLabel = new JLabel("Lives: ");
        livesLabel.setMaximumSize(new Dimension (150, 50));
        super.add(livesLabel);
        
        JPanel towerPanel = new JPanel();
        towerPanel.setLayout(new GridLayout(4, 2, 10, 10));
        towerPanel.setPreferredSize(new Dimension (150, 450));
        super.add(towerPanel);
        
        JButton newWaveButton = new JButton("New Wave");
        newWaveButton.addActionListener(e -> newWave());
        super.add(newWaveButton);
    }
    
    public void newWave()
    {
        mainPanel.newWave();
    }
}

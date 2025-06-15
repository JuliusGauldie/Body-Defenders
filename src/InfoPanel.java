
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 16/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class InfoPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 150; //Game Board widht/height
    public int CANVAS_HEIGHT = 650;

    /**
     * Constructor for objects of class InfoPanel
     */
    public InfoPanel() 
    {
        setLayout(new GridLayout(4, 1));  
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        
        JLabel currencyLabel = new JLabel("Money: ");
        currencyLabel.setPreferredSize(new Dimension (150, 30));
        super.add(currencyLabel);
        
        JLabel livesLabel = new JLabel("Lives: ");
        super.add(livesLabel);
        
        JPanel towerPanel = new JPanel();
        towerPanel.setLayout(new GridLayout(4, 2, 10, 10));
        towerPanel.setPreferredSize(new Dimension (150, 200));
        super.add(towerPanel);
        
        JButton newWaveButton = new JButton("New Wave");
        newWaveButton.addActionListener(e -> newWave());
        super.add(newWaveButton);
    }
    
    public void newWave()
    {
        
    }
}

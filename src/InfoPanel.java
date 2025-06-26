
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 27/06/25
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

    /**
     * Constructor for objects of class InfoPanel
     */
    public InfoPanel(MainGamePanel panel) 
    {
        setLayout(new GridLayout(4, 1));  
        
        mainPanel = panel;
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        this.setFocusable(false);
        
        JLabel currencyLabel = new JLabel("Money: ");
        currencyLabel.setPreferredSize(new Dimension (150, 50));
        super.add(currencyLabel);
        
        JLabel livesLabel = new JLabel("Lives: ");
        livesLabel.setPreferredSize(new Dimension (150, 50));
        super.add(livesLabel);
        
        JPanel towerPanel = new JPanel();
        towerPanel.setLayout(new GridLayout(4, 2, 10, 10));
        towerPanel.setPreferredSize(new Dimension (150, 450));
        
        JButton tower1Button = new JButton(tower1Icon);
        tower1Button.addActionListener(e -> towerSelected());
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
    }
    
    private void towerSelected()
    {
        mainPanel.towerSelected(true, selectedImage, 150);
    }
    
    private void unSelected()
    {
        mainPanel.towerSelected(false, null, 150);
    }
}

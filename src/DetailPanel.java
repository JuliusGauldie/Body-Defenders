
/**
 * Write a description of class Main here.
 *
 * @author Julius Gauldie
 * @version 18/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DetailPanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 150;
    
    // Labels
    JLabel damageLabel;
    JLabel rangeLabel;

    /**
     * Constructor for objects of class DetailPanel
     */
    public DetailPanel() 
    {
        setLayout(new FlowLayout());  

        this.setFocusable(false);
        
        damageLabel = new JLabel();
        add(damageLabel);
        
        rangeLabel = new JLabel();
        add(rangeLabel);
        
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }
    
    public void towerSelected(Tower tower)
    {
        damageLabel.setText("DAMAGE: " + tower.getDamage());
        rangeLabel.setText("RANGE: " + tower.getRange());
    }
    
    public void towerUnSelected()
    {
        damageLabel.setText("");
        rangeLabel.setText("");
    }
}

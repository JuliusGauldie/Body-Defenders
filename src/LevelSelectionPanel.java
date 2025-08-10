
/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 10/08/25
 */
import java.awt.*;
import javax.swing.*;

public class LevelSelectionPanel extends JPanel
{
    public LevelSelectionPanel(PanelManager manager) 
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Select Map and Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, gbc);

        String[] maps = {"Level 1: Heart", "Level 2: Brain", "Level 3: Stomach"};
        String[] difficulties = {"Easy", "Medium", "Hard"};

        gbc.gridy++;
        for (int i = 0; i < 3; i++) 
        {
            JPanel mapPanel = new JPanel();
            mapPanel.setLayout(new FlowLayout());

            JLabel mapLabel = new JLabel(maps[i]);
            mapLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            mapPanel.add(mapLabel);

            for (int j = 0; j < difficulties.length; j++) {
                JButton btn = new JButton(difficulties[j]);
                btn.setActionCommand(i + 1 + "," + j + 1); // Level number, Difficulty number

                btn.addActionListener(e -> 
                {
                    String choice = ((JButton)e.getSource()).getActionCommand();
                    manager.startGame(choice);      
                });
                
                mapPanel.add(btn);
            }
            gbc.gridy++;
            add(mapPanel, gbc);
        }
    }
}

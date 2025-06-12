
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 12/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GamePanel extends JPanel
{
    // Size
    public int CANVAS_WIDTH = 800; //Game Board widht/height
    public int CANVAS_HEIGHT = 600;

    // Pause Menu 
    private JLayeredPane layeredPane;
    MainPanel main;

    // Side Panels
    private MainGamePanel mainGameP;
    private InfoPanel infoP;
    private DetailPanel detailP;

    /**
     * Constructor for objects of class MainBoardPanel
     */
    public GamePanel(MainPanel main) 
    {
        this.main = main;

        mainGameP = new MainGamePanel();
        infoP = new InfoPanel();
        detailP = new DetailPanel();

        setLayout(new BorderLayout());

        JButton pauseButton = new JButton("Pause");
        //add(pauseButton);
        pauseButton.addActionListener(e -> showPauseMenu());

        mainGameP.setBackground(Color.BLUE); 
        add(mainGameP, BorderLayout.CENTER);

        infoP.setBackground(Color.ORANGE); 
        add(infoP, BorderLayout.EAST);

        detailP.setBackground(Color.RED); 
        add(detailP, BorderLayout.SOUTH);

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();
    }

    void showPauseMenu()
    {
        main.showPauseMenu();
    }
}

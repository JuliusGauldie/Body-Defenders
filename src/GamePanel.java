
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 17/07/25
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
    
    // Charachter stats
    private int lives = 5;
    private int money = 300;

    /**
     * Constructor for objects of class MainBoardPanel
     */
    public GamePanel(MainPanel main) 
    {
        this.main = main;

        mainGameP = new MainGamePanel(this);
        infoP = new InfoPanel(this, mainGameP);
        detailP = new DetailPanel();

        setLayout(new BorderLayout());

        JButton pauseButton = new JButton("Pause");
        //add(pauseButton);
        pauseButton.addActionListener(e -> showPauseMenu());

        add(mainGameP, BorderLayout.CENTER);
        add(infoP, BorderLayout.EAST);
        add(detailP, BorderLayout.SOUTH);

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        super.revalidate();
        super.repaint();

    }

    void showPauseMenu()
    {
        main.showPauseMenu();
    }
       
    public void loseLife()
    {
        this.lives--;
        
        if (lives == 0)
            gameOver();
        else
            infoP.setLives(String.valueOf(lives));
    }
    
    public int getCurrentMoney()
    {
        return this.money;
    }
    
    public void spendMoney(int spentMoney)
    {
        this.money -= spentMoney;
        
        infoP.setMoney(money);
    }
    
    public void gainMoney(int gainedMoney)
    {
        this.money += gainedMoney;
        
       infoP.setMoney(money);
    }
    
    public void towerSelected()
    {   
        Tower tower = mainGameP.getSelectedTower();
        
        if (tower != null && detailP != null && tower.isBuilt())
            detailP.towerSelected(tower);
        else
            detailP.towerUnSelected();
    }
    
    private void gameOver()
    {
        // Game Over
    }
}

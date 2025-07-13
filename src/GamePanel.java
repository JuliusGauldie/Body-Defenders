
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 14/07/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GamePanel extends JPanel implements KeyListener
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

        // Keyboard
        addKeyListener(this); // Register KeyListener

        mainGameP = new MainGamePanel(this);
        infoP = new InfoPanel(this, mainGameP);
        detailP = new DetailPanel();

        setLayout(new BorderLayout());

        JButton pauseButton = new JButton("Pause");
        //add(pauseButton);
        pauseButton.addActionListener(e -> showPauseMenu());

        add(mainGameP, BorderLayout.CENTER);

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

    public void  keyTyped(KeyEvent e)
    {

    }

    public void keyPressed(KeyEvent e)
    {
            System.out.println("TEST");
    }

    public void keyReleased(KeyEvent e)
    {

    }
       
    public void loseLife()
    {
        this.lives--;
        
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
}

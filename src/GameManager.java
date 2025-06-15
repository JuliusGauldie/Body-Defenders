
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 16/06/25
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameManager
{
    private int enemyXSpawn = 50;
    private int enemyYSpawn = 50;

    Enemies enemy;

    /**
     * Constructor for objects of class GameManager
     */
    public GameManager()
    {

    }

    public void newWave(int amountOfEnemies)
    {
        for (int i = 0; i < amountOfEnemies; i++)
        {
            new Enemies(enemyXSpawn, enemyYSpawn);
        }
    }
}

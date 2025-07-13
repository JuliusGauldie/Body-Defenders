import java.util.List;
import javax.swing.ImageIcon;

/**
 * Write a description of class Towers here.
 *
 * @author Julius Gauldie
 * @version 14/07/25
 */
public class Tower
{
    // Location
    public int xLocation, yLocation;
    
    // Tower stats
    private int damage = 50;
    public int range = 90;
    float fireRate = 0.5f;
    
    // Tower economic stats
    private int towerCost = 100;
    
    // Time
    private long lastShotTime;

    MainGamePanel main;

    // Images
    ImageIcon image = new ImageIcon("../assets/tower.png");

    /**
     * Constructor for objects of class Towers
     */
    public Tower(MainGamePanel main, int x, int y)
    {
        this.main = main;

        this.xLocation = x;
        this.yLocation = y;
    }

    public void update()
    {
        Enemy target = findTarget();

        if (target != null && canFire())
        {
            fireAt(target);
        }

    }

    private Enemy findTarget()
    {
        for (Enemy e : main.enemies)
        {
            if (e.isAlive() && inRange(e))
            {
                return e;
            }
        }

        return null;
    }

    private boolean inRange(Enemy enemy)
    {
        float dx = enemy.xLocation - xLocation;
        float dy = enemy.yLocation - yLocation;

        return Math.sqrt(dx * dx + dy * dy) <= range;
    }

    private boolean canFire()
    {
        long currentTime = System.currentTimeMillis();
        long delay = (long)(1000 / fireRate); // delay between shots in milliseconds

        return (currentTime - lastShotTime) >= delay;
    }

    private void fireAt(Enemy enemy)
    {
        lastShotTime = System.currentTimeMillis();
        main.projectiles.add(new Projectile(xLocation, yLocation, enemy, this.damage));
    }
}

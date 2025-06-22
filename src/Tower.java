import java.util.List;
import javax.swing.ImageIcon;

/**
 * Write a description of class Towers here.
 *
 * @author Julius Gauldie
 * @version 23/06/25
 */
public class Tower
{
    // instance variables 
    public int xLocation, yLocation;
    int damage = 25;
    float range = 150f;
    float fireRate = 0.5f;
    long lastShotTime;

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

    public void update(List <Enemy> enemy, List<Projectile> projectiles)
    {
        Enemy target = findTarget(main.enemies);

        if (target != null && canFire())
        {
            fireAt(target, projectiles);
        }
    }

    private Enemy findTarget(List <Enemy> enemy)
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

    private boolean inRange(Enemy enemies)
    {
        float dx = enemies.xLocation - xLocation;
        float dy = enemies.yLocation - yLocation;

        return Math.sqrt(dx * dx + dy * dy) <= range;
    }

    private boolean canFire()
    {
        long currentTime = System.currentTimeMillis();
        long delay = (long)(1000 / fireRate); // delay between shots in milliseconds
        
        return (currentTime - lastShotTime) >= delay;
    }

    private void fireAt(Enemy enemy, List<Projectile> projectiles)
    {
        lastShotTime = System.currentTimeMillis();
        main.projectiles.add(new Projectile(xLocation, yLocation, enemy, this.damage));
    }
}

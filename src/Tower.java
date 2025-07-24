import java.util.List;
import javax.swing.ImageIcon;

/**
 * Write a description of class Towers here.
 *
 * @author Julius Gauldie
 * @version 24/07/25
 */
public class Tower
{
    // Location
    public int xLocation, yLocation;

    // Tower stats
    private int damage;
    private int range = 20;
    float fireRate;

    // Tower economic stats
    private int towerCost = 100;

    // Time
    private long lastShotTime;

    MainGamePanel main;

    // Images
    ImageIcon initialTower = new ImageIcon("../assets/towerInitial.png");
    ImageIcon image = new ImageIcon("../assets/tower.png");

    // Boolean
    private boolean isBuilt = false;
    boolean isBuilt() { return isBuilt; }

    /**
     * Constructor for objects of class Towers
     */
    public Tower(MainGamePanel main, int x, int y)
    {
        this.main = main;

        this.xLocation = x;
        this.yLocation = y;
    }

    public void setTowerStats(int damage, int range, float fireRate)
    {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
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

    public void built()
    {
        this.isBuilt = true;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getRange()
    {
        return range;
    }

    public void resetTower()
    {
        this.isBuilt = false;
    }

    public void upgradeTower(int upgradeVariable) // 1 - Damage, 2 - Range, 3 - Firerate
    {
        switch (upgradeVariable)
        {
            case 1:
                this.damage += 20;            
            case 2:
                this.range += 10;
                
                break;
        }
    }
}

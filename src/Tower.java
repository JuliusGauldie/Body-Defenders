import javax.swing.ImageIcon;

/**
 * Write a description of class Towers here.
 *
 * @author Julius Gauldie
 * @version 07/08/25
 */
public class Tower
{
    // Location
    public int xLocation, yLocation;

    // Tower stats
    private float damage;
    private int range;
    float fireRate;
    
    private String towerName;

    private int towerIndex; // 1 - Initial, 2 - Sniper, 3 - Area, 4 - Slow, 5 - Laser

    // Tower economic stats
    private int towerCost;

    // Time
    private long lastShotTime;

    GameplayPanel main;

    // Images
    ImageIcon initialTower = new ImageIcon("resources/assets/towerInitial.png");
    ImageIcon image;

    // Boolean
    private boolean isBuilt = false;
    private boolean canFire = false;
    private boolean isBuffed = false;
    private float buffAmount = 0;

    boolean isBuilt() { return isBuilt; }

    /**
     * Constructor for objects of class Towers
     */
    public Tower(GameplayPanel main, int x, int y)
    {
        this.main = main;

        this.xLocation = x;
        this.yLocation = y;
    }

    public void setTowerStats(float damage, int range, float fireRate, String EnemyImageFileName, String name, int towerCost, int towerIndex)
    {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.towerCost = towerCost;
        this.image = new ImageIcon(EnemyImageFileName);
        this.towerName = name;
        this.towerIndex = towerIndex;

        if (this.damage > 0)
            this.canFire = true;
    }

    public void update()
    {
        if (canFire)
        {
            Enemy target = findTarget();

            if (target != null && canFire())
            {
                fireAt(target);
            }
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
    float towerCenterX = this.xLocation + image.getIconWidth() / 2;
    float towerCenterY = this.yLocation + image.getIconHeight() / 2;
    float enemyCenterX = enemy.xLocation + enemy.image.getIconWidth() / 2;
    float enemyCenterY = enemy.yLocation + enemy.image.getIconHeight() / 2;

        float dx = enemyCenterX - towerCenterX;
        float dy = enemyCenterY - towerCenterY;

        return Math.sqrt(dx * dx + dy * dy) <= range;
    }

    private boolean canFire()
    {
        float currentFirerate = fireRate;

        if (isBuffed)
        {
            currentFirerate *= buffAmount;
        }

        long currentTime = System.currentTimeMillis();
        long delay = (long)(1000 / currentFirerate); // delay between shots in milliseconds

        return (currentTime - lastShotTime) >= delay;
    }

    private void fireAt(Enemy enemy)
    {
        lastShotTime = System.currentTimeMillis();

        main.projectiles.add(new Projectile(xLocation, yLocation, enemy, this.damage, towerIndex));
    }

    public void built()
    {
        this.isBuilt = true;
    }
    
    public void refund()
    {
        this.isBuilt = false;
    }
    
    public String getName()
    {
        return this.towerName;
    }

    public float getDamage()
    {
        return damage;
    }

    public int getRange()
    {
        return range;
    }

    public float getFireRate()
    {
        if (isBuffed)
            return fireRate * buffAmount;
        else
            return fireRate;
    }
    
    public int getCost()
    {
        return towerCost;
    }

    public void resetTower()
    {
        this.isBuilt = false;
    }

    public int getTowerIndex()
    {
        return towerIndex;
    }

    public void receiveBuff(float buffAmount)
    {
        isBuffed = true;
        this.buffAmount = buffAmount;
    }

    public void removeBuff()
    {
        isBuffed = false;
    }

    public void upgradeTower(int upgradeVariable) // 1 - Damage, 2 - Range, 3 - Firerate
    {
        switch (upgradeVariable)
        {
            case 1:
                this.damage += 20; 
                
                break;
            case 2:
                this.range += 10;
                
                break;
        }
    }
}

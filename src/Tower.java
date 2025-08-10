import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Write a description of class Towers here.
 *
 * @author Julius Gauldie
 * @version 10/08/25
 */
public class Tower
{
    // Location
    public int xLocation, yLocation;

    // Tower stats
    private float damage;
    private int range;
    private float fireRate;
    
    private String towerName;

    private int towerIndex; // 1 - Initial, 2 - Sniper, 3 - Area, 4 - Slow, 5 - Laser

    private int currentLevel = 1;

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

    // Abilities
    private boolean ability1 = false;
    private boolean ability2 = false;
    boolean hasAbility1() { return ability1; }
    boolean hasAbility2() { return ability2; }

    // Buffs
    public ArrayList<Tower> buffTowers = new ArrayList<>();
    private int buffAmount = 0;

    // Is tower built 
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
            if (e.isAlive() && inRange(e) && !e.isTunneled())
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

        if (buffAmount > 0)
            currentFirerate *= (1 + (0.2f * buffAmount));

        long currentTime = System.currentTimeMillis();
        long delay = (long)(1000 / currentFirerate); // delay between shots in milliseconds

        return (currentTime - lastShotTime) >= delay;
    }

    private void fireAt(Enemy enemy)
    {
        lastShotTime = System.currentTimeMillis();

        if (towerIndex == 1 && ability1) // Tower 1 - Ability 1
        {
            int gap = 6;
            float dx = enemy.xLocation - xLocation, dy = enemy.yLocation - yLocation, len = (float) Math.hypot(dx, dy);
            float px = -dy / len * gap, py = dx / len * gap;

            main.projectiles.add(new Projectile((int)(xLocation + px), (int)(yLocation + py), enemy, this.damage, this));
            main.projectiles.add(new Projectile((int)(xLocation - px), (int)(yLocation - py), enemy, this.damage, this));
        }  
        else 
        {
            main.projectiles.add(new Projectile(xLocation, yLocation, enemy, this.damage, this));
        }
    }

    public void built() { this.isBuilt = true; }
    
    public void resetTower() { this.isBuilt = false; ability1 = false; ability2 = false; }
    
    public String getName() { return this.towerName;}

    public float getDamage() { return damage; }

    public int getRange() { return range; }

    public float getFireRate()
    {
        if (buffAmount > 0)
            return fireRate *= (1 + (0.2f * buffAmount));
        else
            return fireRate;
    }
    
    public int getCost() { return towerCost; }

    public int getTowerIndex() { return towerIndex; }

    public int getCurrentLevel() { return currentLevel; }

    public boolean onRightSide() 
    { 
        return this.xLocation >= 400;
    }

    public void receiveBuff(Tower buffTower)
    {
        buffAmount++;

        buffTowers.add(buffTower);
    }

    public void removeBuff(Tower buffTower)
    {
        buffAmount--;

        for (Tower t : buffTowers)
        {
            if (t == buffTower)
            {
                buffTowers.remove(t);
                break;
            }
        }
    }

    public void upgradeTower(float newDamage, int newRange, float newFireRate) 
    {
        currentLevel++;

        this.damage = newDamage;
        this.range = newRange;
        this.fireRate = newFireRate;
    }

    public void getAbility1()
    {
        ability1 = true;
    }

    public void getAbility2()
    {
        ability2 = true;
    }
}


/**
 * Tower class represents a tower
 * 
 * Author: Julius Gauldie
 * Version: 14/08/25
 */
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Tower
{
    // Location
    public int xLocation, yLocation;

    // Image
    ImageIcon image;
    ImageIcon initialTower = new ImageIcon("resources/assets/towerInitial.png");

    // Tower stats
    private float damage;
    private int range;
    private float fireRate;
    private int towerCost;
    private String towerName;
    private int towerIndex; // 1 - Initial, 2 - Sniper, 3 - Area, 4 - Slow, 5 - Laser
    private int currentLevel = 1;

    // State & timing
    private boolean isBuilt = false;
    private boolean canFire = false;
    private long lastShotTime;

    // Abilities
    private boolean ability1 = false;
    private boolean ability2 = false;

    public boolean hasAbility1() { return ability1; }
    public boolean hasAbility2() { return ability2; }

    // Buffs
    public ArrayList<Tower> buffTowers = new ArrayList<>();
    private int buffAmount = 0;

    GameplayPanel main;

    // Constructor
    public Tower(GameplayPanel main, int x, int y)
    {
        this.main = main;
        this.xLocation = x;
        this.yLocation = y;
    }

    // Setup & stats
    public void setTowerStats(float damage, int range, float fireRate, String imageFile, String name, int towerCost, int towerIndex)
    {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.towerCost = towerCost;
        this.image = new ImageIcon(imageFile);
        this.towerName = name;
        this.towerIndex = towerIndex;

        if (this.damage > 0) this.canFire = true;
    }

    // Update
    public void update()
    {
        if (canFire)
        {
            Enemy target = findTarget();
            if (target != null && canFire())
                fireAt(target);
        }
    }

    private Enemy findTarget()
    {
        for (Enemy e : main.enemies)
        {
            if (e.isAlive() && inRange(e) && !e.isTunneled())
                return e;
        }
        return null;
    }

    private boolean inRange(Enemy enemy)
    {
        float towerCenterX = xLocation + image.getIconWidth() / 2f;
        float towerCenterY = yLocation + image.getIconHeight() / 2f;
        float enemyCenterX = enemy.xLocation + enemy.image.getIconWidth() / 2f;
        float enemyCenterY = enemy.yLocation + enemy.image.getIconHeight() / 2f;

        float dx = enemyCenterX - towerCenterX;
        float dy = enemyCenterY - towerCenterY;
        return Math.sqrt(dx * dx + dy * dy) <= range;
    }

    private boolean canFire()
    {
        float effectiveFireRate = fireRate * (1 + 0.2f * buffAmount);
        long delay = (long)(1000 / effectiveFireRate);
        return (System.currentTimeMillis() - lastShotTime) >= delay;
    }

    private void fireAt(Enemy enemy)
    {
        lastShotTime = System.currentTimeMillis();

        float finalDamage = damage;

        if (buffAmount > 0 && checkAbility2BuffedTowers())
        {
            // Create a random object
            double chance = 0.10 * buffAmount; // 10% per buff
            double roll = Math.random(); // returns 0.0 - 1.0

            if (roll < chance) {
                finalDamage *= 2; // Double damage
            }
        }

        if (towerIndex == 1 && ability1) // Tower 1 - Ability 1: Split shot
        {
            int gap = 6;
            float dx = enemy.xLocation - xLocation;
            float dy = enemy.yLocation - yLocation;
            float len = (float)Math.hypot(dx, dy);
            float px = -dy / len * gap;
            float py = dx / len * gap;

            main.projectiles.add(new Projectile((int)(xLocation + px), (int)(yLocation + py), enemy, finalDamage, this));
            main.projectiles.add(new Projectile((int)(xLocation - px), (int)(yLocation - py), enemy, finalDamage, this));
        }
        else
        {
            main.projectiles.add(new Projectile(xLocation, yLocation, enemy, finalDamage, this));
        }
    }

    // Build & abilities
    public void built() { isBuilt = true; }
    public void resetTower() { isBuilt = false; ability1 = false; ability2 = false; }
    public void getAbility1() { ability1 = true; }
    public void getAbility2() { ability2 = true; }
    public boolean isBuilt() { return isBuilt; }

    // Tower info
    public String getName() { return towerName; }
    public float getDamage() { return damage; }
    public int getRange() { return range; }
    public float getFireRate() { return fireRate * (1 + 0.2f * buffAmount);}
    public int getCost() { return towerCost; }
    public int getTowerIndex() { return towerIndex; }
    public int getCurrentLevel() { return currentLevel; }
    public int getBuffAmount() { return buffAmount; }

    public boolean onRightSide() { return xLocation >= 400; }

    // Buff management
    public void receiveBuff(Tower buffTower)
    {
        buffAmount++;
        buffTowers.add(buffTower);
    }

    public void removeBuff(Tower buffTower)
    {
        if (buffTowers.remove(buffTower))
            buffAmount--;
    }

    // Upgrade tower stats
    public void upgradeTower(float newDamage, int newRange, float newFireRate)
    {
        currentLevel++;
        this.damage = newDamage;
        this.range = newRange;
        this.fireRate = newFireRate;
    }

    // Check if one of buffing towers has ability 1
    public boolean checkAbility1BuffedTowers()
    {
        for (Tower t : buffTowers)
        {
            if (t.hasAbility1())
            {
                return true;
            }
        }

        return false;
    }

    // Check if one of buffing towers has ability 2
    private boolean checkAbility2BuffedTowers()
    {
        for (Tower t : buffTowers)
        {
            if (t.hasAbility2())
            {
                return true;
            }
        }

        return false;
    }
}

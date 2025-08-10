import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Write a description of class Projectile here.
 *
 * @author Julius Gauldie
 * @version 10/08/25
 */
public class Projectile
{
    // instance variables 
    public int xLocation, yLocation;
    float damage;
    float speed = 15f;
    Enemy target;
    private int projectileIndex; // 1 - Normal, 2 - Area, 3 - Piercing

    boolean active = true;
    
    String projectileImageName = ""; // Image file name for the projectile

    // Image
    ImageIcon image;

    // Pierce projectile
    private boolean hitEnemy = false;
    private int pierceCount = 0;
    private float dirX, dirY;
    public ArrayList<Enemy> hitEnemies = new ArrayList<>();

    public Tower parentTower;

    // Tower 1 - Ability 1
    private boolean rebounded;
    boolean hasRebounded() { return rebounded; }
    
    /**
     * Constructor for objects of class Projectile
     */
    public Projectile(int x, int y, Enemy target, float damage, Tower parentTower)
    {   
        this.parentTower = parentTower;
        this.projectileIndex = parentTower.getTowerIndex();

        projectileImageName = "resources/assets/projectile" + projectileIndex + ".png";
        image = new ImageIcon(projectileImageName);

        this.xLocation = x;
        this.yLocation = y;
        
        this.target = target;
        this.damage = damage;
  
        if (projectileIndex == 3) // Piercing projectile - Normal Tower 3
        {
            if (parentTower.hasAbility1()) // Tower 3 - Ability 1: Extra enemy pierced
                pierceCount = 3;
            else   
                pierceCount = 2;
        }
    }
    
    public void update()
    {
        if (!target.isAlive() || !active || hitEnemy)
        {
            if (projectileIndex != 3 || pierceCount <= 0) // Area damage
            {
                active = false;
            
                return;
            }
            else
            {
                xLocation += dirX * speed;
                yLocation += dirY * speed;

                return;
            }
        }
        
        float dx = target.xLocation - xLocation;
        float dy = target.yLocation - yLocation;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Set projectile direction for piercing projectiles
        if (dirX == 0 && dirY == 0)
        {
            dirX = dx / distance;
            dirY = dy / distance;
        }
        
        if (distance < 10f) // Piercing projectile
        {
            if (projectileIndex != 2 && projectileIndex != 3) // Area damage
            {
                if (projectileIndex == 4)
                    target.weakenEnemy(parentTower.hasAbility2() ? 5000f : 3000f);

                target.hit(damage);
            }
            
            if (projectileIndex != 3) // If not piercing projectile
                active = false;

            hitEnemy = true;
            
            return;
        }
        
        xLocation += (dx / distance) * speed;
        yLocation += (dy / distance) * speed;
    }
    
    public boolean isActive()
    {
        return active;
    }

    public int getProjectileIndex()
    {
        return projectileIndex;
    }

    public int getPierceCount()
    {
        return pierceCount;
    }

    public void decreasePierceCount()
    {
        pierceCount--;
    }

    public void updateTarget(Enemy newEnemy) // Tower 1 - Ability 2
    {
        rebounded = true;

        target = newEnemy;
        hitEnemy = false;
        active = true;
    }
}

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Write a description of class Projectile here.
 *
 * @author Julius Gauldie
 * @version 07/08/25
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
    private boolean hitTarget = false;
    private int pierceCount = 0;
    private float dirX, dirY;
    public ArrayList<Enemy> hitEnemies = new ArrayList<>();
    
    /**
     * Constructor for objects of class Projectile
     */
    public Projectile(int x, int y, Enemy target, float damage, int projectileIndex)
    {   
        this.projectileIndex = projectileIndex;

        projectileImageName = "resources/assets/projectile" + projectileIndex + ".png";
        image = new ImageIcon(projectileImageName);

        this.xLocation = x;
        this.yLocation = y;
        
        this.target = target;
        this.damage = damage;

        if (projectileIndex == 3) // Piercing projectile
        {
            pierceCount = 2; 
        }
    }
    
    public void update()
    {

        if (!target.isAlive() || !active || hitTarget)
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
            hitTarget = true;

            if (projectileIndex != 2 && projectileIndex != 3) // Area damage
                target.hit(damage);

            if (projectileIndex != 3)
                active = false;
            
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
}

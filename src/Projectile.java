import javax.swing.ImageIcon;

/**
 * Write a description of class Projectile here.
 *
 * @author Julius Gauldie
 * @version 03/08/25
 */
public class Projectile
{
    // instance variables 
    public int xLocation, yLocation;
    float damage;
    float speed = 15f;
    Enemy target;
    private int projectileIndex; // 1 - Normal, 2 - Area
    private int pierceCount = 0;
    
    boolean active = true;
    
    String projectileImageName = ""; // Image file name for the projectile

    // Image
    ImageIcon image;
    
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
    }
    
    public void update()
    {
        if (!target.isAlive() || !active)
        {
            active = false;
            
            return;
        }
        
        float dx = target.xLocation - xLocation;
        float dy = target.yLocation - yLocation;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance < 10f)
        {
            if (projectileIndex != 2) // Area damage
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
}

import javax.swing.ImageIcon;

/**
 * Write a description of class Projectile here.
 *
 * @author Julius Gauldie
 * @version 26/06/25
 */
public class Projectile
{
    // instance variables 
    public int xLocation, yLocation;
    int damage;
    float speed = 10f;
    Enemy target;
    
    boolean active = true;
    
    // Images
    ImageIcon image = new ImageIcon("../assets/bullet.png");
    
    /**
     * Constructor for objects of class Projectile
     */
    public Projectile(int x, int y, Enemy target, int damage)
    {
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
        
        if (distance < 3f)
        {
            target.hit(damage);
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
}

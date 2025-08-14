
/**
 * Represents a projectile fired by a tower towards an enemy.
 * 
 * Author: Julius Gauldie
 * Version: 14/08/25
 */
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Projectile 
{
    // Position & movement
    public int xLocation, yLocation; // Current projectile position
    private float dirX = 0, dirY = 0; // Direction for piercing projectiles
    float speed = 15f; // Projectile movement speed

    // Damage & target
    float damage;
    Enemy target; // Target enemy
    boolean active = true; 

    // Projectile type (1 = Normal, 2 = Area, 3 = Piercing)
    private int projectileIndex;

    // Piercing & tracking
    private boolean hitEnemy = false;
    private int pierceCount = 0;
    public ArrayList<Enemy> hitEnemies = new ArrayList<>();

    // Image
    String projectileImageName = ""; // File path for image
    ImageIcon image;

    // Parent tower
    public Tower parentTower;

    // Tower 1 - Ability 1
    private boolean rebounded; // Has projectile rebounded
    boolean hasRebounded() {
        return rebounded;
    }

    /**
     * Constructor for Projectile.
     * 
     * @param x           Initial x position
     * @param y           Initial y position
     * @param target      Target enemy
     * @param damage      Damage dealt
     * @param parentTower Tower that fired this projectile
     */
    public Projectile(int x, int y, Enemy target, float damage, Tower parentTower) 
    {
        this.parentTower = parentTower;
        this.projectileIndex = parentTower.getTowerIndex();

        // Load projectile image based on tower type
        projectileImageName = "resources/assets/projectile" + projectileIndex + ".png";
        image = new ImageIcon(projectileImageName);

        this.xLocation = x;
        this.yLocation = y;

        this.target = target;
        this.damage = damage;

        // Piercing projectile setup
        if (projectileIndex == 3) 
        {
            pierceCount = parentTower.hasAbility1() ? 3 : 2;
        }

        if (projectileIndex == 1 && parentTower.getBuffAmount() > 0 || projectileIndex == 3 && parentTower.getBuffAmount() > 0)
            pierceCount += parentTower.getBuffAmount();
    }

    /**
     * Updates the projectile's position and applies damage if it reaches its
     * target.
     */
    public void update() 
    {
        // If target is dead, projectile inactive, or already hit enemy
        if (!target.isAlive() || !active || hitEnemy) 
        {
            if (projectileIndex != 3 || pierceCount <= 0)
            {
                active = false;
                return;
            } 
            else 
            {
                // Move piercing projectile forward
                xLocation += dirX * speed;
                yLocation += dirY * speed;
                return;
            }
        }   

        // Calculate direction towards target
        float dx = target.xLocation - xLocation;
        float dy = target.yLocation - yLocation;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Set direction for piercing projectiles
        if (dirX == 0 && dirY == 0) 
        {
            dirX = dx / distance;
            dirY = dy / distance;
        }

        if (distance < 10f) // Projectile reached target
        {
            if (projectileIndex != 2 && projectileIndex != 3) // Area damage
            {
                if (projectileIndex == 4)
                    target.weakenEnemy(parentTower.hasAbility2() ? 5000f : 3000f);

                target.hit(damage, parentTower);
            }
            
            if (projectileIndex != 3) // If not piercing projectile
                active = false;

            hitEnemy = true;
            
            return;
        }

        // Move projectile towards target
        xLocation += (dx / distance) * speed;
        yLocation += (dy / distance) * speed;
    }

    // Returns if active
    public boolean isActive() {
        return active;
    }

    // Returns projectile type
    public int getProjectileIndex() {
        return projectileIndex;
    }

    // Returns pierce count
    public int getPierceCount() {
        return pierceCount;
    }

    // Decreases pierce count by 1
    public void decreasePierceCount() {
        pierceCount--;
    }

    /**
     * Reassigns a new target 
     * 
     * @param newEnemy New target enemy
     */
    public void updateTarget(Enemy newEnemy) {
        rebounded = true;
        target = newEnemy;
        hitEnemy = false;
        active = true;
    }
}

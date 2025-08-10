
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 10/08/25
 */
import java.util.*;
import java.awt.Point;
import javax.swing.ImageIcon;

public class Enemy
{
    public int xLocation, yLocation;
    public double xPos, yPos;
    public int currentWaypoint = 0;
    private java.util.List<Point> path;

    private float speed = 1;
    private float startingHealth;
    private float currentHealth;
    private int damage;
    private int armor;

    private int enemyType;

    // Time
    private long lastSlowedTime = System.currentTimeMillis() - 3000; // 10 Seconds before spawn
    private long lastWeakenedTime = System.currentTimeMillis() - 4000; // 10 Seconds before spawn
    private long lastTunnelTime = System.currentTimeMillis() - TUNNEL_CYCLE_MS; // 10 Seconds before spawn
    private long lastParasiteTime = System.currentTimeMillis() - PARASITE_CYCLE_MS;

    // Tunnel - Enemy type 3
    private static final long TUNNEL_CYCLE_MS = 5000; // 5 seconds
    private static final long TUNNEL_DURATION_MS = 1500; // 1.5 second

    // Parasite spawn - Enemy type 5
    private static final long PARASITE_CYCLE_MS = 8000; // 8 seconds

    // Weakness
    private float currentWeaknessTime = 3000f;

    boolean isAlive() { return currentHealth > 0; }
    
    boolean madeToEnd() { return currentWaypoint >= path.size(); }

    boolean isSlowed() { return System.currentTimeMillis() - lastSlowedTime <= 4000f; } // 4 Seconds of slowing

    boolean isWeakened() { return System.currentTimeMillis() - lastWeakenedTime <= currentWeaknessTime; } // 3 Seconds of weakness

    boolean above80PercentHealth() { return currentHealth > (0.8 * startingHealth); }

    // Images
    ImageIcon image;

    /**
     * Constructor for objects of class Enemies
     */
    public Enemy(List<Point> path, String EnemyImageFileName, int enemyType, float health, float speed, int damage, int armor)
    {
        this.enemyType = enemyType;
        this.startingHealth = health;
        currentHealth = startingHealth;
        this.speed = speed;
        this.path = path;
        this.damage = damage;
        this.armor = armor;

        this.image = new ImageIcon(EnemyImageFileName);

        this.xPos = path.get(0).x;
        this.yPos = path.get(0).y;
        this.xLocation = (int)Math.round(xPos);
        this.yLocation = (int)Math.round(yPos);
    }

    public void update() 
    {
        if (currentWaypoint >= path.size()) return;

        Point target = path.get(currentWaypoint);

        double dx = target.x - xPos;
        double dy = target.y - yPos;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) 
        {
            // Snap to waypoint and go to next
            xPos = target.x;
            yPos = target.y;
            currentWaypoint++;
        }
        else 
        {
            // Move toward the target
            xPos += dx / distance * (!isSlowed() ? speed : speed * 0.8f);
            yPos += dy / distance * (!isSlowed() ? speed : speed * 0.8f);
        }

        // Update locations in ints
        xLocation = (int)Math.round(xPos);
        yLocation = (int)Math.round(yPos);
    }

    public void hit(float damage)
    {
        float damageDealt = damage;

        if (isWeakened())
            damageDealt *= 1.2f;

        damageDealt -= armor; // Damage done is damage - enemy armor, so 15 damage against enemy with 5 armor does 10 damage

        if (damageDealt < 0)
            damageDealt = 0;

        currentHealth -= damageDealt; // Do damage
    }

    public int enemyType()
    {
        return enemyType;
    }

    public int getDamage()
    {
        return damage;
    }

    public float getHealth()
    {        
        return currentHealth;
    }

    public float getStartingHealth()
    {
        return startingHealth;
    }

    public void slowEnemy()
    {
        lastSlowedTime = System.currentTimeMillis();
    }

    public void weakenEnemy(float weakenTime)
    {
        currentWeaknessTime = weakenTime;

        lastWeakenedTime = System.currentTimeMillis();
    }

     // Returns true if this enemy is currently tunneled (untargetable for 1s every 5s)
    public boolean isTunneled() 
    {
        if (enemyType != 3) 
            return false;
        
        long now = System.currentTimeMillis();
        long cycle = (now - lastTunnelTime) % TUNNEL_CYCLE_MS;

        return cycle < TUNNEL_DURATION_MS;
    }

    public boolean parasiteSpawnable() 
    {
        if (enemyType != 5)
            return false;

        long now = System.currentTimeMillis();

        // Only true for the first tick of the 8s cycle
        return (now - lastParasiteTime) >= PARASITE_CYCLE_MS;
    }

    public void resetParasiteSpawnTimer() {
        lastParasiteTime = System.currentTimeMillis();
    }
}

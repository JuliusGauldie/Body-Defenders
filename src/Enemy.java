
/**
 * Represents an enemy that moves along a path in the tower defense game.
 * Each enemy has health, speed, armor, damage
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.util.*;
import java.awt.Point;
import javax.swing.ImageIcon;

public class Enemy 
{
    // Position
    public float xLocation, yLocation;

    // Tracks current waypoint in the path
    public int currentWaypoint = 0;

    // Path the enemy follows
    private List<Point> path;

    // Enemy stats
    private float speed = 1; // Movement speed
    private float startingHealth; // Max health
    private float currentHealth; // Current health
    private float damage; // Damage to the player if enemy reaches end
    private int armor; // Reduces damage taken
    private int enemyType; // Enemy type identifier

    // Timing for status effects and abilities
    private long lastSlowedTime = System.currentTimeMillis() - 3000; // 4s slow timer
    private long lastWeakenedTime = System.currentTimeMillis() - 4000; // Weakness timer
    private long lastTunnelTime = System.currentTimeMillis() - TUNNEL_CYCLE_MS; // Tunnel timer
    private long lastParasiteTime = System.currentTimeMillis() - PARASITE_CYCLE_MS; // Parasite spawn timer

    // Constants for abilities
    private static final long TUNNEL_CYCLE_MS = 5000; // Tunnel cycle: 5s
    private static final long TUNNEL_DURATION_MS = 1500; // Tunnel active duration: 1.5s
    private static final long PARASITE_CYCLE_MS = 8000; // Parasite spawn cycle: 8s

    // Weakness duration in milliseconds
    private float currentWeaknessTime = 3000f;

    // Enemy image
    ImageIcon image;

    // For tower ability
    private Tower killedByTower;

    /**
     * Constructor for Enemy.
     * Initializes the enemy with given stats and starting position.
     * 
     * @param path               Path for the enemy to follow
     * @param EnemyImageFileName Image file for the enemy
     * @param enemyType          Type of the enemy (affects abilities)
     * @param health             Starting health
     * @param speed              Movement speed
     * @param damage             Damage to player if it reaches the end
     * @param armor              Reduces damage taken
     */
    public Enemy(List<Point> path, String EnemyImageFileName, int enemyType, float health, float speed, float damage,
            int armor) {
        this.enemyType = enemyType;
        this.startingHealth = health;
        this.currentHealth = startingHealth;
        this.speed = speed;
        this.path = path;
        this.damage = damage;
        this.armor = armor;

        this.image = new ImageIcon(EnemyImageFileName);

        // Set starting position at first waypoint
        this.xLocation = path.get(0).x;
        this.yLocation = path.get(0).y;
    }

    /**
     * Updates enemy position along the path.
     * Accounts for slow effects.
     */
    public void update() {
        if (currentWaypoint >= path.size())
            return; // Already at the end

        Point target = path.get(currentWaypoint);

        double dx = target.x - xLocation;
        double dy = target.y - yLocation;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            // Snap to waypoint and move to next
            xLocation = target.x;
            yLocation = target.y;
            currentWaypoint++;
        } else {
            // Move toward the target
            float appliedSpeed = isSlowed() ? speed * 0.8f : speed;
            xLocation += dx / distance * appliedSpeed;
            yLocation += dy / distance * appliedSpeed;
        }
    }

    /**
     * Applies damage to the enemy, accounting for armor and weakness.
     * 
     * @param damage Amount of damage being dealt
     */
    public void hit(float damage, Tower tower) {
        float damageDealt = damage;

        // Increase damage if enemy is weakened
        if (isWeakened())
            damageDealt *= 1.2f;

        damageDealt -= armor; // Armor reduces damage

        if (damageDealt < 0) // Check damage on enemy is never negative
            damageDealt = 0;

        currentHealth -= damageDealt;

        if (currentHealth <= 0)
            killedByTower = tower;
    }

    /** Getters and state checks **/

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public boolean madeToEnd() {
        return currentWaypoint >= path.size();
    }

    public boolean isSlowed() {
        return System.currentTimeMillis() - lastSlowedTime <= 4000f;
    }

    public boolean isWeakened() {
        return System.currentTimeMillis() - lastWeakenedTime <= currentWeaknessTime;
    }

    public boolean above80PercentHealth() {
        return currentHealth > (0.8 * startingHealth);
    }

    public int enemyType() {
        return enemyType;
    }

    public float getDamage() {
        return damage;
    }

    public float getHealth() {
        return currentHealth;
    }

    public float getStartingHealth() {
        return startingHealth;
    }

    public void slowEnemy() {
        lastSlowedTime = System.currentTimeMillis();
    }

    public void weakenEnemy(float weakenTime) {
        currentWeaknessTime = weakenTime;
        lastWeakenedTime = System.currentTimeMillis();
    }

    /**
     * Returns true if enemy type 3 is currently tunneled
     */
    public boolean isTunneled() {
        if (enemyType != 3)
            return false;

        long now = System.currentTimeMillis();
        long cycle = (now - lastTunnelTime) % TUNNEL_CYCLE_MS;
        return cycle < TUNNEL_DURATION_MS;
    }

    /**
     * Returns true if enemy type 5 can spawn parasites
     */
    public boolean parasiteSpawnable() {
        if (enemyType != 5)
            return false;

        long now = System.currentTimeMillis();
        return (now - lastParasiteTime) >= PARASITE_CYCLE_MS;
    }

    public void resetParasiteSpawnTimer() {
        lastParasiteTime = System.currentTimeMillis();
    }

    public Tower getKilledByTower()
    {
        return killedByTower;
    }
}

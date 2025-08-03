
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 03/08/25
 */
import java.util.*;
import java.awt.Point;
import javax.swing.ImageIcon;

public class Enemy
{
    public int xLocation, yLocation;
    private double xPos, yPos;
    private int currentWaypoint = 0;
    private java.util.List<Point> path;

    private float speed = 1;
    private float health;
    private int damage;

    private int enemyType;

    boolean isAlive() { return health > 0; }
    
    boolean madeToEnd() { return currentWaypoint >= path.size(); }

    // Images
    ImageIcon image;

    /**
     * Constructor for objects of class Enemies
     */
    public Enemy(List<Point> path, String EnemyImageFileName, int enemyType, float health, float speed, int damage)
    {
        this.enemyType = enemyType;
        this.health = health;
        this.speed = speed;
        this.path = path;
        this.damage = damage;

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

        if (distance < speed) {
            // Snap to waypoint and go to next
            xPos = target.x;
            yPos = target.y;
            currentWaypoint++;
        } else {
            // Move toward the target
            xPos += dx / distance * speed;
            yPos += dy / distance * speed;
        }
        // Update locations in ints
        xLocation = (int)Math.round(xPos);
        yLocation = (int)Math.round(yPos);
    }

    public void hit(float damage)
    {
        health -= damage;
    }

    public int enemyType()
    {
        return enemyType;
    }

    public int getDamage()
    {
        return damage;
    }
}

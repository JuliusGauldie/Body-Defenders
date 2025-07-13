
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 14/07/25
 */
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.Point;
import javax.swing.ImageIcon;

public class Enemy
{
    public int xLocation, yLocation;
    private int currentWaypoint = 0;
    private java.util.List<Point> path;

    private int speed = 1;
    private int health = 100;
    private int damage = 0;

    boolean isAlive() { return health > 0; }
    
    boolean madeToEnd() { return currentWaypoint >= path.size(); }

    // Images
    ImageIcon image = new ImageIcon("../assets/enemy.png");

    /**
     * Constructor for objects of class Enemies
     */
    public Enemy(List<Point> path)
    {
        this.path = path;

        this.xLocation = path.get(0).x;
        this.yLocation = path.get(0).y;
    }

    public void update() 
    {
        if (currentWaypoint >= path.size()) return;

        Point target = path.get(currentWaypoint);

        double dx = target.x - xLocation;
        double dy = target.y - yLocation;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            // Snap to waypoint and go to next
            xLocation = target.x;
            yLocation = target.y;
            currentWaypoint++;
        } else {
            // Move toward the target
            xLocation += dx / distance * speed;
            yLocation += dy / distance * speed;
        }
    }

    public void hit(int damage)
    {
        health -= damage;
    }
}

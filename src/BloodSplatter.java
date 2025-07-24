
/**
 * Write a description of class BloodSplatter here.
 *
 * @author Julius Gauldie
 * @version 25/07/25
 */
import javax.swing.*;

public class BloodSplatter
{
    // instance variables 
    public int x, y;
    public ImageIcon bloodSplatter = new ImageIcon("../assets/BloodSplatter.png");

    /**
     * Constructor for objects of class BloodSplatter
     */
    public BloodSplatter(int xLocation, int yLocation)
    {
        this.x = xLocation;
        this.y = yLocation;
    }
}
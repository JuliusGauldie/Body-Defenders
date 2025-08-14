
/**
 * Represents a visual blood splatter effect on the game panel.
 * Each instance stores its position and the image to display.
 *
 * @author Julius Gauldie
 * @version 14/08/25
 */
import javax.swing.*;

public class BloodSplatter 
{
    // X and Y position of the blood splatter on the game panel
    public float x, y;

    // Image representing the blood splatter
    public ImageIcon bloodSplatter = new ImageIcon("resources/assets/cellResidue.png");

    /**
     * Constructor for BloodSplatter.
     *
     * @param xLocation X-coordinate for blood splatter
     * @param yLocation Y-coordinate for blood splatter
     */
    public BloodSplatter(float xLocation, float yLocation) {
        this.x = xLocation;
        this.y = yLocation;
    }
}

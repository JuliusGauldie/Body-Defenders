/**
 * Represents a clickable menu button with a rectangular area.
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
public class MenuButton 
{
    // Instance variables
    public int x; // Top-left X position of the button
    public int y; // Top-left Y position of the button
    public int width; // Button width in pixels
    public int height; // Button height in pixels
    public Runnable onClick; // Action when button is clicked

    /**
     * Constructor - creates a MenuButton.
     * 
     * @param x       The top-left X position of the button.
     * @param y       The top-left Y position of the button.
     * @param width   The width of the button in pixels.
     * @param height  The height of the button in pixels.
     * @param onClick The action to execute when the button is clicked.
     */
    public MenuButton(int x, int y, int width, int height, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onClick = onClick;
    }

    /**
     * Checks if the given mouse coordinates are inside this button.
     * 
     * @param mx Mouse X position.
     * @param my Mouse Y position.
     * @return True if the mouse click is inside the button area, otherwise false.
     */
    public boolean isClicked(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}

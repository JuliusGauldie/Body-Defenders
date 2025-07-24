/**
 * Write a description of class MenuButton here.
 *
 * @author Julius Gauldie
 * @version 25/07/25
 */
public class MenuButton
{
    // instance variables 
    int x, y, width, height;
    Runnable onClick;

    /**
     * Constructor for objects of class MenuButton
     */
    public MenuButton(int x, int y, int width, int height, Runnable onClick)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onClick = onClick;
    }

    public boolean isClicked(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}
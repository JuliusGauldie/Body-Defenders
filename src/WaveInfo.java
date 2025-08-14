/**
 * WaveInfo stores the number of enemies in a wave
 * 
 * Author: Julius Gauldie
 * Version: 14/08/25
 */
public class WaveInfo 
{
    // Number of enemies of each type
    private int enemy1Count;
    private int enemy2Count;
    private int enemy3Count;
    private int enemy4Count;
    private int enemy5Count;

    /**
     * Constructor to initialize enemy counts for this wave.
     */
    public WaveInfo(int enemy1Count, int enemy2Count, int enemy3Count, int enemy4Count, int enemy5Count)
    {
        this.enemy1Count = enemy1Count;
        this.enemy2Count = enemy2Count;
        this.enemy3Count = enemy3Count;
        this.enemy4Count = enemy4Count;
        this.enemy5Count = enemy5Count;
    }

    // Get enemies in wave
    public int getEnemy1Count() { return enemy1Count; }       
    public int getEnemy2Count() { return enemy2Count; }   
    public int getEnemy3Count() { return enemy3Count; }
    public int getEnemy4Count() { return enemy4Count; }   
    public int getEnemy5Count() { return enemy5Count; }
}

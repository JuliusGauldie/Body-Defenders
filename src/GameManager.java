
/**
 * Manages the waves of enemies in the game.
 * Generates all waves and provides access to individual wave information.
 * 
 * @author Julius Gauldie
 * @version 14/08/25
 */
import java.util.*;

public class GameManager 
{
    // Array containing all waves
    private ArrayList<WaveInfo> waves = new ArrayList<>();

    /**
     * Constructor initializes the wave list by generating all waves
     */
    public GameManager() {
        generateWaves();
    }

    /**
     * Generates 15 waves with increasing difficulty and variety of enemies
     */
    private void generateWaves() {
        for (int i = 1; i <= 15; i++) {
            if (i <= 3) {
                // Early waves: Spike Virus only, small numbers
                waves.add(new WaveInfo(5 + i * 3, 0, 0, 0, 0));
            } else if (i <= 5) {
                // Introduce Shell Bacteria gradually
                waves.add(new WaveInfo(8 + i * 4, 2 + i, 0, 0, 0));
            } else if (i <= 7) {
                // Add Toxin Worm alongside Spike Virus and Shell Bacteria
                waves.add(new WaveInfo(10 + i * 4, 3 + i, 1 + (i - 5), 0, 0));
            } else if (i <= 10) {
                // Introduce Mutation Swarm and mix enemies
                waves.add(new WaveInfo(12 + i * 4, 5 + i, 3 + (i - 7), 4 + (i - 7), 0));
            } else if (i == 11 || i == 12) {
                // Mini-boss waves: Parasite Queen + strong minions
                waves.add(new WaveInfo(20 + i * 4, 10 + i, 6 + (i - 10), 6 + (i - 10), i - 10));
            } else if (i == 13 || i == 14) {
                // Stronger mixed waves: more enemies and mutations
                waves.add(new WaveInfo(25 + i * 4, 12 + i, 8 + (i - 12), 8 + (i - 12), (i - 12) * 2));
            } else if (i == 15) {
                // Final boss wave: maximum enemies + Parasite Queen boss
                waves.add(new WaveInfo(35 + i * 4, 15 + i, 10 + (i - 14), 10 + (i - 14), 3));
            }
        }
    }

    /**
     * Returns the WaveInfo for a given wave number
     * 
     * @param waveNumber The wave number
     */
    public WaveInfo getWave(int waveNumber) {
        return waves.get(waveNumber - 1);
    }

    /**
     * Returns the total number of waves
     */
    public int getTotalWaves() {
        return waves.size();
    }
}

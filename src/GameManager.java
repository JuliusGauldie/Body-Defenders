
/**
 * Panel showing main game
 *
 * @author Julius Gauldie
 * @version 03/08/25
 */
import java.util.*;

public class GameManager 
{
    private List<WaveInfo> waves = new ArrayList<>();

    public GameManager() 
    {
        generateWaves();
    }

    private void generateWaves() 
    {
        for (int i = 0; i <= 50; i++)
        {
             // Early waves: simple enemies
            if (i <= 5) 
            {
                waves.add(new WaveInfo(5 + i * 2, 0, 0, 0, 0));
            }
            // Introduce Shell Bacteria (tanky) after wave 5
            else if (i <= 10) 
            {
                waves.add(new WaveInfo(8 + i * 2, 2 + i, 0, 0, 0));
            }
            // Introduce Toxin Worm after wave 10
            else if (i <= 20) 
            {
                waves.add(new WaveInfo(10 + i * 2, 3 + i, 1 + i / 2, 0, 0));
            }
            // Introduce Mutation Swarm after wave 20
            else if (i <= 30) 
            {
                waves.add(new WaveInfo(12 + i * 2, 5 + i, 3 + i / 2, 4 + i, 0));
            }
            // Late-game: high density & tanky enemies
            else if (i <= 40) {
                waves.add(new WaveInfo(15 + i * 2, 6 + i, 5 + i / 2, 5 + i, 0));
            }
            // Final 10 waves: bosses appear
            else 
            {
                waves.add(new WaveInfo(18 + i * 2, 8 + i, 6 + i / 2, 6 + i, i / 5));
            }
        }
    }

    public WaveInfo getWave(int waveNumber)
    {
        if (waveNumber < 1 || waveNumber > waves.size()) {
            throw new IndexOutOfBoundsException("Wave number out of range: " + waveNumber);
        }
        return waves.get(waveNumber - 1);
    }

    public int getTotalWaves() 
    {
        return waves.size();
    }
}

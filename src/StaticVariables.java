
/**
 * Reads CSV files for enemy, tower, and map values
 * 
 * Author: Julius Gauldie
 * Version: 14/08/25
 * 
 */
import java.util.*;
import java.io.*;

public class StaticVariables
{
    // File paths
    private final String TOWERFILENAME = "resources/data/TowerVariables.txt";
    private final String ENEMYFILENAME = "resources/data/EnemyVariables.txt";
    private final String MAPFILENAME = "resources/data/MapVariables.txt";
    private final String TOWERPLACEMENTFILENAME = "resources/data/TowerBuildSpots.txt";

    private File towerVariables = new File(TOWERFILENAME);
    private File enemyVariables = new File(ENEMYFILENAME);
    private File mapVariables = new File(MAPFILENAME);
    private File towerPlacementVariables = new File(TOWERPLACEMENTFILENAME);

    // Tower constants
    private final int AMOUNTOFTOWERS = 5;
    private String[][] baseStats = new String[AMOUNTOFTOWERS][];      // [tower][base values]
    private String[][] upgrade1Stats = new String[AMOUNTOFTOWERS][];  // [tower][upgrade 1 values]
    private String[][] upgrade2Stats = new String[AMOUNTOFTOWERS][];  // [tower][upgrade 2 values]
    private String[][] ability1Stats = new String[AMOUNTOFTOWERS][];  // [tower][ability 1 values]
    private String[][] ability2Stats = new String[AMOUNTOFTOWERS][];  // [tower][ability 2 values]

    // Enemy constants
    private final int AMOUNTOFENEMIES = 5;
    private final int ENEMYVALUESPERLINE = 7;
    private String[][] enemyVariableElements = new String[AMOUNTOFENEMIES][ENEMYVALUESPERLINE];

    // Map constants
    private final int AMOUNTOFPOINTS = 17;
    private int[][] mapVariableElements = new int[AMOUNTOFPOINTS][2];     

    // Tower placement constants
    private final int AMOUNTOFTOWERSPOTS = 9;
    private int[][] towerPlacements = new int[AMOUNTOFTOWERSPOTS][2];     

    /**
     * Constructor reads all CSV files on initialization.
     */
    public StaticVariables()
    {
        readTowerVariables();
        readEnemyVariables();
        readMapVariables();
        readTowerPlacementVariables();
    }

    // Reads tower stats, upgrades, and abilities from tower CSV file
    private void readTowerVariables() 
    {
        int lineCount = 0;
        try (Scanner reader = new Scanner(towerVariables)) 
        {
            while (reader.hasNextLine() && lineCount < AMOUNTOFTOWERS) 
            {
                String line = reader.nextLine();
                String[] sections = line.split("\\|"); // Split into base|upgrade1|upgrade2|ability1|ability2

                baseStats[lineCount] = sections[0].split(",");
                upgrade1Stats[lineCount] = sections[1].split(",");
                upgrade2Stats[lineCount] = sections[2].split(",");
                ability1Stats[lineCount] = sections[3].split(",");
                ability2Stats[lineCount] = sections[4].split(",");

                lineCount++;
            }
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR - CAN'T READ TOWER FILE");
        }
    }

    // Reads enemy stats from enemy CSV file 
    private void readEnemyVariables()
    {
        int lineCount = 0;
        try (Scanner reader = new Scanner(enemyVariables)) 
        {
            while (reader.hasNextLine() && lineCount < AMOUNTOFENEMIES)
            {
                String[] values = reader.nextLine().split(",");
                System.arraycopy(values, 0, enemyVariableElements[lineCount], 0, values.length);
                lineCount++;
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR - CAN'T READ ENEMY FILE");
        }
    }

    // Reads map coordinates from map CSV file
    private void readMapVariables()
    {
        int lineCount = 0;
        try (Scanner reader = new Scanner(mapVariables)) 
        {
            while (reader.hasNextLine() && lineCount < AMOUNTOFPOINTS)
            {
                String[] values = reader.nextLine().split(",");
                for (int i = 0; i < values.length; i++)
                    mapVariableElements[lineCount][i] = Integer.parseInt(values[i]);
                lineCount++;
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR - CAN'T READ MAP FILE");
        }
    }

    // Reads tower build spot coordinates from CSV file 
    private void readTowerPlacementVariables()
    {
        int lineCount = 0;
        try (Scanner reader = new Scanner(towerPlacementVariables)) 
        {
            while (reader.hasNextLine() && lineCount < AMOUNTOFTOWERSPOTS)
            {
                String[] values = reader.nextLine().split(",");
                for (int i = 0; i < values.length; i++)
                    towerPlacements[lineCount][i] = Integer.parseInt(values[i]);
                lineCount++;
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR - CAN'T READ TOWER PLACEMENT FILE");
        }
    }

    // Base Tower stats
    public String getTowerName(int tower) { return baseStats[tower - 1][0]; }
    public float getTowerDamage(int tower) { return Float.parseFloat(baseStats[tower - 1][1]); }
    public int getTowerRange(int tower) { return Integer.parseInt(baseStats[tower - 1][2]); }
    public float getTowerFirerate(int tower) { return Float.parseFloat(baseStats[tower - 1][3]); }
    public int getTowerCost(int tower) { return Integer.parseInt(baseStats[tower - 1][4]); }
    public String getTowerImage(int tower) { return baseStats[tower - 1][5]; }
    public String getTowerAbility(int tower) { return baseStats[tower - 1][6]; }

    // Upgrade 1
    public float getTowerDamageUpgrade1(int tower) { return Float.parseFloat(upgrade1Stats[tower - 1][0]); }
    public int getTowerRangeUpgrade1(int tower) { return Integer.parseInt(upgrade1Stats[tower - 1][1]); }
    public float getTowerFirerateUpgrade1(int tower) { return Float.parseFloat(upgrade1Stats[tower - 1][2]); }
    public int getTowerCostUpgrade1(int tower) { return Integer.parseInt(upgrade1Stats[tower - 1][3]); }

    // Upgrade 2
    public float getTowerDamageUpgrade2(int tower) { return Float.parseFloat(upgrade2Stats[tower - 1][0]); }
    public int getTowerRangeUpgrade2(int tower) { return Integer.parseInt(upgrade2Stats[tower - 1][1]); }
    public float getTowerFirerateUpgrade2(int tower) { return Float.parseFloat(upgrade2Stats[tower - 1][2]); }
    public int getTowerCostUpgrade2(int tower) { return Integer.parseInt(upgrade2Stats[tower - 1][3]); }

    // Ability 1
    public String getAbility1Info(int tower) { return ability1Stats[tower - 1][0]; }
    public String getAbility1Description(int tower) { return ability1Stats[tower - 1][1]; }
    public int getAbility1Cost(int tower) { return Integer.parseInt(ability1Stats[tower - 1][2]); }

    // Ability 2
    public String getAbility2Info(int tower) { return ability2Stats[tower - 1][0]; }
    public String getAbility2Description(int tower) { return ability2Stats[tower - 1][1]; }
    public int getAbility2Cost(int tower) { return Integer.parseInt(ability2Stats[tower - 1][2]); }

    // Map & tower placement 
    public int getCoord(int point, int coordinateNumber) { return mapVariableElements[point][coordinateNumber]; }
    public int getTCoord(int point, int coordinateNumber) { return towerPlacements[point][coordinateNumber]; }
    public int getAmountOfMapPoints() { return AMOUNTOFPOINTS; }
    public int getAmountOfTowerSpots() { return AMOUNTOFTOWERSPOTS; }

    // Enemies
    public String getEnemyName(int enemyIndex) { return enemyVariableElements[enemyIndex - 1][0]; }
    public float getEnemyHealth(int enemyIndex) { return Float.parseFloat(enemyVariableElements[enemyIndex - 1][1]); }
    public int getEnemyDamage(int enemyIndex) { return Integer.parseInt(enemyVariableElements[enemyIndex - 1][2]); }
    public float getEnemySpeed(int enemyIndex) { return Float.parseFloat(enemyVariableElements[enemyIndex - 1][3]); }
    public int getEnemyArmor(int enemyIndex) { return Integer.parseInt(enemyVariableElements[enemyIndex - 1][4]); }
    public int getEnemyReward(int enemyIndex) { return Integer.parseInt(enemyVariableElements[enemyIndex - 1][5]); }
    public String getEnemyImage(int enemyIndex) { return enemyVariableElements[enemyIndex - 1][6]; }
}

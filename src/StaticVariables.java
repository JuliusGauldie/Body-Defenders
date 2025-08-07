
/**
 * Write a description of class TowerVariables here.
 *
 * @author Julius Gauldie
 * @version 07/08/25
 */
import java.util.*;
import java.io.*;

public class StaticVariables
{
    // Files
    final String TOWERFILENAME = "resources/data/InitialTowerVariables.txt"; // Name of CSV file
    final String ENEMYFILENAME = "resources/data/EnemyVariables.txt"; // Name of CSV file
    final String MAPFILENAME = "resources/data/MapVariables.txt";
    final String TOWERPLACEMENTFILENAME = "resources/data/TowerBuildSpots.txt"; // Name of CSV file for tower placements

    // Tower Variables File Setup
    File towerVariables = new File(TOWERFILENAME); // File to read and save accounts 
    final int AMOUNTOFTOWERS = 5; // Total amount of lines in file
    final int VALUESPERLINE = 6; // Damage, Range, Firerate, Towercost, Tower Image, Tower Name

    // Enemy Variables File Setup
    File enemyVariables = new File(ENEMYFILENAME);
    final int AMOUNTOFENEMIES = 5; // Total amount of lines in file
    final int ENEMYVALUESPERLINE = 7; // Health, Speed, Image, Name

    // Map Variables File Setup
    File mapVariables = new File(MAPFILENAME);
    final int AMOUNTOFPOINTS = 17;

    // Tower Placement Variables File Setup
    File towerPlacementVariables = new File(TOWERPLACEMENTFILENAME);
    final int AMOUNTOFTOWERSPOTS = 10;

    // Array(Lists)
    private String allLinesAllElements[][] = new String[AMOUNTOFTOWERS][VALUESPERLINE]; // Array for seperated values
    private String enemyVariableElements[][] = new String[AMOUNTOFENEMIES][ENEMYVALUESPERLINE]; // Array for enemy variables
    private int mapVariableElements[][] = new int[AMOUNTOFPOINTS][2]; // 2 numbers for each coordinate
    private int towerPlacements[][] = new int[AMOUNTOFTOWERSPOTS][2]; // 2 Numbers - 1 for each coordinate

    // Scanner
    Scanner keyboard;

    /**
     * Constructor for objects of class TowerVariables
     */
    public StaticVariables()
    {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        readTowerVariables();
        readEnemyVariables();
        readMapVariables();
        readTowerPlacementVariables();
    }

    /**
     * Check for any saved CSV file to add saved accounts
     */
    private void readTowerVariables() 
    {
        int lineCount = 0; // Int to store amount of lines (accounts)

        try {
            Scanner reader = new Scanner(towerVariables); // Open file with scanner

            while (reader.hasNextLine() && lineCount < AMOUNTOFTOWERS) // Read in the file
            {
                String values[] = reader.nextLine().split(","); // Split the line read on the commas, and save in array

                // Go through the array, and put every string into a new array to have all values seperated
                for (int i = 0; i < values.length; i++)
                {
                    allLinesAllElements[lineCount][i] = values[i];
                }

                lineCount++; 
            }

            reader.close();
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CAN'T READ TOWER FILE"); // Error Message
        } 
    }

    public void readEnemyVariables()
    {
        int lineCount = 0; // Int to store amount of lines (accounts)

        try {
            Scanner reader = new Scanner(enemyVariables); // Open file with scanner

            while (reader.hasNextLine() && lineCount < AMOUNTOFENEMIES) // Read in the file
            {
                String values[] = reader.nextLine().split(","); // Split the line read on the commas, and save in array

                // Go through the array, and put every string into a new array to have all values seperated
                for (int i = 0; i < values.length; i++)
                {
                    enemyVariableElements[lineCount][i] = values[i];
                }

                lineCount++; 
            }

            reader.close();
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CAN'T READ ENEMY FILE"); // Error Message
        } 
    }

    public void readMapVariables()
    {
        int lineCount = 0;

        try {
            Scanner reader = new Scanner(mapVariables); // Open file with scanner

            while (reader.hasNextLine() && lineCount < AMOUNTOFPOINTS)
            {
                String values[] = reader.nextLine().split(","); // Split the line read on the commas, and save in array

                // Go through the array, and put every string into a new array to have all values seperated
                for (int i = 0; i < values.length; i++)
                {
                    mapVariableElements[lineCount][i] = Integer.valueOf(values[i]);
                }

                lineCount++;
            }

            reader.close();
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CAN'T READ MAP FILE"); // Error Message
        } 
    }

    public void readTowerPlacementVariables()
    {
        int lineCount = 0;

        try {
            Scanner reader = new Scanner(towerPlacementVariables); // Open file with scanner

            while (reader.hasNextLine() && lineCount < AMOUNTOFTOWERSPOTS)
            {
                String values[] = reader.nextLine().split(","); // Split the line read on the commas, and save in array

                // Go through the array, and put every string into a new array to have all values seperated
                for (int i = 0; i < values.length; i++)
                {
                    towerPlacements[lineCount][i] = Integer.valueOf(values[i]);
                }

                lineCount++;
            }

            reader.close();
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CAN'T READ TOWER PLACEMENT FILE"); // Error Message
        } 
    }

    // TOWER VARIABLES --------------------

    public float getTowerDamage(int tower)
    {
        return Float.valueOf(allLinesAllElements[(tower - 1)][1]);
    }

    public int getTowerRange(int tower)
    {
        return Integer.valueOf(allLinesAllElements[(tower - 1)][2]);
    }

    public float getTowerFirerate(int tower)
    {
        return Float.valueOf(allLinesAllElements[(tower - 1)][3]);
    }

    public int getTowerCost(int tower)
    {
        return Integer.valueOf(allLinesAllElements[(tower - 1)][4]);
    }

    public String getTowerName(int tower)
    {
        return allLinesAllElements[(tower - 1)][0];
    }

    public String getTowerImage(int tower)
    {
        return allLinesAllElements[tower - 1][5];
    }

    //COORDINATES --------------------

    public int getCoord(int point, int coordinateNumber)
    {
        return mapVariableElements[point][coordinateNumber];
    }
    
    public int getTCoord(int point, int coordinateNumber)
    {
        return towerPlacements[point][coordinateNumber];
    }
    
    public int getAmountOfMapPoints()
    {
        return AMOUNTOFPOINTS;
    }

    public int getAmountOfTowerSpots()
    {
        return AMOUNTOFTOWERSPOTS;
    }

    //ENEMIES --------------------

    public String getEnemyImage(int enemyIndex)
    {
        return enemyVariableElements[enemyIndex - 1][6];
    }

    public int getEnemyReward(int enemyIndex)
    {
        return Integer.valueOf(enemyVariableElements[enemyIndex - 1][5]);
    }

    public float getEnemyHealth(int enemyIndex)
    {
        return Float.valueOf(enemyVariableElements[enemyIndex - 1][1]);
    }

    public float getEnemySpeed(int enemyIndex)
    {
        return Float.valueOf(enemyVariableElements[enemyIndex - 1][3]);
    }
    public int getEnemyDamage(int enemyIndex)
    {
        return Integer.valueOf(enemyVariableElements[enemyIndex - 1][2]);
    }
}
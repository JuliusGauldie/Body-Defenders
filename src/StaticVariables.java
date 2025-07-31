
/**
 * Write a description of class TowerVariables here.
 *
 * @author Julius Gauldie
 * @version 01/08/25
 */
import java.util.*;
import java.io.*;

public class StaticVariables
{
    // Files
    final String TOWERFILENAME = "InitialTowerVariables.txt"; // Name of CSV file
    final String MAPFILENAME = "MapVariables.txt";

    // Tower Variables File Setup
    File towerVariables = new File(TOWERFILENAME); // File to read and save accounts 
    final int AMOUNTOFTOWERS = 2; // Total amount of lines in file
    final int VALUESPERLINE = 6; // Damage, Range, Firerate, Towercost, Tower Image, Tower Name

    // Map Variables File Setup
    File mapVariables = new File(MAPFILENAME);
    final int AMOUNTOFPOINTS = 10;
    final int LINETOWERSPOTSTARTS = 13;
    final int AMOUNTOFTOWERSPOTS = 5;

    // Array(Lists)
    private String allLinesAllElements[][] = new String[AMOUNTOFTOWERS][VALUESPERLINE]; // Array for seperated values
    private int mapVariableElements[][] = new int[AMOUNTOFPOINTS][2]; // 2 numbers for each coordinate
    private int towerPlacements[][] = new int[AMOUNTOFTOWERSPOTS][2]; // 2 Numbers - 1 for each coordinate

    // Scanner
    Scanner keyboard;

    /**
     * Constructor for objects of class TowerVariables
     */
    public StaticVariables()
    {
        readTowerVariables();
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
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CSV file does not follow layout (name,address,number,type,balance)"); // Error Message
            System.out.println("Could not load one or more accounts");

            // Wait 5 seconds before screen is cleared, to show user error message 
            try {
                Thread.sleep(5000);
            } catch (Exception a) {}
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
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CSV file does not follow layout (name,address,number,type,balance)"); // Error Message
            System.out.println("Could not load one or more accounts");

            // Wait 5 seconds before screen is cleared, to show user error message 
            try {
                Thread.sleep(5000);
            } catch (Exception a) {}
        } 
    }

    public void readTowerPlacementVariables()
    {
        int lineCount = 0;

        try {
            Scanner reader = new Scanner(mapVariables); // Open file with scanner

            for (int i = 0; i < LINETOWERSPOTSTARTS - 1; i++)
            {
                reader.nextLine();
            }

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
        }
        catch (Exception e) // If file can't be read
        { 
            System.out.println("ERROR - CSV file does not follow layout (name,address,number,type,balance)"); // Error Message
            System.out.println("Could not load one or more accounts");

            // Wait 5 seconds before screen is cleared, to show user error message 
            try {
                Thread.sleep(5000);
            } catch (Exception a) {}
        } 
    }

    public int getTowerDamage(int tower)
    {
        return Integer.valueOf(allLinesAllElements[(tower - 1)][0]);
    }

    public int getTowerRange(int tower)
    {
        return Integer.valueOf(allLinesAllElements[(tower - 1)][1]);
    }

    public float getTowerFirerate(int tower)
    {
        return Float.valueOf(allLinesAllElements[(tower - 1)][2]);
    }

    public int getTowerCost(int tower)
    {
        return Integer.valueOf(allLinesAllElements[(tower - 1)][3]);
    }

    public String getTowerName(int tower)
    {
        return allLinesAllElements[(tower - 1)][5];
    }

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
}
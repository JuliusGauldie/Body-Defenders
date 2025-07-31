
/**
 * Write a description of class MapVariables here.
 *
 * @author Julius Gauldie
 * @version 31/07/25
 */
import java.util.*;
import java.io.*;

public class MapVariables
{
    // File Setup
    final String FILENAME = "MapVariables.txt"; // Name of CSV file
    File towerVariables = new File(FILENAME); // File to read and save accounts 
    final int AMOUNTOFTOWERS = 2; // Total amount of lines in file
    final int VALUESPERLINE = 6; // Damage, Range, Firerate, Towercost, Tower Image, Tower Name

    // Array(Lists)
    private String allLinesAllElements[][] = new String[AMOUNTOFTOWERS][VALUESPERLINE]; // Array for seperated values
    
    /**
     * Constructor for objects of class MapVariables
     */
    public MapVariables()
    {
        readCSVFile();
    }

    /**
     * Check for any saved CSV file to add saved accounts
     */
    private void readCSVFile() 
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

}

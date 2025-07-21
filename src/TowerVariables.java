
/**
 * Write a description of class TowerVariables here.
 *
 * @author Julius Gauldie
 * @version 21/07/25
 */
import java.util.*;
import java.io.*;

public class TowerVariables
{
    // instance variables 
    InfoPanel panel;

    // File Setup
    final String FILENAME = "../assets/TowerVariables.csv"; // Name of CSV file
    File towerVariables = new File(FILENAME); // File to read and save accounts 
    int amountOfTowers = 2; // Total amount of lines in file
    final int VALUESPERLINE = 5; // Damage, Range, Firerate, Towercost, Tower Image

    // Array(Lists)
    private String allLinesAllElements[][] = new String[amountOfTowers][VALUESPERLINE]; // Array for seperated values

    // Scanner
    Scanner keyboard;

    /**
     * Constructor for objects of class TowerVariables
     */
    public TowerVariables(InfoPanel panel)
    {
        this.panel = panel;

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

            reader.nextLine();

            while (reader.hasNextLine() && lineCount <= amountOfTowers) // Read in the file
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

    /**
     * Counts the amount of lines (accounts) in CSV file
     */
    private int findAmountOfTowers()
    {
        try {
            Scanner reader = new Scanner(towerVariables); // Open file with scanner

            return Integer.valueOf(reader.nextLine());
        }
        catch (IOException e) { // Give error message if file can't be read
            System.out.println("ERROR - " + FILENAME + " does not exist or has been moved"); // Error message

            // Wait 5 seconds before screen is cleared, to show user error message 
            try {
                Thread.sleep(5000);
            } catch (Exception a) {}
        }   
        
        return 2;
    }

    public int getTowerDamage(int tower)
    {
        return Integer.valueOf(allLinesAllElements[tower - 1][0]);
    }

    public int getTowerRange(int tower)
    {
        return Integer.valueOf(allLinesAllElements[tower - 1][1]);
    }

    public float getTowerFirerate(int tower)
    {
        return Float.valueOf(allLinesAllElements[tower - 1][2]);
    }

    public int getTowerCost(int tower)
    {
        return Integer.valueOf(allLinesAllElements[tower - 1][3]);
    }

}
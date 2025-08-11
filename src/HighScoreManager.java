/**
 * Panel showing start menu on startup
 *
 * @author Julius Gauldie
 * @version 11/08/25
 */

import java.io.*;

public class HighScoreManager 
{
    private final String FILE_NAME = "resources/data/highscores.txt";
    private int[] highscores = new int[3]; 

    public HighScoreManager() 
    {
        loadScores();
    }

    public void loadScores()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) 
        {
            for (int i = 0; i < 3; i++)
            {
                String line = reader.readLine();

                if (line == null || line.isEmpty())
                    highscores[i] = Integer.MAX_VALUE;
                else    
                    highscores[i] = Integer.valueOf(line);
            }
        } 
        catch (IOException e) 
        {
            System.out.println("No high score file found");
        }
    }

    public int getScore(int level) 
    {
        return highscores[level - 1];
    }

    public void setScore(int level, int score) 
    {
        highscores[level - 1] = score;
    }

    public void saveScores() 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) 
        {
            for (int score : highscores) 
            {
                writer.write(String.valueOf(score));

                writer.newLine();
            }

            loadScores();
        } 
        catch (IOException e) {   }
    }
}



/**
 * Manages loading, saving, and retrieving high scores for each level.
 * High scores are stored in a simple text file, one score per line.
 *
 * @author Julius Gauldie
 * @version 14/08/25
 */

import java.io.*;

public class HighScoreManager 
{
    // File location for storing highscores
    private static final String FILE_NAME = "resources/data/highscores.txt";

    // Array holding highscores for 3 levels
    private int[] highscores = new int[3];

    /**
     * Constructor – automatically loads scores from file on creation.
     */
    public HighScoreManager() {
        loadScores();
    }

    /**
     * Loads high scores from the file into the array.
     * If a file or line is missing, default is Integer.MAX_VALUE 
     */
    public void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            for (int i = 0; i < 3; i++) {
                String line = reader.readLine();

                if (line == null || line.isEmpty()) {
                    highscores[i] = Integer.MAX_VALUE; // No score recorded yet
                } else {
                    highscores[i] = Integer.valueOf(line);
                }
            }

        } catch (IOException e) {
            System.out.println("No high score file found");
        }
    }

    /**
     * Gets the saved high score for a specific level.
     * 
     * @param level The level number (1–3)
     * @return The saved high score (or Integer.MAX_VALUE if none set)
     */
    public int getScore(int level) {
        return highscores[level - 1];
    }

    /**
     * Sets the high score for a specific level.
     * 
     * @param level The level number (1–3)
     * @param score The new score to set
     */
    public void setScore(int level, int score) {
        highscores[level - 1] = score;
    }

    /**
     * Saves the current high scores to the file.
     * One score per line, corresponding to each level.
     */
    public void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (int score : highscores) {
                writer.write(String.valueOf(score));
                writer.newLine();
            }

            // Reload scores to confirm file changes were applied
            loadScores();

        } catch (IOException e) {
            System.out.println("Failed to save high scores.");
        }
    }
}

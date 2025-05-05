package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HighScoreManager {

    private final Path filePath;
    private List<ScoreEntry> highScores;
    private static final int MAX_SCORES = 10; // Max antal poäng att spara

    public HighScoreManager(String filePath) {
        this.filePath = Paths.get(filePath);
        // Skapa katalog om den inte finns
        try {
            if (this.filePath.getParent() != null) {
                Files.createDirectories(this.filePath.getParent());
            }
        } catch (IOException e) {
            System.err.println("Could not create directory for high score file: " + e.getMessage());
        }
        this.highScores = loadScores(); // Ladda poäng vid start
    }

    // Laddar poäng från CSV-fil
    private List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        if (!Files.exists(filePath)) {
            System.out.println("High score file not found, will be created on save: " + filePath);
            return scores; // Returnera tom lista om filen inte finns
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Hoppa över tomma rader
                String[] parts = line.split(",", 2); // Dela vid första kommat
                if (parts.length == 2) {
                    try {
                        int score = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        scores.add(new ScoreEntry(score, name));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid score line: " + line + " (" + e.getMessage() + ")");
                    }
                } else {
                    System.err.println("Skipping malformed score line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }

        // Sortera listan (högst poäng först) när den laddats
        scores.sort(Collections.reverseOrder());
        return scores;
    }

    // Lägger till en ny poäng, sorterar och sparar listan
    public void addScore(int score, String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "Anonymous";
        }
        // Undvik komma­tecken i namnet för att inte bryta CSV-formatet
        name = name.replace(",", ";");

        highScores.add(new ScoreEntry(score, name));
        // Sortera igen och behåll bara de bästa
        highScores.sort(Collections.reverseOrder());
        if (highScores.size() > MAX_SCORES) {
            highScores = new ArrayList<>(highScores.subList(0, MAX_SCORES));
        }
        saveScores(); // Spara direkt efter ändring
    }

    // Sparar poäng till CSV-fil
    private void saveScores() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (ScoreEntry entry : highScores) {
                writer.write(entry.getScore() + "," + entry.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    // Kollar om en poäng är tillräckligt hög för listan
    public boolean isHighScore(int score) {
        if (highScores.isEmpty() || highScores.size() < MAX_SCORES) {
            return true; // Alltid plats om listan inte är full
        }
        // Jämför med lägsta poängen på listan (sista elementet eftersom listan är sorterad)
        return score > highScores.get(highScores.size() - 1).getScore();
    }

    // Returnerar en (oföränderlig) kopia av topplistan för visning
    public List<ScoreEntry> getHighScores() {
        return Collections.unmodifiableList(highScores);
    }

    //-------------------------------------------------------------------------
    // Intern klass för att hålla poängdata
    // Implementerar Comparable för enkel sortering (för användning med Collections.reverseOrder()).
    //-------------------------------------------------------------------------
    public static class ScoreEntry implements Comparable<ScoreEntry>, Serializable {
        private static final long serialVersionUID = 1L;
        private final int score;
        private final String name;

        public ScoreEntry(int score, String name) {
            this.score = score;
            this.name = (name != null && !name.trim().isEmpty()) ? name : "Anonymous";
        }

        public int getScore() {
            return score;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(ScoreEntry other) {
            // För sortering i stigande ordning; använd reverseOrder() för högst först
            return Integer.compare(this.score, other.score);
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ScoreEntry that = (ScoreEntry) o;
            return score == that.score
                && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, name);
        }
    }

}

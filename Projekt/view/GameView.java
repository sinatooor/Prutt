package view;

import model.Board;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform; // <-- Import för rotation
import java.util.HashMap;
import java.util.Map;

public class GameView extends JPanel {

    private static final int TILE_SIZE = 100; // Storlek på varje bricka i pixlar
    private static final int GAP = 10; // Mellanrum mellan brickor
    private static final int ARC_SIZE = 10; // Hur runda hörnen ska vara

    private Board board; // Referens till spelbrädet för att kunna rita

    // === Färger för rosa/lila tema ===
    private static final Color GAME_BACKGROUND_COLOR = new Color(0xC8A2C8); // Lila (Lilac)
    private static final Color EMPTY_TILE_COLOR = new Color(0xE6E6FA);      // Ljuslila (Lavender)
    private static final Color DEFAULT_TILE_COLOR = new Color(0x301934);    // Mycket mörk lila (fallback)
    private static final Color DARK_TEXT_COLOR = new Color(0x483D8B);       // Mörk skifferblå/lila text
    private static final Color LIGHT_TEXT_COLOR = new Color(0xFFF0F5);      // Mycket ljusrosa (LavenderBlush) text

    // Färgkarta för olika brickvärden (från ljust till mörkt lila)
    private static final Map<Integer, Color> TILE_COLORS = new HashMap<>();
    static {
        TILE_COLORS.put(0, EMPTY_TILE_COLOR);     // Tomma rutor (används i paintComponent)
        TILE_COLORS.put(2, new Color(0xFFD1DC));  // Väldigt ljust rosa (Pink Lace)
        TILE_COLORS.put(4, new Color(0xFFB6C1));  // Ljusrosa (Light Pink)
        TILE_COLORS.put(8, new Color(0xE0B0FF));  // Ljuslila (Mauve)
        TILE_COLORS.put(16, new Color(0xDA70D6)); // Mellanlila (Orchid)
        TILE_COLORS.put(32, new Color(0xBA55D3)); // Medium lila (Medium Orchid)
        TILE_COLORS.put(64, new Color(0x9932CC)); // Mörkare lila (Dark Orchid)
        TILE_COLORS.put(128, new Color(0x9400D3)); // Mörkare lila (Dark Violet)
        TILE_COLORS.put(256, new Color(0x8A2BE2)); // Blålila (Blue Violet)
        TILE_COLORS.put(512, new Color(0x800080)); // Lila (Purple)
        TILE_COLORS.put(1024, new Color(0x4B0082)); // Mörklila (Indigo)
        TILE_COLORS.put(2048, new Color(0x483D8B)); // Mörk skifferblå/lila
        // Lägg till fler för ännu högre värden om du vill...
        TILE_COLORS.put(4096, new Color(0x301934)); // Nästan svart lila för höga värden
    }

    // Färgkarta för text baserat på brickvärde
    private static final Map<Integer, Color> TEXT_COLORS = new HashMap<>();
    static {
        // Ljusa brickor får mörk text
        TEXT_COLORS.put(2, DARK_TEXT_COLOR);
        TEXT_COLORS.put(4, DARK_TEXT_COLOR);
        // Mörkare brickor får ljus text
        TEXT_COLORS.put(8, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(16, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(32, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(64, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(128, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(256, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(512, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(1024, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(2048, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(4096, LIGHT_TEXT_COLOR); // Ljus text för höga värden
    }
    // === Slut på färger ===


    public GameView() {
        // Beräkna panelens föredragna storlek baserat på rutnätet
        int totalGridSize = Board.SIZE * TILE_SIZE + (Board.SIZE + 1) * GAP;
        // Sätt en bredare preferred size för att ge plats åt texten till höger
        setPreferredSize(new Dimension(totalGridSize + TILE_SIZE, totalGridSize)); // Lägg till bredd för text
        setBackground(GAME_BACKGROUND_COLOR); // Sätt bakgrundsfärg för hela panelen
        setFocusable(true); // Viktigt för att KeyListener ska fungera!
    }

    // Metod för att sätta vilket bräde som ska ritas
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ritar bakgrunden (med färgen satt i konstruktorn)

        if (board == null) {
            return; // Rita inget om brädet inte är satt
        }

        Graphics2D g2d = (Graphics2D) g;
        // Sätt på anti-aliasing för snyggare kanter och text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Tile[][] grid = board.getGrid();
        int gridTotalHeight = Board.SIZE * TILE_SIZE + (Board.SIZE + 1) * GAP; // Behövs för centrering

        // --- Rita spelplan och brickor ---
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                int x = GAP + c * (TILE_SIZE + GAP);
                int y = GAP + r * (TILE_SIZE + GAP);

                // Rita bakgrundsruta för varje cell (använder färgen för värde 0)
                g2d.setColor(TILE_COLORS.get(0)); // Färgen för tom ruta
                g2d.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, ARC_SIZE, ARC_SIZE);

                // Om det finns en bricka, rita den ovanpå
                if (grid[r][c] != null) {
                    int value = grid[r][c].getValue();
                    String text = String.valueOf(value);

                    // Hämta färger
                    Color tileColor = TILE_COLORS.getOrDefault(value, DEFAULT_TILE_COLOR);
                    Color textColor = TEXT_COLORS.getOrDefault(value, LIGHT_TEXT_COLOR);

                    // Rita brickans bakgrund
                    g2d.setColor(tileColor);
                    g2d.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, ARC_SIZE, ARC_SIZE);

                    // Rita texten
                    g2d.setColor(textColor);
                    Font font = getFontForValue(value);
                    g2d.setFont(font);
                    FontMetrics fm = g2d.getFontMetrics(font);
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent() - fm.getDescent();

                    // Centrera texten i brickan
                    int textX = x + (TILE_SIZE - textWidth) / 2;
                    int textY = y + (TILE_SIZE - textHeight) / 2 + fm.getAscent() - fm.getDescent() / 2;
                    g2d.drawString(text, textX, textY);
                }
            }
        }
        // --- Slut på ritning av spelplan och brickor ---


        // === Rita VERTIKAL TEXT till höger ===
        try {
            AffineTransform oldTransform = g2d.getTransform();

            String verticalText = "Sina & Malte";
            g2d.setColor(DARK_TEXT_COLOR); // Använd en av de definierade färgerna
            g2d.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 60)); // Anpassa font

            // Beräkna position för texten
            int gridTotalWidth = Board.SIZE * TILE_SIZE + (Board.SIZE + 1) * GAP;
            int textStartX = gridTotalWidth + GAP * 2; // Lite mer marginal till höger
            int textStartY = GAP; // Starta nära toppen

            // Rotera 90 grader runt startpunkten (texten ritas "nedåt")
            g2d.rotate(Math.toRadians(90), textStartX, textStartY);
            g2d.drawString(verticalText, textStartX, textStartY);

            // Återställ transformationen
            g2d.setTransform(oldTransform);

        } catch (Exception e) {
             // Fånga eventuella fel under ritning (osannolikt här)
             System.err.println("Error drawing vertical text: " + e.getMessage());
             e.printStackTrace(); // Skriv ut stack trace för debugging
         }
        // === SLUT PÅ VERTIKAL TEXT ===


        // --- Rita Game Over overlay ---
        if (board.isGameOver()) {
             g2d.setColor(new Color(100, 100, 100, 180)); // Halvtransparent grå
             g2d.fillRect(0, 0, getWidth(), getHeight()); // Fyll hela panelen

             g2d.setColor(Color.WHITE); // Vit text för Game Over
             g2d.setFont(new Font("Arial", Font.BOLD, 40));
             FontMetrics fm = g2d.getFontMetrics();
             String msg = "Game Over!";
             int msgWidth = fm.stringWidth(msg);
             // Centrera Game Over-texten
             g2d.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        }
        // --- Slut på Game Over ---
    } // Slut på paintComponent

    // Hjälpmetod för att välja fontstorlek baserat på brickans värde
    private Font getFontForValue(int value) {
        int fontSize;
        if (value >= 10000) fontSize = 24; // Mindre font för 5+ siffror
        else if (value >= 1000) fontSize = 30; // Mindre font för 4 siffror
        else if (value >= 128) fontSize = 35;
        else if (value >= 16) fontSize = 40;
        else fontSize = 45; // Störst font för 1-2 siffror
        return new Font("Arial", Font.BOLD, fontSize);
    }

} // Slut på GameView klassen
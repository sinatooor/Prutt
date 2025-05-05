package view;

import model.Board;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameView extends JPanel {

    private static final int TILE_SIZE = 100; // Storlek på varje bricka i pixlar
    private static final int GAP = 10; // Mellanrum mellan brickor
    private static final int ARC_SIZE = 10; // Hur runda hörnen ska vara

    private Board board; // Referens till spelbrädet för att kunna rita

    // === FÄRGÄNDRINGAR HÄR ===

    // Definiera dina rosa/lila färger
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
    }

    // === SLUT PÅ FÄRGÄNDRINGAR ===


    public GameView() {
        // Beräkna panelens föredragna storlek
        int totalSize = Board.SIZE * TILE_SIZE + (Board.SIZE + 1) * GAP;
        setPreferredSize(new Dimension(totalSize, totalSize));
        // Sätt bakgrundsfärgen för hela panelen
        setBackground(GAME_BACKGROUND_COLOR); // <-- ÄNDRING HÄR
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Tile[][] grid = board.getGrid();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                int x = GAP + c * (TILE_SIZE + GAP);
                int y = GAP + r * (TILE_SIZE + GAP);

                // Rita bakgrundsruta för varje cell (använder färgen för värde 0)
                g2d.setColor(TILE_COLORS.get(0)); // <-- Använder nu EMPTY_TILE_COLOR
                g2d.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, ARC_SIZE, ARC_SIZE);

                // Om det finns en bricka, rita den ovanpå
                if (grid[r][c] != null) {
                    int value = grid[r][c].getValue();
                    String text = String.valueOf(value);

                    // Hämta färger (använder getOrDefault med vår mörka fallback-färg)
                    Color tileColor = TILE_COLORS.getOrDefault(value, DEFAULT_TILE_COLOR);
                    Color textColor = TEXT_COLORS.getOrDefault(value, LIGHT_TEXT_COLOR); // Default ljus text

                    // Rita brickans bakgrund
                    g2d.setColor(tileColor);
                    g2d.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, ARC_SIZE, ARC_SIZE);

                    // Rita texten
                    g2d.setColor(textColor);
                    // Justera fontstorlek
                    Font font = getFontForValue(value);
                    g2d.setFont(font);
                    // Centrera texten
                    FontMetrics fm = g2d.getFontMetrics(font);
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent() - fm.getDescent(); // fm.getHeight() är ibland för mycket

                    int textX = x + (TILE_SIZE - textWidth) / 2;
                    // Justera Y-positionen lite för bättre vertikal centrering
                    int textY = y + (TILE_SIZE - textHeight) / 2 + fm.getAscent() - fm.getDescent()/2;
                    g2d.drawString(text, textX, textY);
                }
            }
        }

        // Rita Game Over om det är aktuellt (enkel overlay)
        if (board.isGameOver()) {
             g2d.setColor(new Color(100, 100, 100, 180)); // Mörkare, mer täckande grå
             g2d.fillRect(0, 0, getWidth(), getHeight());
             g2d.setColor(Color.WHITE);
             g2d.setFont(new Font("Arial", Font.BOLD, 40));
             FontMetrics fm = g2d.getFontMetrics();
             String msg = "Game Over!";
             int msgWidth = fm.stringWidth(msg);
             g2d.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        }
    }

     // Hjälpmetod för att välja fontstorlek (ingen ändring här)
     private Font getFontForValue(int value) {
         int fontSize;
         if (value >= 10000) fontSize = 24; // För 5-siffriga tal
         else if (value >= 1000) fontSize = 30;
         else if (value >= 128) fontSize = 35;
         else if (value >= 16) fontSize = 40;
         else fontSize = 45;
         return new Font("Arial", Font.BOLD, fontSize);
     }
}
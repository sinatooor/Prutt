package view;

import model.Board.Position;
import model.Board;
import model.Tile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

public class GameView extends JPanel {

    private static final int TILE_SIZE = 100;
    private static final int GAP = 10;
    private static final int ARC_SIZE = 10;
    private Board board;
    
    // === ÄNDRING HÄR: Variabler för animation ===
    private Position posToFlash;
    private Timer flashTimer; // En timer för att styra animationens längd
    // === SLUT PÅ ÄNDRING ===
    
    // Färger och statiska block är oförändrade
    private static final Color GAME_BACKGROUND_COLOR = new Color(0xC8A2C8);
    private static final Color EMPTY_TILE_COLOR = new Color(0xE6E6FA);
    private static final Color DEFAULT_TILE_COLOR = new Color(0x301934);
    private static final Color DARK_TEXT_COLOR = new Color(0x483D8B);
    private static final Color LIGHT_TEXT_COLOR = new Color(0xFFF0F5);
    private static final Map<Integer, Color> TILE_COLORS = new HashMap<>();
    static {
        TILE_COLORS.put(0, EMPTY_TILE_COLOR);
        TILE_COLORS.put(2, new Color(0xFFD1DC)); TILE_COLORS.put(4, new Color(0xFFB6C1));
        TILE_COLORS.put(8, new Color(0xE0B0FF)); TILE_COLORS.put(16, new Color(0xDA70D6));
        TILE_COLORS.put(32, new Color(0xBA55D3)); TILE_COLORS.put(64, new Color(0x9932CC));
        TILE_COLORS.put(128, new Color(0x9400D3)); TILE_COLORS.put(256, new Color(0x8A2BE2));
        TILE_COLORS.put(512, new Color(0x800080)); TILE_COLORS.put(1024, new Color(0x4B0082));
        TILE_COLORS.put(2048, new Color(0x483D8B)); TILE_COLORS.put(4096, new Color(0x301934));
    }
    private static final Map<Integer, Color> TEXT_COLORS = new HashMap<>();
    static {
        TEXT_COLORS.put(2, DARK_TEXT_COLOR); TEXT_COLORS.put(4, DARK_TEXT_COLOR);
        TEXT_COLORS.put(8, LIGHT_TEXT_COLOR); TEXT_COLORS.put(16, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(32, LIGHT_TEXT_COLOR); TEXT_COLORS.put(64, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(128, LIGHT_TEXT_COLOR); TEXT_COLORS.put(256, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(512, LIGHT_TEXT_COLOR); TEXT_COLORS.put(1024, LIGHT_TEXT_COLOR);
        TEXT_COLORS.put(2048, LIGHT_TEXT_COLOR); TEXT_COLORS.put(4096, LIGHT_TEXT_COLOR);
    }
    
    public GameView() {
        int totalGridSize = Board.SIZE * TILE_SIZE + (Board.SIZE + 1) * GAP;
        setPreferredSize(new Dimension(totalGridSize + TILE_SIZE, totalGridSize));
        setBackground(GAME_BACKGROUND_COLOR);
        setFocusable(true);

        // === ÄNDRING HÄR: Initiera timern ===
        // Denna ActionListener körs EN GÅNG efter 300 ms.
        ActionListener timerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posToFlash = null; // Stäng av "flashen"
                repaint();         // Rita om en sista gång med vanlig färg
            }
        };
        flashTimer = new Timer(300, timerAction);
        flashTimer.setRepeats(false); // Viktigt: Timern ska bara köra en gång per start
        // === SLUT PÅ ÄNDRING ===
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Tile[][] grid = board.getGrid();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                int x = GAP + c * (TILE_SIZE + GAP);
                int y = GAP + r * (TILE_SIZE + GAP);
                g2d.setColor(TILE_COLORS.get(0));
                g2d.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, ARC_SIZE, ARC_SIZE);

                if (grid[r][c] != null) {
                    int value = grid[r][c].getValue();
                    Color tileColor;
                    
                    // === ÄNDRING HÄR: Samma logik som förut ===
                    // Denna logik fungerar nu eftersom timern kommer att sätta posToFlash till null
                    if (posToFlash != null && posToFlash.row == r && posToFlash.col == c) {
                        tileColor = Color.WHITE;
                    } else {
                        tileColor = TILE_COLORS.getOrDefault(value, DEFAULT_TILE_COLOR);
                    }
                    
                    g2d.setColor(tileColor);
                    g2d.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, ARC_SIZE, ARC_SIZE);
                    
                    String text = String.valueOf(value);
                    Color textColor = TEXT_COLORS.getOrDefault(value, LIGHT_TEXT_COLOR);
                    g2d.setColor(textColor);
                    Font font = getFontForValue(value);
                    g2d.setFont(font);
                    FontMetrics fm = g2d.getFontMetrics(font);
                    int textWidth = fm.stringWidth(text);
                    int textY = y + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(text, x + (TILE_SIZE - textWidth) / 2, textY);
                }
            }
        }
        
        // --- Rita sidotext och Game Over (oförändrad) ---
        try {
            AffineTransform oldTransform = g2d.getTransform();
            String verticalText = "Sina & Malte";
            g2d.setColor(DARK_TEXT_COLOR);
            g2d.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 60));
            int gridTotalWidth = Board.SIZE * TILE_SIZE + (Board.SIZE + 1) * GAP;
            int textStartX = gridTotalWidth + GAP * 2;
            int textStartY = GAP;
            g2d.rotate(Math.toRadians(90), textStartX, textStartY);
            g2d.drawString(verticalText, textStartX, textStartY);
            g2d.setTransform(oldTransform);
        } catch (Exception e) {
             System.err.println("Error drawing vertical text: " + e.getMessage());
         }
        if (board.isGameOver()) {
             g2d.setColor(new Color(100, 100, 100, 180));
             g2d.fillRect(0, 0, getWidth(), getHeight());
             g2d.setColor(Color.WHITE);
             g2d.setFont(new Font("Arial", Font.BOLD, 40));
             FontMetrics fm = g2d.getFontMetrics();
             String msg = "Game Over!";
             int msgWidth = fm.stringWidth(msg);
             g2d.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        }

        // === ÄNDRING HÄR: Vi tar bort nollställningen härifrån ===
        // this.posToFlash = null; // <-- DENNA RAD TAS BORT. Timern sköter detta nu.
    }

    private Font getFontForValue(int value) {
        int fontSize;
        if (value >= 10000) fontSize = 24;
        else if (value >= 1000) fontSize = 30;
        else if (value >= 128) fontSize = 35;
        else if (value >= 16) fontSize = 40;
        else fontSize = 45;
        return new Font("Arial", Font.BOLD, fontSize);
    }
    
    // === ÄNDRING HÄR: Uppdaterad metod för att starta animationen ===
    public void flashNewTile(Position pos) {
        this.posToFlash = pos;
        
        // Om en flash-animation redan körs, stoppa den gamla och starta om
        // Detta hanterar snabba drag.
        if (flashTimer.isRunning()) {
            flashTimer.stop();
        }
        flashTimer.start(); // Starta 300ms nedräkningen
        
        repaint(); // Rita om omedelbart för att visa "flashen"
    }
}
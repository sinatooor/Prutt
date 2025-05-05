package model;

import java.io.Serializable; // För att kunna spara/ladda GameState

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L; // Bra praxis för Serializable
    private int value;

    public Tile(int value) {
        if (value < 0) { // Värdet bör inte vara negativt
            throw new IllegalArgumentException("Tile value cannot be negative.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Skyddad metod så att endast Board kan ändra värdet (vid merge)
    protected void setValue(int value) {
        this.value = value;
    }

    // En bricka anses tom om värdet är 0 (även om vi använder null i Board)
    public boolean isEmpty() {
        return value == 0;
    }

    @Override
    public String toString() {
        // För enkel debugging i konsolen
        return String.valueOf(value);
    }

    // Överlagra equals och hashCode om du skulle lagra Tiles i Set/Map direkt (inte nödvändigt här)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return value == tile.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
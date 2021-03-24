package gamePieces;

public class Tile {
    private char letter;
    private int value;

    public Tile(char letter, int value) {
        this.letter = letter;
        this.value = value;
    }

    /**
     * Only used to create "distinct" Tile objects with different references
     * (mostly for the Rack object since we have tiles with the same letter
     * in the rack and we're storing the collection of tiles in the rack as a
     * map...)
     * @param tile
     */
    public Tile(Tile tile) {
        this.letter = tile.letter;
        this.value = tile.value;
    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }

    public boolean checkTile(char letter) {
        return this.letter == letter;
    }

    @Override
    public String toString() {
        return "" + letter + " (value: " + value + ")";
    }
}

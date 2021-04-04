package gamePieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Holds the information for the tile (simple encapsulation of the letter and
 * value of the tile) and extra static methods to determine blank tiles
 */
public class Tile {
    // assuming the character for a blank character is an asterisk '*', which
    // will also be referenced in the WordSolver class to execute the special
    // check for a blank tile
    public static final char BLANK_LETTER = '*';
    public static final int BLANK_VALUE = 0;
    private static final String BLANK_FILE_PREFIX = "Blank";

    private char letter;
    private int value;

    private ImageView imageView;

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

    public ImageView getTileImage() {
        return imageView;
    }

    public void setTileImage() {
        String fileName = ".png";
        String filePrefix = "/scrabble_tiles/";

        if (letter >= 'a' && letter <= 'z') {
            fileName = filePrefix + Character.toUpperCase(letter) + fileName;
        } else { // assume this is the blank...
            fileName = filePrefix + BLANK_FILE_PREFIX + fileName;
        }

        Image image =
                new Image(String.valueOf(
                        Tile.class.getResource(fileName)));
        imageView = new ImageView(image);
    }

    public static boolean isBlankTile(Tile tile) {
        if (tile.letter == BLANK_LETTER || tile.getValue() == BLANK_VALUE) {
            return true;
        }

        return false;
    }

    public void updateBlankLetter(char letter) {
        this.letter = letter;
    }

    public void revertBlankLetter() {
        this.letter = BLANK_LETTER;
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

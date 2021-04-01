package comparators;

import gamePieces.Tile;

import java.util.Comparator;

/**
 * Comparator for the Tile class (just compares the letter of the Tile objects)
 */
public class TileComparator implements Comparator<Tile> {
    @Override
    public int compare(Tile tile1, Tile tile2) {
        return tile1.getLetter() - tile2.getLetter();
    }
}

package gamePieces;

import java.util.Comparator;

public class TileComparator implements Comparator<Tile> {
    @Override
    public int compare(Tile tile1, Tile tile2) {
        return tile1.getLetter() - tile2.getLetter();
    }
}

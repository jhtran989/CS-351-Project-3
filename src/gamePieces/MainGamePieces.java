package gamePieces;

import constants.InputChoice;

public class MainGamePieces {
    public static boolean DEBUG = true;

    public static void main(String[] args) {
        TileBag tileBag = new TileBag(InputChoice.FILE);
        Board board = new Board(InputChoice.CONSOLE, tileBag);

//        Set<Tile> tiles = new TreeSet<>(new TileComparator());
//        tiles.add(new Tile('a', 1));
//        tiles.add(new Tile('a', 1));
//        System.out.println(tiles);
    }
}

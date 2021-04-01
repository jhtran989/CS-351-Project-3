package gamePieces;

import constants.InputChoice;

import java.util.Scanner;

/**
 * Main to test out the game pieces
 */
public class MainGamePieces {
    public static boolean DEBUG = true;

    public static void main(String[] args) {
        try (Scanner scanner =
                     new Scanner(System.in)) {
            TileBag tileBag = new TileBag(InputChoice.FILE,
                    scanner);
            Board board = new Board(InputChoice.CONSOLE,
                    tileBag, scanner);
        }

//        Set<Tile> tiles = new TreeSet<>(new TileComparator());
//        tiles.add(new Tile('a', 1));
//        tiles.add(new Tile('a', 1));
//        System.out.println(tiles);
    }
}

package gamePieces;

import comparators.TileComparator;
import constants.InputChoice;
import exceptions.InputErrorException;
import exceptions.InternalError;
import utilities.CustomParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Holds the information and collection of tiles in the bag used to draw
 * tiles from
 */
public class TileBag {
    private final Map<Tile, Integer> tileFrequency;
    private final Set<Character> fullLetterSet;
    private Scanner scanner;

    private final boolean PRINT_TILE_BAG = false;

    public TileBag(InputChoice inputChoice, Scanner scanner) {
        this.scanner = scanner;
        tileFrequency = new TreeMap<>(new TileComparator());
        fullLetterSet = new TreeSet<>();

        if (inputChoice == InputChoice.FILE) {
            String tilesFilePath = "resources/scrabble_tiles.txt";

            setupTiles(tilesFilePath);
        } else {
            setupTiles();
        }

        if (PRINT_TILE_BAG) {
            printTiles();
        }
    }

    public TileBag(String filePath) {
        tileFrequency = new TreeMap<>(new TileComparator());
        fullLetterSet = new TreeSet<>();

        setupTiles(filePath);

        if (PRINT_TILE_BAG) {
            printTiles();
        }
    }

    public Set<Character> getFullLetterSet() {
        return fullLetterSet;
    }

    /**
     * Finds the tile in the bag with the given letter (null if not found or
     * if the bag runs out)
     *
     * @param letter
     * @return
     */
    public Tile findTileInFrequencyMap(char letter) {
//        // also allows for capital letters for blank tiles
//        letter = Character.toLowerCase(letter);

        if (Character.isUpperCase(letter)) {
            Tile blank = getBlankTile();

            assert blank != null;
            blank.updateBlankLetter(letter);
            int frequency = tileFrequency.get(blank);

            if (frequency - 1 < 0) {
                try {
                    throw new InternalError("Out of tile " + blank
                            + " " + "in the tile bag...");
                } catch (InternalError internalError) {
                    System.out.println(internalError.getMessage());
                    System.out.println("Returning null...");
                    return null;
                }
            }

            frequency--;
            tileFrequency.put(blank, frequency);
            return blank;
        }

        for (Map.Entry<Tile, Integer> tileFrequencyEntry :
                tileFrequency.entrySet()) {
            Tile currentTile = tileFrequencyEntry.getKey();
            int frequency = tileFrequencyEntry.getValue();

            if (currentTile.getLetter() == letter) {
                if (frequency - 1 < 0) {
                    try {
                        throw new InternalError("Out of tile " + currentTile
                                + " " + "in the tile bag...");
                    } catch (InternalError internalError) {
                        System.out.println(internalError.getMessage());
                        break;
                    }
                }

                frequency--;
                tileFrequency.put(currentTile, frequency);
                return currentTile;
            }
        }

        System.out.println("Returning null...");

        return null; // shouldn't happen if the board and tile files agree
        // with each other...
    }

    private Tile getBlankTile() {
        for (Tile tile : tileFrequency.keySet()) {
            if (tile.getLetter() == Tile.BLANK_LETTER) {
                return tile;
            }
        }

        return null; // shouldn't happen if our constant for the blank tile
        // is contained in the file...
    }

    private void setupTiles(String filePath) {
        try (Scanner scanner =
                     new Scanner(new FileReader(filePath))) {
            while (scanner.hasNextLine()) {
                char letter = CustomParser.parseChar(scanner.next());
                int value = CustomParser.parseInt(scanner.next());
                int frequency = CustomParser.parseInt(scanner.next());
                tileFrequency.put(new Tile(letter, value),
                        frequency);

                // only allow the letters from the alphabet...asterisk almost
                // got in...
                if (letter >= 'a' && letter <= 'z') {
                    fullLetterSet.add(letter);
                }

                scanner.nextLine();
            }
        } catch (InputErrorException | FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setupTiles() {
        try {
            while (scanner.hasNextLine()) {
                char letter = CustomParser.parseChar(scanner.next());
                int value = CustomParser.parseInt(scanner.next());
                int frequency = CustomParser.parseInt(scanner.next());
                tileFrequency.put(new Tile(letter, value),
                        frequency);
                fullLetterSet.add(letter);

                scanner.nextLine();
            }
        } catch (InputErrorException inputErrorException) {
            System.out.println(inputErrorException.getMessage());
        }
    }

    private void printTiles() {
        System.out.println("Tiles");

        for (Map.Entry<Tile, Integer> tileEntry : tileFrequency.entrySet()) {
            System.out.println(tileEntry.getKey() + " (frequency: " +
                    tileEntry.getValue() + ")");
        }
    }
}

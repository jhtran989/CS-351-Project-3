package gamePieces;

import comparators.TileComparator;
import constants.InputChoice;
import exceptions.InputErrorException;
import exceptions.InternalError;
import utilities.CustomParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class TileBag {
    private Map<Tile, Integer> tileFrequency;
    private Set<Character> fullLetterSet;
    private InputChoice inputChoice;
    private Scanner scanner;

    public TileBag(InputChoice inputChoice, Scanner scanner) {
        this.inputChoice = inputChoice;
        this.scanner = scanner;
        tileFrequency = new TreeMap<>(new TileComparator());
        fullLetterSet = new TreeSet<>();

        if (this.inputChoice == InputChoice.FILE) {
            String tilesFilePath = "resources/scrabble_tiles.txt";

            setupTiles(tilesFilePath);
        } else {
            setupTiles();
        }

        printTiles();
    }

    public Set<Character> getFullLetterSet() {
        return fullLetterSet;
    }

    public Tile findTileInFrequencyMap(char letter) {
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
                    }
                }

                frequency--;
                tileFrequency.put(currentTile, frequency);
                return currentTile;
            }
        }

        return null; // shouldn't happen if the board and tile files agree
        // with each other...
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

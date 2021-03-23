package gamePieces;

import comparators.TileComparator;
import constants.InputChoice;
import exceptions.InputErrorException;
import utilities.CustomParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class TileBag {
    private Map<Tile, Integer> frequencyTile;
    private Set<Character> fullLetterSet;
    private InputChoice inputChoice;

    public TileBag(InputChoice inputChoice) {
        this.inputChoice = inputChoice;
        frequencyTile = new TreeMap<>(new TileComparator());
        fullLetterSet = new TreeSet<>();

        if (this.inputChoice == InputChoice.FILE) {
            String tilesFilePath = "resources/scrabble_tiles.txt";

            setupTiles(tilesFilePath);
        } else {
            setupTiles();
        }

        //printTiles();
    }

    public Set<Character> getFullLetterSet() {
        return fullLetterSet;
    }

    public Tile findTileInFrequencyMap(char letter) {
        for (Map.Entry<Tile, Integer> tileFrequencyEntry :
                frequencyTile.entrySet()) {
            Tile currentTile = tileFrequencyEntry.getKey();

            if (currentTile.getLetter() == letter) {
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
                frequencyTile.put(new Tile(letter, value),
                        frequency);
                fullLetterSet.add(letter);

                scanner.nextLine();
            }
        } catch (InputErrorException | FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setupTiles() {
        try (Scanner scanner =
                     new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                char letter = CustomParser.parseChar(scanner.next());
                int value = CustomParser.parseInt(scanner.next());
                int frequency = CustomParser.parseInt(scanner.next());
                frequencyTile.put(new Tile(letter, value),
                        frequency);
                fullLetterSet.add(letter);

                scanner.nextLine();
            }
        } catch (InputErrorException inputErrorException) {
            System.out.println(inputErrorException .getMessage());
        }
    }

    private void printTiles() {
        System.out.println("Tiles");

        for (Map.Entry<Tile, Integer> tileEntry : frequencyTile.entrySet()) {
            System.out.println(tileEntry.getKey() + " (frequency: " +
                    tileEntry.getValue() + ")");
        }
    }
}

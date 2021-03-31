package gamePieces;

import comparators.TileComparator;
import wordSolver.MainWordSolver;

import java.util.*;

import static gamePieces.Tile.BLANK_LETTER;

public class Rack {
    protected Map<Tile, Character> rackMap;
    protected TileBag tileBag;
    private final Scanner scanner;

    public Rack(TileBag tileBag, Scanner scanner) {
        this.scanner = scanner;
        //rackMap = new TreeMap<>(new TileComparator());
        rackMap = new LinkedHashMap<>();
        this.tileBag = tileBag;

        setupRack();

        printRack();
    }

    public Map<Tile, Character> getRackMap() {
        return rackMap;
    }

    public Tile searchLetter(Character letter) {
        for (Map.Entry<Tile, Character> tileCharacterEntry :
                rackMap.entrySet()) {
            if (tileCharacterEntry.getValue().equals(letter)) {
                return tileCharacterEntry.getKey();
            }
        }

        // the blank tile has the least precedence to make sure it will only
        // be used if there isn't already a a non-blank tile that has the
        // matching letter
        if (rackMap.containsValue(BLANK_LETTER)) {
            for (Map.Entry<Tile, Character> tileCharacterEntry :
                    rackMap.entrySet()) {
                if (tileCharacterEntry.getValue() == BLANK_LETTER) {
                    return tileCharacterEntry.getKey();
                }
            }
        }

        return null;
    }

    public void removeTile(Tile tile) {
        rackMap.remove(tile);
    }

    public void addTile(Tile tile) {
        if (Tile.isBlankTile(tile)) {
            rackMap.put(tile, BLANK_LETTER);
        } else {
            rackMap.put(tile, tile.getLetter());
        }
    }

    // FIXME: fix mapping and update frequency (decrement)... (so there's
    //  duplicates,
    //  and maybe change .equals()
    //  for Tiles...)
    private void setupRack() {
        String rackLetters = "empty";
        if (scanner.hasNextLine()) {
            rackLetters = scanner.nextLine();
        }

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println("Rack input: " + rackLetters);
        }

        for (int i = 0; i < rackLetters.length(); i++) {
            char currentLetter = rackLetters.charAt(i);
            Tile currentTile =
                    tileBag.findTileInFrequencyMap(currentLetter);
            // a new Tile object is created to account for the fact that the
            // tiles are "decoupled" once removed from the bag of tiles and
            // to account for the possibility that multiple tiles with the same
            // letter can be added to the rack ()
            Tile newTile = new Tile(currentTile);

            rackMap.put(newTile, currentLetter);
        }
    }

    public void printRack() {
        System.out.println("Rack: ");

        int count = 0;
        for (Character letter : rackMap.values()) {
            System.out.println("" + letter + " " + count);
            count++;
        }
    }

    @Override
    public String toString() {
        String rackString = "";
        for (Character rackLetter : rackMap.values()) {
            rackString += rackLetter;
        }

        return rackString;
    }
}

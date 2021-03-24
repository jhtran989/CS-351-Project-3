package gamePieces;

import comparators.TileComparator;
import wordSolver.MainWordSolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Rack {
    protected Map<Tile, Character> rackMap;
    protected TileBag tileBag;
    private Scanner scanner;

    public Rack(TileBag tileBag, Scanner scanner) {
        this.scanner = scanner;
        rackMap = new HashMap<>();
        this.tileBag = tileBag;

        setupRack();

        printRack();
    }

    public Tile searchCharacter(Character character) {
        for (Map.Entry<Tile, Character> tileCharacterEntry : rackMap.entrySet()) {
            if (tileCharacterEntry.getValue().equals(character)) {
                return tileCharacterEntry.getKey();
            }
        }

        return null;
    }

    public void removeTile(Tile tile) {
        rackMap.remove(tile);
    }

    public void addTile(Tile tile) {
        rackMap.put(tile, tile.getLetter());
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

        if (MainWordSolver.DEBUG) {
            System.out.println("Rack input: " + rackLetters);
        }

        for (int i = 0; i < rackLetters.length(); i++) {
            char currentLetter = rackLetters.charAt(i);
            Tile currentTile =
                    tileBag.findTileInFrequencyMap(currentLetter);
            Tile newTile = new Tile(currentTile);

            rackMap.put(newTile, currentLetter);
        }
    }

    private void printRack() {
        System.out.println("Rack: ");

        int count = 0;
        for (Character letter : rackMap.values()) {
            System.out.println("" + letter + " " + count);
            count++;
        }
    }
}

package gamePieces;

import comparators.TileComparator;
import utilities.CustomParser;
import wordSolver.MainWordSolver;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Rack {
    protected Map<Tile, Character> rackMap;
    protected TileBag tileBag;

    public Rack(TileBag tileBag) {
        rackMap = new TreeMap<>(new TileComparator());
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
        try (Scanner scanner =
                     new Scanner(System.in)) {
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
                rackMap.put(currentTile, currentLetter);
            }
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

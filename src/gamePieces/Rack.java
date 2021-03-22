package gamePieces;

import java.util.Map;

public class Rack {
    protected Map<Tile, Character> rack;
    
    public Tile searchCharacter(Character character) {
        for (Map.Entry<Tile, Character> tileCharacterEntry : rack.entrySet()) {
            if (tileCharacterEntry.getValue().equals(character)) {
                return tileCharacterEntry.getKey();
            }
        }

        return null;
    }

    public void removeTile(Tile tile) {
        rack.remove(tile);
    }

    public void addTile(Tile tile) {
        rack.put(tile, tile.getLetter());
    }
}

package players;

import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.Tile;
import gamePieces.TileBag;
import javafx.scene.image.ImageView;
import wordSolver.WordSolver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Player {
    protected Board board;
    protected TileBag tileBag;
    protected Rack rack;
    protected WordSolver wordSolver;
    protected List<ImageView> newTileInRackImageView;
    protected Map<Tile, Character> newHumanRackMap;

    public Player(Board board, TileBag tileBag, WordSolver wordSolver) {
        this.board = board;
        this.tileBag = tileBag;
        rack = new Rack(tileBag);
        this.wordSolver = wordSolver;

        newTileInRackImageView = new ArrayList<>();
        newHumanRackMap = new LinkedHashMap<>();
    }

    public abstract void play();

    public List<ImageView> getRackImages() {
        List<ImageView> rackImages = new ArrayList<>();

        for (Tile tile : rack.getRackMap().keySet()) {
            rackImages.add(tile.getTileImage());
        }

        return rackImages;
    }

    public Rack getRack() {
        return rack;
    }

    public void setWordSolver(WordSolver wordSolver) {
        this.wordSolver = wordSolver;
    }

    public void refreshRack() {
        newTileInRackImageView.clear();
        newHumanRackMap.clear();

        while (rack.getRackMap().size() < Rack.RACK_SIZE) {
            Tile currentTile = rack.addRandomTile();
            newTileInRackImageView.add(currentTile.getTileImage());
            newHumanRackMap.put(currentTile, currentTile.getLetter());
        }
    }

    public List<ImageView> getNewTileInRackImageView() {
        return newTileInRackImageView;
    }

    public Map<Tile, Character> getNewHumanRackMap() {
        return newHumanRackMap;
    }
}

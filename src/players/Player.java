package players;

import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.Tile;
import gamePieces.TileBag;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    protected Board board;
    protected TileBag tileBag;
    protected Rack rack;

    public Player(Board board, TileBag tileBag) {
        this.board = board;
        this.tileBag = tileBag;
        rack = new Rack(tileBag);
    }

    public List<ImageView> getRackImages() {
        List<ImageView> rackImages = new ArrayList<>();

        for (Tile tile : rack.getRackMap().keySet()) {
            rackImages.add(tile.getTileImage());
        }

        return rackImages;
    }
}

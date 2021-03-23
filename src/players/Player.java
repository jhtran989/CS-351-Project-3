package players;

import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.Tile;

import java.util.List;
import java.util.Map;

public class Player {
    protected Board board;
    protected Rack rack;

    public Player(Board board, Rack rack) {
        this.board = board;
        this.rack = rack;
    }
}

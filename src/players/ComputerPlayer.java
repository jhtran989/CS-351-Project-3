package players;

import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.TileBag;
import wordSolver.WordSolver;

public class ComputerPlayer extends Player {
    public ComputerPlayer(Board board, TileBag tileBag, WordSolver wordSolver) {
        super(board, tileBag, wordSolver);
    }

    @Override
    public void play() {

    }
}

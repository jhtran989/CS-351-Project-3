package gamePieces;

import constants.PlayDirection;

import java.util.List;

public class CrossCheckWord extends WordInPlay {
    private char finalChar;
    private int finalCharIndex;
    private boolean setFinalChar;

    public CrossCheckWord(PlayDirection playDirection, String word,
                          int firstIndex, int lastIndex, int rowColumnIndex,
                          List<BoardSquare> wordBoardSquares) {
        super(playDirection, word, firstIndex,
                lastIndex, rowColumnIndex,
                wordBoardSquares);

        setFinalChar = false;
    }


    public void setCharChoice(char finalChar, char finalCharIndex) {
        this.finalChar = finalChar;
        this.finalCharIndex = finalCharIndex;
        setFinalChar = true;
    }

    @Override
    public String toString() {
        return super.toString() + " complete: " + setFinalChar;
    }
}

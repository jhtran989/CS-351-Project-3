package gamePieces;

import constants.PlayDirection;

import java.util.List;

/**
 * An extension of the WordInPlay class that has a few more methods and
 * member variables to check cross words that are formed (in the opposite
 * play direction of a given word)
 */
public class CrossCheckWord extends WordInPlay {
    private final int finalCharIndex;
    private boolean setFinalChar;

    public CrossCheckWord(PlayDirection playDirection, String word,
                          int firstIndex, int lastIndex, int rowColumnIndex,
                          List<BoardSquare> wordBoardSquares, int finalCharIndex) {
        super(playDirection, word, firstIndex,
                lastIndex, rowColumnIndex,
                wordBoardSquares);

        this.finalCharIndex = finalCharIndex;
        setFinalChar = false;
    }

    public void addFinalBoardSquare(BoardSquare wordBoardSquare,
                                    int startWordIndex) {
        wordBoardSquares.add(finalCharIndex - startWordIndex,
                wordBoardSquare);
    }

    public BoardSquare removeFinalBoardSquare(int startWordIndex) {
        return wordBoardSquares.remove(finalCharIndex - startWordIndex);
    }

    public void setLetterChoice(char finalChar, int startWordIndex) {
        word = word.substring(0, finalCharIndex - startWordIndex) + finalChar +
                word.substring(finalCharIndex - startWordIndex);

        setFinalChar = true;
    }

    public void resetLetterChoice(int startWordIndex) {
        word = word.substring(0, finalCharIndex - startWordIndex) +
                        word.substring(
                                finalCharIndex - startWordIndex + 1);

        setFinalChar = false;
    }

    public int getFinalCharIndex() {
        return finalCharIndex;
    }

    @Override
    public String toString() {
        return super.toString() + " complete: " + setFinalChar;
    }
}

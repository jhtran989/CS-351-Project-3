package gamePieces;

import constants.BoardSquareType;
import constants.PlayDirection;

import java.util.ArrayList;
import java.util.List;

public class WordInPlay {
    protected PlayDirection playDirection;
    protected String word;
    protected int firstIndex;
    protected int lastIndex;
    protected int rowColumnIndex;
    protected List<BoardSquare> wordBoardSquares;

    public WordInPlay(PlayDirection playDirection, String word,
                      int firstIndex, int lastIndex, int rowColumnIndex,
                      List<BoardSquare> wordBoardSquares) {
        this.playDirection = playDirection;
        this.word = word;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.rowColumnIndex = rowColumnIndex;
        this.wordBoardSquares = wordBoardSquares;
    }

    public List<BoardSquare> getWordBoardSquares() {
        return wordBoardSquares;
    }

    /**
     * Only calculates the word itself (no cross words)
     *
     * @return
     */
    public int calculateScore() {
        int wordScore = 0;
        int wordMultiplier = 1;

        for (BoardSquare wordBoardSquare : wordBoardSquares) {
            int currentSquareValue = wordBoardSquare.getActiveTile().getValue();

            if (wordBoardSquare.isActiveMultiplier()) {
                BoardSquareType currentBoardSquareType =
                        wordBoardSquare.getBoardSquareType();
                currentSquareValue *=
                        currentBoardSquareType.getLetterMultiplier();
                wordMultiplier *= currentBoardSquareType.getWordMultiplier();
            }

            wordScore += currentSquareValue;
        }

        return wordScore * wordMultiplier;
    }

    public void updateWord(char addedCharacter) {
        word += addedCharacter;
        lastIndex++;
    }

    public PlayDirection getPlayDirection() {
        return playDirection;
    }

    public String getWord() {
        return word;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int getRowColumnIndex() {
        return rowColumnIndex;
    }

    @Override
    public String toString() {
        String direction =
                playDirection + " (index " + rowColumnIndex + ") ";
        return direction + "first index: " + firstIndex + ", last index: "
                + lastIndex + ", word: " + word;
    }
}

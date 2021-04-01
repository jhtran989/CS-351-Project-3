package gamePieces;

import constants.BoardSquareType;
import constants.PlayDirection;
import constants.TrueBoardSquareType;

import java.util.ArrayList;
import java.util.List;

/**
 * An encapsulation of the words that are going to be played (or already in
 * play) -- holds some extra information while going through the process of
 * forming words, like the left and right parts
 */
public class WordInPlay {
    protected PlayDirection playDirection;
    protected String word;
    protected int firstIndex;
    protected int lastIndex;
    protected int rowColumnIndex;
    protected List<BoardSquare> wordBoardSquares;
    protected String leftPart = "";
    protected String rightPart = "";

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

    public WordInPlay(WordInPlay wordInPlay) {
        this.playDirection = wordInPlay.playDirection;
        this.word = wordInPlay.word;
        this.firstIndex = wordInPlay.firstIndex;
        this.lastIndex = wordInPlay.lastIndex;
        this.rowColumnIndex = wordInPlay.rowColumnIndex;
        this.wordBoardSquares = new ArrayList<>(wordInPlay.wordBoardSquares);

        this.leftPart = wordInPlay.leftPart;
        this.rightPart = wordInPlay.rightPart;
    }

    public void addWordBoardSquareToBeginning(BoardSquare boardSquare) {
        wordBoardSquares.add(0, boardSquare);
    }

    public void addWordBoardSquareToEnd(BoardSquare boardSquare) {
        wordBoardSquares.add(boardSquare);
    }

    public BoardSquare removeWordBoardSquareAtBeginning() {
        if (!wordBoardSquares.isEmpty()) {
            return wordBoardSquares.remove(0);
        }

        return null;
    }

    public BoardSquare removeWordBoardSquareAtEnd() {
        if (!wordBoardSquares.isEmpty()) {
            return wordBoardSquares.remove(wordBoardSquares.size() - 1);
        }

        return null;
    }

    public BoardSquare getWordBoardSquareAtBeginning() {
        if (!wordBoardSquares.isEmpty()) {
            return wordBoardSquares.get(0);
        }

        return null;
    }

    public BoardSquare getWordBoardSquareAtEnd() {
        if (!wordBoardSquares.isEmpty()) {
            return wordBoardSquares.get(wordBoardSquares.size() - 1);
        }

        return null;
    }

    public void removeWordBoardSquare(BoardSquare boardSquare) {
        wordBoardSquares.remove(boardSquare);
    }

    public int getWordLength() {
        return word.length();
    }

    public List<BoardSquare> getWordBoardSquares() {
        return wordBoardSquares;
    }

    public void printWordBoardSquares() {
        System.out.println("Word board squares: ");

        for (BoardSquare wordBoardSquare : wordBoardSquares) {
            if (wordBoardSquare == null) {
                System.out.println("Empty board square...");
            } else {
                wordBoardSquare.printFullBoardSquareInfo();
            }
        }
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
                TrueBoardSquareType currentBoardSquareType =
                        wordBoardSquare.getBoardSquareType();
                currentSquareValue *=
                        currentBoardSquareType.getLetterMultiplier();
                wordMultiplier *= currentBoardSquareType.getWordMultiplier();
            }

            wordScore += currentSquareValue;
        }

        return wordScore * wordMultiplier;
    }

    public String getRightPart() {
        return rightPart;
    }

    public void updateRightExtendWord(char addedCharacter) {
        word += addedCharacter;
        rightPart += addedCharacter;

        if (rightPart.length() > 1) {
            lastIndex++;
        }
    }

    public void removeRightExtendWord() {
        word = word.substring(0, word.length() - 1);
        rightPart = rightPart.substring(0, rightPart.length() - 1);

        if (rightPart.length() > 0) {
            lastIndex--;
        }
    }

    public String getLeftPart() {
        return leftPart;
    }

    public void updateLeftPartWord(char addedCharacter) {
        word = addedCharacter + word;
        leftPart = addedCharacter + leftPart;

        firstIndex--;
    }

    public void removeLeftPartWord() {
        word = word.substring(1);
        leftPart = leftPart.substring(1);

        firstIndex++;
    }

    public void setLeftPart(String leftPart) {
        this.leftPart = leftPart;
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

    public char getLetterAtIndex(int letterIndex) {
        return word.charAt(letterIndex - firstIndex);
    }

    // Overriden both equals() and hashCode() since we don't want duplicate
    // words...(not really necessary, but helps optimize the list of legal
    // words)

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            WordInPlay other = (WordInPlay) obj;

            if (this.word.equals(other.word)
                    && this.firstIndex == other.firstIndex
                    && this.lastIndex == other.lastIndex
                    && this.rowColumnIndex == other.rowColumnIndex
                    && this.playDirection == other.playDirection) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return word.hashCode() + firstIndex + lastIndex + rowColumnIndex
                + playDirection.hashCode();
    }

    @Override
    public String toString() {
        String direction =
                playDirection + " (index " + rowColumnIndex + ") ";
        return direction + "first index: " + firstIndex + ", last index: "
                + lastIndex + ", word: " + word;
    }
}

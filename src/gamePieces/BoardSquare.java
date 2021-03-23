package gamePieces;

import constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoardSquare {
    private final int NUM_CHAR = 2;

    private String twoChar;
    private Character letter;
    private BoardSquareType boardSquareType;
    private int rowIndex;
    private int columnIndex;
    private boolean wordHorizontalCheck;
    private boolean wordVerticalCheck;
    private Anchor anchor;
    private int dimension;
    private boolean topEdge;
    private boolean bottomEdge;
    private boolean rightEdge;
    private boolean leftEdge;
    private boolean crossCheck;
    private List<Character> crossCheckList;
    private boolean crossCheckHorizontal;
    private boolean crossCheckVertical;
    private boolean activeMultiplier;
    private Tile activeTile;
    Set<Character> fullLetterSet;

    // FIXME
    private List<CrossCheckWord> crossCheckWordList;
    private CrossCheckWord horizontalCrossCheckWord;
    private CrossCheckWord verticalCrossCheckWord;

    public BoardSquare(String twoChar, int rowIndex, int columnIndex,
                       int dimension, Set<Character> fullLetterSet) {
        this.twoChar = twoChar;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.dimension = dimension;
        letter = null;
        this.fullLetterSet = fullLetterSet;

        setBoardSquareType();
        wordHorizontalCheck = true;
        wordVerticalCheck = true;

        anchor = null;

        if (rowIndex == 0) {
            topEdge = true;
        } else if (rowIndex == dimension - 1) {
            bottomEdge = true;
        }

        if (columnIndex == 0) {
            leftEdge = true;
        } else if (columnIndex == dimension - 1) {
            rightEdge = true;
        }

        crossCheck = false;
        crossCheckList = generateCrossCheckList();
        crossCheckHorizontal = false;
        crossCheckVertical = false;

        activeMultiplier = true;
        activeTile = null;

        horizontalCrossCheckWord = null;
        verticalCrossCheckWord = null;
    }

    public CrossCheckWord getHorizontalCrossCheckWord() {
        return horizontalCrossCheckWord;
    }

    public CrossCheckWord getVerticalCrossCheckWord() {
        return verticalCrossCheckWord;
    }

    public void addCrossCheckWord(PlayDirection playDirection,
                                  CrossCheckWord crossCheckWord) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            horizontalCrossCheckWord = crossCheckWord;
        } else {
            verticalCrossCheckWord = crossCheckWord;
        }
    }

    public Tile getActiveTile() {
        return activeTile;
    }

    public boolean isActiveMultiplier() {
        return activeMultiplier;
    }

    /**
     * "Deactivates" the multiplier on the board square (so the multiplier is
     * just x1)
     * @param tile
     */
    public void placeTile(Tile tile) {
        boardSquareType = BoardSquareType.LETTER;
        letter = tile.getLetter();

        activeTile = tile;
        activeMultiplier = false;
    }

    public List<Character> getCrossCheckList() {
        return crossCheckList;
    }

    private List<Character> generateCrossCheckList() {
        List<Character> copyList = new ArrayList<>();

        for (int i = 'a'; i <= 'z'; i++) {
            copyList.add((char) i);
        }

        return copyList;
    }

    public void resetCheckPlayDirection() {
        wordHorizontalCheck = true;
        wordVerticalCheck = true;
    }

    public void checkPlayDirection(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            wordHorizontalCheck = false;
        } else {
            wordVerticalCheck = false;
        }
    }

    public void initiateCrossChecks(PlayDirection playDirection) {
        AnchorType primaryAnchorType = anchor.getPrimaryAnchorType();
        AnchorType secondaryAnchorType = anchor.getSecondaryAnchorType();

        if (primaryAnchorType != null &&
                primaryAnchorType.getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
            if (playDirection == PlayDirection.HORIZONTAL) {
                crossCheckVertical = true;
            } else {
                crossCheckHorizontal = true;
            }
        }

        if (secondaryAnchorType != null &&
                secondaryAnchorType.getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
            if (playDirection == PlayDirection.HORIZONTAL) {
                crossCheckHorizontal = true;
            } else {
                crossCheckVertical = true;
            }
        }
    }

    public boolean getCrossCheck(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            return crossCheckHorizontal;
        } else {
            return crossCheckVertical;
        }
    }

    public boolean getCheckPlayDirection(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            return wordHorizontalCheck;
        } else {
            return wordVerticalCheck;
        }
    }

    public boolean checkEdge(CheckDirection checkDirection) {
        switch (checkDirection) {
            case RIGHT:
                return rightEdge;
            case LEFT:
                return leftEdge;
            case UP:
                return topEdge;
            case DOWN:
                return bottomEdge;
            default: // shouldn't occur, but just in case checkDirection is
                // null...
                return false;
        }
    }

    public boolean isTopEdge() {
        return topEdge;
    }

    public boolean isBottomEdge() {
        return bottomEdge;
    }

    public boolean isRightEdge() {
        return rightEdge;
    }

    public boolean isLeftEdge() {
        return leftEdge;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Character getLetter() {
        return letter;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public boolean isWordHorizontalCheck() {
        return wordHorizontalCheck;
    }

    public boolean isWordVerticalCheck() {
        return wordVerticalCheck;
    }

    public void setWordHorizontalCheck(boolean wordHorizontalCheck) {
        this.wordHorizontalCheck = wordHorizontalCheck;
    }

    public void setWordVerticalCheck(boolean wordVerticalCheck) {
        this.wordVerticalCheck = wordVerticalCheck;
    }

    @Override
    public String toString() {
        return twoChar;
    }

    /**
     * Sets the type of the board square (assumes the square is one of the
     * valid forms described in the rubric)
     */
    private void setBoardSquareType() {
        char firstChar = twoChar.charAt(0);
        char secondChar = ' '; // default value if there is a letter for the
        // word solver...

        if (!fullLetterSet.contains(firstChar)) {
            secondChar = twoChar.charAt(1);
        }

        if (firstChar != '.') {
            if (firstChar == '2') {
                boardSquareType = BoardSquareType.WORD_MULTIPLIER_2;
            } else if (firstChar == '3') {
                boardSquareType = BoardSquareType.WORD_MULTIPLIER_3;
            } else { // has to be a space, which means this square represents
                // a letter
                boardSquareType = BoardSquareType.LETTER;
                letter = firstChar;
                twoChar = " " + letter;
            }
        } else {
            if (secondChar == '2') {
                boardSquareType = BoardSquareType.LETTER_MULTIPLIER_2;
            } else if (secondChar == '3') {
                boardSquareType = BoardSquareType.LETTER_MULTIPLIER_3;
            } else { // has to be a period, which means this square has no
                // multiplier
                boardSquareType = BoardSquareType.NO_MULTIPLIER;
            }
        }
    }

    public BoardSquareType getBoardSquareType() {
        return boardSquareType;
    }
}

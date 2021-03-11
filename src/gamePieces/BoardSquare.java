package gamePieces;

import constants.BoardSquareType;

public class BoardSquare {
    private String twoChar;
    private Character letter;
    private BoardSquareType boardSquareType;
    private final int NUM_CHAR = 2;
    private int rowIndex;
    private int columnIndex;
    private boolean horizontalCheck;
    private boolean verticalCheck;

    public BoardSquare(String twoChar, int rowIndex, int columnIndex) {
        this.twoChar = twoChar;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        letter = null;

        setBoardSquareType();
        horizontalCheck = true;
        verticalCheck = true;
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

    public boolean isHorizontalCheck() {
        return horizontalCheck;
    }

    public boolean isVerticalCheck() {
        return verticalCheck;
    }

    public void setHorizontalCheck(boolean horizontalCheck) {
        this.horizontalCheck = horizontalCheck;
    }

    public void setVerticalCheck(boolean verticalCheck) {
        this.verticalCheck = verticalCheck;
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
        char secondChar = twoChar.charAt(1);

        if (firstChar != '.') {
            if (firstChar == '2') {
                boardSquareType = BoardSquareType.WORD_MULTIPLIER_2;
            } else if (firstChar == '3') {
                boardSquareType = BoardSquareType.WORD_MULTIPLIER_3;
            } else { // has to be a space, which means this square represents
                // a letter
                boardSquareType = BoardSquareType.LETTER;
                letter = secondChar;
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

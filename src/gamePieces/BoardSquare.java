package gamePieces;

import constants.BoardSquareType;

public class BoardSquare {
    private String square;
    private BoardSquareType boardSquareType;
    private final int NUM_CHAR = 2;
    private int rowIndex;
    private int columnIndex;
    private boolean horizontalCheck;
    private boolean verticalCheck;

    public BoardSquare(String square, int rowIndex, int columnIndex) {
        this.square = square;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;

        setBoardSquareType();
        horizontalCheck = true;
        verticalCheck = true;
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
        return square;
    }

    /**
     * Sets the type of the board square (assumes the square is one of the
     * valid forms described in the rubric)
     */
    private void setBoardSquareType() {
        char firstChar = square.charAt(0);
        char secondChar = square.charAt(1);

        if (firstChar != '.') {
            if (firstChar == '2') {
                boardSquareType = BoardSquareType.WORD_MULTIPLIER_2;
            } else if (firstChar == '3') {
                boardSquareType = BoardSquareType.WORD_MULTIPLIER_3;
            } else { // has to be a space, which means this square represents
                // a letter
                boardSquareType = BoardSquareType.LETTER;
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

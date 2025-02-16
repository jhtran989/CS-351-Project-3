package gamePieces;

import constants.*;
import wordSolver.MainWordSolver;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A meaty class that holds various information about a given board square
 * (range from position on the board, the letter (if any) it holds with the
 * corresponding Tile object, the check set used when finding possible words,
 * etc.)
 */
public class BoardSquare {
    private final int NUM_CHAR = 2;

    private String twoChar;
    private char letter;
    private TrueBoardSquareType boardSquareType;
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
    private final Set<Character> crossCheckSetHorizontal;
    private final Set<Character> crossCheckSetVertical;
    private boolean activeMultiplier;
    private Tile activeTile;
    Set<Character> fullLetterSet;
    private boolean crossCheckHorizontal;
    private boolean crossCheckVertical;

    // FIXME
    private List<CrossCheckWord> crossCheckWordList;
    private CrossCheckWord horizontalCrossCheckWord;
    private CrossCheckWord verticalCrossCheckWord;

    // Added a new member variable since there's a difference between a
    // "possible" (candidate) anchor square and an anchor square that's
    // actually valid to check (pertaining to if it is a valid anchor square
    // so there's no repetition)
    private boolean candidateAnchorSquare;

    // DEBUG
    private final boolean PARSE_BOARD_SQUARE = false;

    public BoardSquare(String twoChar, int rowIndex, int columnIndex,
                       int dimension, Set<Character> fullLetterSet) {
        this.twoChar = twoChar;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.dimension = dimension;
        letter = ' '; // changed from null so it could be printed out easier
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

        crossCheckSetHorizontal = generateCrossCheckSet();
        crossCheckSetVertical = generateCrossCheckSet();

        activeMultiplier = true;
        activeTile = null;

        horizontalCrossCheckWord = null;
        verticalCrossCheckWord = null;

        crossCheckHorizontal = false;
        crossCheckVertical = false;

        candidateAnchorSquare = false;
    }

    public int getCrossCheckWordIndex(PlayDirection wordPlayDirection) {
        if (wordPlayDirection == PlayDirection.HORIZONTAL) {
            return columnIndex;
        } else {
            return rowIndex;
        }
    }

    /**
     * Should be the only static method in this class...
     * Called only on special anchor squares - more specifically, on those
     * with multiple reference words (like when two words already on the board
     * form a cross, the four "corner" squares that touch both words)
     *
     * @param boardSquare
     */
    public static void mergeCrossCheckSets(BoardSquare boardSquare) {
        Set<Character> crossCheckSetHorizontal =
                boardSquare.getCrossCheckSet(
                        PlayDirection.HORIZONTAL);
        Set<Character> crossCheckSetVertical =
                boardSquare.getCrossCheckSet(
                        PlayDirection.VERTICAL);

        crossCheckSetHorizontal.retainAll(crossCheckSetVertical);
        crossCheckSetVertical.retainAll(crossCheckSetHorizontal);
    }

    public void printCrossChecks() {
        System.out.println("Cross checks - Horizontal: " +
                crossCheckHorizontal + ", " + "Vertical: " +
                crossCheckVertical);
    }

    public boolean getCrossCheck(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            return crossCheckHorizontal;
        } else {
            return crossCheckVertical;
        }
    }

    public Set<Character> getCrossCheckSet(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            return crossCheckSetVertical;
        } else {
            return crossCheckSetHorizontal;
        }
    }

    private Set<Character> generateCrossCheckSet() {
        Set<Character> copySet = new TreeSet<>();

        if (boardSquareType == TrueBoardSquareType.LETTER) {
            copySet.add(letter);
        } else {
            copySet.addAll(fullLetterSet);
        }

        return copySet;
    }

    public void initiateCrossChecks(PlayDirection playDirection) {
        AnchorType primaryAnchorType = anchor.getPrimaryAnchorType();
        AnchorType secondaryAnchorType = anchor.getSecondaryAnchorType();

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println();
            System.out.println("Word play direction " + playDirection);
            System.out.println(primaryAnchorType);
            System.out.println(secondaryAnchorType);
        }

        if (primaryAnchorType != null &&
                primaryAnchorType.getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
            if (boardSquareType != TrueBoardSquareType.LETTER) { // may seem
                // redundant, but it's here to check if the OUTSIDE ANCHOR
                // SQUARE isn't already covered by a tile (no use checking
                // since only one letter is applicable...)
                if (playDirection == PlayDirection.HORIZONTAL) {
                    crossCheckVertical = true;
                } else {
                    crossCheckHorizontal = true;
                }
            }
        }

        if (secondaryAnchorType != null &&
                secondaryAnchorType.getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
            if (boardSquareType != TrueBoardSquareType.LETTER) {
                if (playDirection == PlayDirection.HORIZONTAL) {
                    crossCheckHorizontal = true;
                } else {
                    crossCheckVertical = true;
                }
            }
        }
    }

    public void initiateCrossChecks(PlayDirection playDirection,
                                    AnchorType primaryAnchorType,
                                    AnchorType secondaryAnchorType) {
        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println();
            System.out.println("Word play direction " + playDirection);
            System.out.println(primaryAnchorType);
            System.out.println(secondaryAnchorType);
        }

        if (anchor.isInitialCrossCheck()) {
            anchor.turnOffInitialCrossCheck();
        } else {
            anchor.turnOnAnchorOverlap();
        }

        if (primaryAnchorType != null &&
                primaryAnchorType.getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
            if (boardSquareType != TrueBoardSquareType.LETTER) { // may seem
                // redundant, but it's here to check if the OUTSIDE ANCHOR
                // SQUARE isn't already covered by a tile (no use checking
                // since only one letter is applicable...)
                if (playDirection == PlayDirection.HORIZONTAL) {
                    crossCheckVertical = true;
                } else {
                    crossCheckHorizontal = true;
                }
            }
        }

        if (secondaryAnchorType != null &&
                secondaryAnchorType.getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
            if (boardSquareType != TrueBoardSquareType.LETTER) {
                if (playDirection == PlayDirection.HORIZONTAL) {
                    crossCheckHorizontal = true;
                } else {
                    crossCheckVertical = true;
                }
            }
        }
    }

    public CrossCheckWord getCrossCheckWord(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            return horizontalCrossCheckWord;
        } else {
            return verticalCrossCheckWord;
        }
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

    public void setActiveTile(Tile activeTile) {
        this.activeTile = activeTile;
    }

    public void removeActiveTile() {
        this.activeTile = null;
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
        boardSquareType = TrueBoardSquareType.LETTER;
        letter = tile.getLetter();

        activeTile = tile;
        activeMultiplier = false;

        twoChar = " " + letter;
    }

    public void resetCheckPlayDirection() {
        wordHorizontalCheck = true;
        wordVerticalCheck = true;
    }

    public void setCheckPlayDirection(PlayDirection playDirection) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            wordHorizontalCheck = false;
        } else {
            wordVerticalCheck = false;
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

    public char getLetter() {
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
        if (PARSE_BOARD_SQUARE) {
            System.out.println();
            System.out.println("Current board square: " + this + " row: "
                    + rowIndex + " column: " + columnIndex);
        }

        char firstChar = twoChar.charAt(0);
        char secondChar = ' '; // default value if there is a letter for the
        // word solver...

        if (!fullLetterSet.contains(Character.toLowerCase(firstChar))) {
            secondChar = twoChar.charAt(1);
        }

        // assuming the structure given in the project spec that the
        // multiplier will have a '.' in either of the two allotted spaces
        // for the board square
        if (firstChar != '.') {
//            if (firstChar == '2') {
//                boardSquareType = BoardSquareType.WORD_MULTIPLIER_2;
//            } else if (firstChar == '3') {
//                boardSquareType = BoardSquareType.WORD_MULTIPLIER_3;
//            } else { // has to be a letter since we're using next() from
//                // scanner (skips the spaces)
//                boardSquareType = TrueBoardSquareType.LETTER;
//                letter = firstChar;
//                twoChar = " " + letter;
//            }

            if (firstChar >= '1' && firstChar <= '9') {
                boardSquareType =
                        new TrueBoardSquareType(
                                MultiplierType.WORD_MULTIPLIER,
                                Character.getNumericValue(
                                        firstChar));
            } else {
                boardSquareType = TrueBoardSquareType.LETTER;
                letter = firstChar;
                twoChar = " " + letter;
            }
        } else {
            if (secondChar >= '1' && secondChar <= '9') {
                boardSquareType =
                        new TrueBoardSquareType(
                                MultiplierType.LETTER_MULTIPLIER,
                                Character.getNumericValue(
                                        secondChar));
            } else {
                boardSquareType = TrueBoardSquareType.NO_MULTIPLIER;
            }

//            if (secondChar == '2') {
//                boardSquareType = TrueBoardSquareType.LETTER_MULTIPLIER_2;
//            } else if (secondChar == '3') {
//                boardSquareType = TrueBoardSquareType.LETTER_MULTIPLIER_3;
//            } else { // has to be a period, which means this square has no
//                // multiplier
//                boardSquareType = BoardSquareType.NO_MULTIPLIER;
//            }
        }
    }

    public TrueBoardSquareType getBoardSquareType() {
        return boardSquareType;
    }

    public boolean isCandidateAnchorSquare() {
        return candidateAnchorSquare;
    }

    public void turnOnCandidateAnchorSquare() {
        candidateAnchorSquare = true;
    }

    public void printAnchorSquare() {
        printFullBoardSquareInfo();
        System.out.println("Anchor: " + letter + " Primary direction "
                + anchor.getPrimaryDirection() + " " +
                AnchorType.customToString(
                        anchor.getPrimaryAnchorType()) + " " +
                AnchorType.customToString(
                        anchor.getSecondaryAnchorType()));
        System.out.println("Left limit (Horizontal): " +
                anchor.getLeftLimit(PlayDirection.HORIZONTAL));
        System.out.println("Left limit (Vertical): " +
                anchor.getLeftLimit(PlayDirection.VERTICAL));
        System.out.println("Anchor overlap: " + anchor.isAnchorOverlap());
    }

    public void printFullBoardSquareInfo() {
        System.out.println("Letter " + letter + " row index: " + rowIndex +
                " column index: " + columnIndex + " Tile: " + activeTile);
    }

    public static void printFullBoardSquareInfo(BoardSquare boardSquare) {
        if (boardSquare == null) {
            System.out.println("Empty board square");
        } else {
            boardSquare.printFullBoardSquareInfo();
        }
    }
}

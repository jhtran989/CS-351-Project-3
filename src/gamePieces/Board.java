package gamePieces;

import comparators.TileComparator;
import constants.*;
import exceptions.InputErrorException;
import utilities.CustomParser;
import utilities.CyclicIndexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {
    private int dimension;
    private Map<Tile, Integer> frequencyTile;
    private BoardSquare[][] boardSquareArray;
    private InputChoice inputChoice;
    private List<WordInPlay> wordInPlayList;
    private int firstLetterIndex;
    private int lastLetterIndex;
    private int rowColumnLetterIndex;
    private List<BoardSquare> anchorBoardSquares;

    public Board(InputChoice inputChoice) {
        this.inputChoice = inputChoice;
        frequencyTile = new TreeMap<>(new TileComparator());
        wordInPlayList = new ArrayList<>();
        anchorBoardSquares = new ArrayList<>();

        if (this.inputChoice == InputChoice.FILE) {
            String boardFilePath = "resources/scrabble_board.txt";
            String tilesFilePath = "resources/scrabble_tiles.txt";
            setupBoard(boardFilePath);
            setupTiles(tilesFilePath);
        } else {
            setupBoard();
            setupTiles();
        }

        printBoard();
        printTiles();
    }

//    public Board() {
//        frequencyTile = new TreeMap<>(new TileComparator());
//
//        setupBoard();
//        setupTiles();
//
//        printBoard();
//        printTiles();
//    }

    public BoardSquare getBoardSquare(int rowIndex, int columnIndex) {
        return boardSquareArray[rowIndex][columnIndex];
    }

    public int getDimension() {
        return dimension;
    }

    private void setupBoard(String filePath) {
        try (Scanner scanner =
                     new Scanner(new FileReader(filePath))) {
            dimension = CustomParser.parseInt(scanner.nextLine());
            boardSquareArray = new BoardSquare[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    boardSquareArray[i][j] = new BoardSquare(
                            scanner.next(), i, j,
                            dimension);
                }

                scanner.nextLine();
            }
        } catch (InputErrorException | FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setupBoard() {
        try (Scanner scanner =
                     new Scanner(System.in)) {
            dimension = CustomParser.parseInt(scanner.nextLine());
            boardSquareArray = new BoardSquare[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    boardSquareArray[i][j] = new BoardSquare(
                            scanner.next(), i, j,
                            dimension);
                }

                scanner.nextLine();
            }
        } catch (InputErrorException inputErrorException) {
            System.out.println(inputErrorException.getMessage());
        }
    }

    private void setupTiles(String filePath) {
        try (Scanner scanner =
                     new Scanner(new FileReader(filePath))) {
            while (scanner.hasNextLine()) {
                char letter = CustomParser.parseChar(scanner.next());
                int value = CustomParser.parseInt(scanner.next());
                int frequency = CustomParser.parseInt(scanner.next());
                frequencyTile.put(new Tile(letter, value),
                        frequency);

                scanner.nextLine();
            }
        } catch (InputErrorException | FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setupTiles() {
        try (Scanner scanner =
                     new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                char letter = CustomParser.parseChar(scanner.next());
                int value = CustomParser.parseInt(scanner.next());
                int frequency = CustomParser.parseInt(scanner.next());
                frequencyTile.put(new Tile(letter, value),
                        frequency);

                scanner.nextLine();
            }
        } catch (InputErrorException inputErrorException) {
            System.out.println(inputErrorException .getMessage());
        }
    }

    private void printBoard() {
        System.out.println("Board:");

        for (BoardSquare[] boardSquares : boardSquareArray) {
            for (BoardSquare boardSquare : boardSquares) {
                System.out.print(boardSquare + " ");
            }

            System.out.println();
        }

        System.out.println();
    }

    private void printTiles() {
        System.out.println("Tiles");

        for (Map.Entry<Tile, Integer> tileEntry : frequencyTile.entrySet()) {
            System.out.println(tileEntry.getKey() + " (frequency: " +
                    tileEntry.getValue() + ")");
        }

        System.out.println();
    }

//    private void resetTurn() {
//
//    }
//
//    private void performInitialCrossChecks() {
//        for (BoardSquare anchorBoardSquare : anchorBoardSquares) {
//            for (PlayDirection playDirection : PlayDirection.values()) {
//                if (anchorBoardSquare.getCrossCheck(playDirection)) {
//
//                }
//            }
//        }
//    }
//
//    private void performWordCrossCheck() {
//
//    }
//
//    private String firstPartCrossCheck(BoardSquare anchorBoardSquare,
//                                PlayDirection playDirection) {
//        String firstPart = "";
//
//        BoardSquare previousBoardSquare =
//                getBoardSquareInCheckDirection(anchorBoardSquare,
//                        playDirection.getReverseCheckDirection());
//
//        while (previousBoardSquare != null
//                && previousBoardSquare.getBoardSquareType()
//                        == BoardSquareType.LETTER) {
//            firstPart = previousBoardSquare.getLetter() + firstPart;
//            previousBoardSquare =
//                    getBoardSquareInCheckDirection(anchorBoardSquare,
//                            playDirection
//                                    .getReverseCheckDirection());
//        }
//
//        return firstPart;
//    }
//
//    private String secondPartCrossCheck(BoardSquare anchorBoardSquare,
//                                      PlayDirection playDirection) {
//        String secondPart = "";
//
//        BoardSquare nextBoardSquare =
//                getBoardSquareInCheckDirection(anchorBoardSquare,
//                playDirection.getReverseCheckDirection());
//
//        while (nextBoardSquare != null
//                && nextBoardSquare.getBoardSquareType()
//                == BoardSquareType.LETTER) {
//            secondPart += nextBoardSquare.getLetter();
//            nextBoardSquare =
//                    getBoardSquareInCheckDirection(anchorBoardSquare,
//                            playDirection
//                                    .getReverseCheckDirection());
//        }
//
//        return secondPart;
//    }
//
//    /**
//     * The anchor squares found depending on the orientation of the word on
//     * the board -- thinking about it, the orientation might not matter, but
//     */
//    private void findInitialAnchorBoardSquares() {
//        for (WordInPlay wordInPlay : wordInPlayList) {
//            firstLetterIndex = wordInPlay.getFirstIndex();
//            lastLetterIndex = wordInPlay.getLastIndex();
//            rowColumnLetterIndex = wordInPlay.getRowColumnIndex();
//
//            List<BoardSquare> wordBoardSquareList = new ArrayList<>();
//            PlayDirection wordPlayDirection = wordInPlay.getPlayDirection();
//
//            // adds all the board squares that make up the word into the list
//            for (int letterIndex = firstLetterIndex;
//                 letterIndex <= lastLetterIndex; letterIndex++) {
//                if (wordPlayDirection == PlayDirection.HORIZONTAL) {
//                    wordBoardSquareList.add(
//                            boardSquareArray[rowColumnLetterIndex]
//                                    [letterIndex]);
//                } else {
//                    wordBoardSquareList.add(
//                            boardSquareArray[letterIndex]
//                                    [rowColumnLetterIndex]);
//                }
//            }
//
//            // add the inside anchor squares (only the first board square is
//            // in the primary direction, and the rest are used to check in
//            // the opposite play direction -- i.e. if the word is played
//            // horizontally, then the body anchors are checked vertically)
//            boolean first = true;
//            for (BoardSquare boardSquare : wordBoardSquareList) {
//                if (first) {
//                    addAnchorSquare(boardSquare,
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_CENTER_HEAD,
//                            AnchorType.SECONDARY_BODY);
//
//                } else {
//                    addAnchorSquare(boardSquare,
//                            wordPlayDirection,
//                            null,
//                            AnchorType.SECONDARY_BODY);
//                }
//
//                first = false;
//            }
//
//            // add the side body anchor squares (in the "buffer" region that
//            // doesn't contain a letter yet -- adjacent to the word and
//            // played along the same play direction)
//            first = true;
//            for (BoardSquare boardSquare : wordBoardSquareList) {
//                BoardSquare firstHeadBoardSquare =
//                        getBoardSquareInCheckDirection(
//                                boardSquare,
//                                wordPlayDirection
//                                        .getCheckDirection());
//
//                if (firstHeadBoardSquare == null) {
//                    break;
//                }
//
//                if (checkPrimaryBodyAnchor(
//                        firstHeadBoardSquare,
//                        wordPlayDirection)) {
//                    if (first) {
//                        addAnchorSquare(firstHeadBoardSquare,
//                                wordPlayDirection,
//                                AnchorType.PRIMATE_SIDE_HEAD,
//                                null);
//                    } else {
//                        addAnchorSquare(firstHeadBoardSquare,
//                                wordPlayDirection,
//                                AnchorType.PRIMARY_BODY,
//                                null);
//                    }
//                }
//
//                first = false;
//            }
//
//            first = true;
//            for (BoardSquare boardSquare : wordBoardSquareList) {
//                BoardSquare secondHeadBoardSquare =
//                        getBoardSquareInCheckDirection(
//                                boardSquare,
//                                wordPlayDirection
//                                        .getReverseCheckDirection());
//
//                if (secondHeadBoardSquare == null) {
//                    break;
//                }
//
//                if (checkPrimaryBodyAnchor(
//                        secondHeadBoardSquare,
//                        wordPlayDirection)) {
//                    if (first) {
//                        addAnchorSquare(secondHeadBoardSquare,
//                                wordPlayDirection,
//                                AnchorType.PRIMATE_SIDE_HEAD,
//                                null);
//                    } else {
//                        addAnchorSquare(boardSquare,
//                                wordPlayDirection,
//                                AnchorType.PRIMARY_BODY,
//                                null);
//                    }
//                }
//
//                first = false;
//            }
//
//            BoardSquare firstBoardSquare = wordBoardSquareList.get(0);
//            BoardSquare lastBoardSquare =
//                    wordBoardSquareList.get(wordBoardSquareList.size() - 1);
//
//            if (checkSecondaryEndAnchor(firstBoardSquare,
//                    wordPlayDirection.getReverseCheckDirection())) {
//                BoardSquare frontEnd =
//                        getBoardSquareInCheckDirection(
//                                firstBoardSquare,
//                                wordPlayDirection
//                                        .getReverseCheckDirection());
//
//                assert frontEnd != null;
//                addAnchorSquare(frontEnd,
//                        wordPlayDirection, null,
//                        AnchorType.SECONDARY_END);
//            }
//
//            if (checkSecondaryEndAnchor(lastBoardSquare,
//                    wordPlayDirection.getCheckDirection())) {
//                BoardSquare backEnd =
//                        getBoardSquareInCheckDirection(
//                                lastBoardSquare,
//                                wordPlayDirection
//                                        .getCheckDirection());
//
//                assert backEnd != null;
//                addAnchorSquare(backEnd,
//                        wordPlayDirection, null,
//                        AnchorType.SECONDARY_END);
//            }
//        }
//    }
//
//    private int findLeftLimitAnchor(BoardSquare boardSquare,
//                                    PlayDirection playDirection) {
//        int leftLimit = 0;
//        BoardSquare previousBoardSquare = getBoardSquareInCheckDirection(
//                boardSquare,
//                playDirection.getReverseCheckDirection());
//
//        while (previousBoardSquare != null) {
//            if (previousBoardSquare.getBoardSquareType()
//                    == BoardSquareType.LETTER) {
//                return leftLimit - 1; // so the left part doesn't touch
//                // another word
//            }
//
//            previousBoardSquare = getBoardSquareInCheckDirection(
//                    boardSquare,
//                    playDirection.getReverseCheckDirection());
//            leftLimit++;
//        }
//
//        return leftLimit;
//    }
//
//    /**
//     * Checks if there is a letter in the board two squares "behind" in in
//     * the word play direction (so when we're left extending, we don't
//     * over check with the word "behind" it)
//     * @param boardSquare
//     * @param playDirection
//     * @return
//     */
//    private boolean checkPrimaryBodyAnchor(BoardSquare boardSquare,
//                                           PlayDirection playDirection) {
//        BoardSquare previousBoardSquare = boardSquare;
//        for (int i = 0; i < 2; i++) {
//            previousBoardSquare =
//                    getBoardSquareInCheckDirection(
//                            previousBoardSquare,
//                            playDirection
//                                    .getReverseCheckDirection());
//            if (previousBoardSquare == null) {
//                return true;
//            } else if (previousBoardSquare.getBoardSquareType()
//                    == BoardSquareType.LETTER) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * Checks if the ends in the opposite play direction of the word are letters
//     * Note: the ends of the word cannot contain a letter (else, something
//     * might be wrong with the word search algorithm...)
//     *
//     * @param boardSquare
//     * @param checkDirection
//     * @return
//     */
//    private boolean checkSecondaryEndAnchor(BoardSquare boardSquare,
//                                            CheckDirection checkDirection) {
//        return !boardSquare.checkEdge(checkDirection);
//    }
//
//    /**
//     * Gets the board square in the said direction relative to a given
//     * board square
//     *
//     * @param boardSquare
//     * @param checkDirection
//     * @return
//     */
//    private BoardSquare getBoardSquareInCheckDirection(BoardSquare boardSquare,
//                                       CheckDirection checkDirection) {
//        if (checkDirection == CheckDirection.UP) {
//            if (!boardSquare.isTopEdge()) {
//                return boardSquareArray[boardSquare.getRowIndex() - 1]
//                        [boardSquare.getColumnIndex()];
//            }
//        } else if (checkDirection == CheckDirection.DOWN) {
//            if (!boardSquare.isBottomEdge()) {
//                return boardSquareArray[boardSquare.getRowIndex() + 1]
//                        [boardSquare.getColumnIndex()];
//            }
//        } else if (checkDirection == CheckDirection.LEFT) {
//            if (!boardSquare.isLeftEdge()) {
//                return boardSquareArray[boardSquare.getRowIndex()]
//                        [boardSquare.getColumnIndex() - 1];
//            }
//        } else { // Right
//            if (!boardSquare.isRightEdge()) {
//                return boardSquareArray[boardSquare.getRowIndex()]
//                        [boardSquare.getColumnIndex() + 1];
//            }
//        }
//
//        return null;
//    }
//
//    private void addAnchorSquare(BoardSquare boardSquare,
//                                 PlayDirection wordPlayDirection,
//                                 AnchorType primaryAnchorType,
//                                 AnchorType secondaryAnchorType) {
//        Anchor anchor = new Anchor(wordPlayDirection,
//                primaryAnchorType,
//                secondaryAnchorType);
//        boardSquare.setAnchor(anchor);
//        boardSquare.initiateCrossChecks(wordPlayDirection);
//
//        if (primaryAnchorType != null) {
//            anchor.setLeftLimit(findLeftLimitAnchor(boardSquare,
//                    wordPlayDirection));
//        }
//
//        anchorBoardSquares.add(boardSquare);
//    }
//
//    public void findWordsInPlay() {
//        for (int i = 0; i < dimension; i++) {
//            for (int j = 0; j < dimension; j++) {
//                BoardSquare currentBoardSquare = boardSquareArray[i][j];
//
//                if (currentBoardSquare.getBoardSquareType()
//                        == BoardSquareType.LETTER) {
//                    if (currentBoardSquare.isWordHorizontalCheck()) {
//                        checkWordAlongDirection(
//                                PlayDirection.HORIZONTAL,
//                                currentBoardSquare);
//                    }
//
//                    if (currentBoardSquare.isWordVerticalCheck()) {
//                        checkWordAlongDirection(
//                                PlayDirection.VERTICAL,
//                                currentBoardSquare);
//                    }
//                }
//            }
//        }
//    }
//
//
//    // FIXME: realized getting the next board square would probably be the
//    //  easiest method, but retained old design (wouldn't want to waste work,
//    //  right...)
//    /**
//     * Assuming there's no one letter word
//     * Also, the letters are formed from left to right or top to bottom (only
//     * one direction to check for each axis -- horizontal or vertical,
//     * respectively)
//     * @param playDirection
//     * @param boardSquare
//     */
//    private void checkWordAlongDirection(PlayDirection playDirection,
//                                         BoardSquare boardSquare) {
//        String temporaryWord = "" + boardSquare.getLetter();
//        BoardSquare nextBoardSquare;
//
//        setLetterIndices(playDirection, boardSquare);
//
////        RowColumn rowColumn = new RowColumn(
////                boardSquare.getRowIndex(),
////                boardSquare.getColumnIndex(), dimension);
//
//        boardSquare.checkPlayDirection(playDirection);
//
//        nextBoardSquare = getBoardSquareInCheckDirection(
//                boardSquare,
//                playDirection.getCheckDirection());
//
//        while (nextBoardSquare != null && nextBoardSquare
//                .getBoardSquareType()
//                == BoardSquareType.LETTER) {
//            nextBoardSquare.checkPlayDirection(playDirection);
//
//            temporaryWord += nextBoardSquare.getLetter();
////            rowColumn.applyCheckDirection(
////                    playDirection.getCheckDirection());
//            lastLetterIndex++;
//
//            nextBoardSquare = getBoardSquareInCheckDirection(
//                    boardSquare,
//                    playDirection.getCheckDirection());
//        }
//
//        if (temporaryWord.length() > 1) {
//            WordInPlay wordInPlay = new WordInPlay(playDirection,
//                    temporaryWord, firstLetterIndex,
//                    lastLetterIndex, rowColumnLetterIndex);
//            wordInPlayList.add(wordInPlay);
//
//            if (MainGamePieces.DEBUG) {
//                System.out.println(wordInPlay);
//            }
//        }
//    }
//
//    private void setLetterIndices(PlayDirection playDirection,
//                                  BoardSquare boardSquare) {
//        if (playDirection == PlayDirection.HORIZONTAL) {
//            firstLetterIndex = boardSquare.getColumnIndex();
//            lastLetterIndex = boardSquare.getColumnIndex();
//            rowColumnLetterIndex = boardSquare.getRowIndex();
//        } else {
//            firstLetterIndex = boardSquare.getRowIndex();
//            lastLetterIndex = boardSquare.getRowIndex();
//            rowColumnLetterIndex = boardSquare.getColumnIndex();
//        }
//    }
}

package gamePieces;

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
    private int rowColumnIndex;
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

    /**
     * The anchor squares found depending on the orientation of the word on
     * the board -- thinking about it, the orientation might not matter, but
     */
    private void findAnchorBoardSquares() {
        for (WordInPlay wordInPlay : wordInPlayList) {
            firstLetterIndex = wordInPlay.getFirstIndex();
            lastLetterIndex = wordInPlay.getLastIndex();
            rowColumnIndex = wordInPlay.getRowColumnIndex();

            BoardSquare firstBoardSquare;
            BoardSquare lastBoardSquare;

            if (wordInPlay.getPlayDirection() == PlayDirection.HORIZONTAL) {
                //FIXME
                //anchorBoardSquares.add(boardSquareArray[][]);
                firstBoardSquare =
                        boardSquareArray[rowColumnIndex][firstLetterIndex];
                lastBoardSquare =
                        boardSquareArray[rowColumnIndex][lastLetterIndex];

                BoardSquareType
            } else {

            }
        }
    }

    public void findWordsInPlay() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BoardSquare currentBoardSquare = boardSquareArray[i][j];

                if (currentBoardSquare.getBoardSquareType()
                        == BoardSquareType.LETTER) {
                    if (currentBoardSquare.isHorizontalCheck()) {
                        checkWordAlongDirection(
                                PlayDirection.HORIZONTAL,
                                currentBoardSquare);
                    }

                    if (currentBoardSquare.isVerticalCheck()) {
                        checkWordAlongDirection(
                                PlayDirection.VERTICAL,
                                currentBoardSquare);
                    }
                }
            }
        }
    }


    // FIXME: realized getting the next board square would probably be the
    //  easiest method, but retained old design (wouldn't want to waste work,
    //  right...)
    /**
     * Assuming there's no one letter word
     * Also, the letters are formed from left to right or top to bottom (only
     * one direction to check for each axis -- horizontal or vertical,
     * respectively)
     * @param playDirection
     * @param boardSquare
     */
    private void checkWordAlongDirection(PlayDirection playDirection,
                                         BoardSquare boardSquare) {
        String temporaryWord = "" + boardSquare.getLetter();
        BoardSquare nextBoardSquare;

        if (playDirection == PlayDirection.HORIZONTAL) {
            firstLetterIndex = boardSquare.getColumnIndex();
            lastLetterIndex = firstLetterIndex;
            rowColumnIndex = boardSquare.getRowIndex();

            boardSquare.setHorizontalCheck(false);

//            int nextLetterIndex = CyclicIndexer.findCyclicIndex(
//                    lastLetterIndex,
//                    CheckDirection.RIGHT.getRowCorrection(),
//                    dimension - 1);

            while (getNextBoardSquareType(CheckDirection.RIGHT)
                    == BoardSquareType.LETTER) {
                nextBoardSquare =
                        boardSquareArray[rowColumnIndex][lastLetterIndex];
                nextBoardSquare.setHorizontalCheck(false);

                applyPlayDirectionCorrection(CheckDirection.RIGHT);
                temporaryWord += nextBoardSquare.getLetter();

//                if (nextLetterIndex == dimension - 1) {
//                    break;
//                }
            }
        } else {
            firstLetterIndex = boardSquare.getRowIndex();
            lastLetterIndex = firstLetterIndex;
            rowColumnIndex = boardSquare.getColumnIndex();

            boardSquare.setVerticalCheck(false);

//            int nextLetterIndex = CyclicIndexer.findCyclicIndex(
//                    lastLetterIndex,
//                    CheckDirection.DOWN.getRowCorrection(),
//                    dimension + 1);

            while (getNextBoardSquareType(CheckDirection.DOWN)
                    == BoardSquareType.LETTER) {
                nextBoardSquare =
                        boardSquareArray[lastLetterIndex][rowColumnIndex];
                nextBoardSquare.setVerticalCheck(false);

                applyPlayDirectionCorrection(CheckDirection.DOWN);
                temporaryWord += nextBoardSquare.getLetter();

//                if (nextLetterIndex == dimension - 1) {
//                    break;
//                }
            }
        }

        if (temporaryWord.length() > 1) {
            WordInPlay wordInPlay = new WordInPlay(playDirection,
                    temporaryWord, firstLetterIndex,
                    lastLetterIndex, rowColumnIndex);
            wordInPlayList.add(wordInPlay);

            if (MainGamePieces.DEBUG) {
                System.out.println(wordInPlay);
            }
        }


//        if (playDirection == PlayDirection.HORIZONTAL) {
//            int currentLeftIndex;
//            int currentRightIndex;
//            int rowIndex = boardSquare.getRowIndex();
//
//            currentLeftIndex = boardSquare.getColumnIndex();
//            currentRightIndex = boardSquare.getColumnIndex();
//
//            int nextLeftIndex = currentLeftIndex - 1;
//            int nextRightIndex = currentRightIndex + 1;
//
//            while (boardSquareArray[rowIndex][nextLeftIndex]
//                    .getBoardSquareType() == BoardSquareType.LETTER) {
//                if (nextLeftIndex == 0) {
//                    applyPlayDirectionCorrection(CheckDirection.LEFT,
//                            boardSquare);
//                    break;
//                }
//
//                applyPlayDirectionCorrection(CheckDirection.LEFT,
//                        boardSquare);
//                nextLeftIndex--;
//            }
//
//            while (boardSquareArray[rowIndex][nextRightIndex]
//                    .getBoardSquareType() == BoardSquareType.LETTER) {
//                if (nextRightIndex == dimension - 1) {
//                    break;
//                }
//
//                applyPlayDirectionCorrection(CheckDirection.RIGHT,
//                        boardSquare);
//                nextRightIndex++;
//            }
//
//
//
//
//            return new WordInPlay(playDirection, )
//        } else {
//
//        }
    }

    /**
     * Returns the next BoardSquareType in the specified CheckDirection
     *
     * @param checkDirection direction to get the next Tile
     * @return BoardType of the next Tile in the specified CheckDirection;
     * returns null if the edge of the board is reached
     */
    private BoardSquareType getNextBoardSquareType(
            CheckDirection checkDirection) {
        int rowCorrection = checkDirection.getRowCorrection();
        int columnCorrection = checkDirection.getColumnCorrection();

//        int newRowIndex = CyclicIndexer.findCyclicIndex(
//                lastLetterIndex,
//                rowCorrection, dimension);
//        int newColumnIndex = CyclicIndexer.findCyclicIndex(
//                lastLetterIndex,
//                columnCorrection, dimension);

        IndexCode rowIndexCode =
                CyclicIndexer.findAbsoluteIndex(lastLetterIndex,
                rowCorrection, dimension);
        IndexCode columnIndexCode = CyclicIndexer.findAbsoluteIndex(
                lastLetterIndex,
                columnCorrection, dimension);

        if (rowIndexCode == IndexCode.OUT_OF_BOUNDS
                || columnIndexCode == IndexCode.OUT_OF_BOUNDS) {
            return null;
        } else {
            int newRowIndex = rowIndexCode.getIndex();
            int newColumnIndex = columnIndexCode.getIndex();

            return boardSquareArray[newRowIndex][newColumnIndex].
                    getBoardSquareType();
        }
    }

//    private BoardSquareType getNextBoardSquareType(
//            CheckDirection checkDirection, BoardSquare boardSquare) {
//        int currentRowIndex = boardSquare.getRowIndex();
//        int currentColumnIndex = boardSquare.getColumnIndex();
//        int rowCorrection = checkDirection.getRowCorrection();
//        int columnCorrection = checkDirection.getColumnCorrection();
//
//        int newRowIndex = CyclicIndexer.findCyclicIndex(
//                currentRowIndex,
//                rowCorrection, dimension);
//        int newColumnIndex = CyclicIndexer.findCyclicIndex(
//                currentColumnIndex,
//                columnCorrection, dimension);
//
//        return boardSquareArray[newRowIndex][newColumnIndex].
//                getBoardSquareType();
//    }

//    private void applyPlayDirectionCorrection(
//            CheckDirection checkDirection, BoardSquare boardSquare) {
//        boardSquare.setRowIndex(
//                boardSquare.getRowIndex() + checkDirection.getRowCorrection());
//        boardSquare.setColumnIndex(
//                boardSquare.getColumnIndex() +
//                        checkDirection.getColumnCorrection());
//
//
//    }

    private void applyPlayDirectionCorrection(
            CheckDirection checkDirection) {
        if (checkDirection == CheckDirection.RIGHT) {
            lastLetterIndex += checkDirection.getRowCorrection();
        } else if (checkDirection == CheckDirection.DOWN) {
            lastLetterIndex += checkDirection.getColumnCorrection();
        }
    }

    private void setupBoard(String filePath) {
        try (Scanner scanner =
                     new Scanner(new FileReader(filePath))) {
            dimension = CustomParser.parseInt(scanner.nextLine());
            boardSquareArray = new BoardSquare[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    boardSquareArray[i][j] = new BoardSquare(
                            scanner.next(), i, j);
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
                            scanner.next(), i, j);
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
}

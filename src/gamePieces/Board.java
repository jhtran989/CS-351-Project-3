package gamePieces;

import constants.BoardSquareType;
import constants.CheckDirection;
import constants.InputChoice;
import constants.PlayDirection;
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

    private void findAnchorBoardSquares() {
        for (WordInPlay wordInPlay : wordInPlayList) {
            firstLetterIndex = wordInPlay.getFirstIndex();
            lastLetterIndex = wordInPlay.getLastIndex();
            rowColumnIndex = wordInPlay.getRowColumnIndex();

            if (wordInPlay.getPlayDirection() == PlayDirection.HORIZONTAL) {
                //FIXME
                //anchorBoardSquares.add(boardSquareArray[][]);
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
                        checkLetterDirection(PlayDirection.HORIZONTAL,
                                currentBoardSquare);
                    } else if (currentBoardSquare.isVerticalCheck()) {
                        checkLetterDirection(PlayDirection.VERTICAL,
                                currentBoardSquare);
                    }
                }
            }
        }
    }

    // FIXME: use the correction from the CheckDirection instead (also, maybe
    //  put an error to the cyclic indexer...)

    /**
     * Assuming there's no one letter word
     * Also, the letters are formed from left to right or top to bottom (only
     * one direction to check for each axis -- horizontal or vertical,
     * respectively)
     * @param playDirection
     * @param boardSquare
     */
    private void checkLetterDirection(PlayDirection playDirection,
                                            BoardSquare boardSquare) {
        String temporaryWord = "" + boardSquare.getLetter();

        if (playDirection == PlayDirection.HORIZONTAL) {
            firstLetterIndex = boardSquare.getColumnIndex();
            lastLetterIndex = firstLetterIndex;
            rowColumnIndex = boardSquare.getRowIndex();

            int nextLetterIndex = CyclicIndexer.findCyclicIndex(
                    lastLetterIndex,
                    CheckDirection.RIGHT.getRowCorrection(),
                    dimension - 1);
            while (getNextBoardSquareType(CheckDirection.RIGHT)
                    == BoardSquareType.LETTER) {
                applyPlayDirectionCorrection(CheckDirection.RIGHT);
                temporaryWord +=
                        boardSquareArray[rowColumnIndex][nextLetterIndex]
                                .getLetter();

                if (nextLetterIndex == dimension - 1) {
                    break;
                }
            }
        } else {
            firstLetterIndex = boardSquare.getRowIndex();
            lastLetterIndex = firstLetterIndex;
            rowColumnIndex = boardSquare.getColumnIndex();

            int nextLetterIndex = CyclicIndexer.findCyclicIndex(
                    lastLetterIndex,
                    CheckDirection.DOWN.getRowCorrection(), dimension + 1);
            while (getNextBoardSquareType(CheckDirection.DOWN)
                    == BoardSquareType.LETTER) {
                applyPlayDirectionCorrection(CheckDirection.DOWN);
                temporaryWord +=
                        boardSquareArray[rowColumnIndex][nextLetterIndex]
                                .getLetter();

                if (nextLetterIndex == dimension - 1) {
                    break;
                }
            }
        }

        if (firstLetterIndex != lastLetterIndex) {
            wordInPlayList.add(new WordInPlay(playDirection,
                    temporaryWord, firstLetterIndex,
                    lastLetterIndex, rowColumnIndex));
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

    private BoardSquareType getNextBoardSquareType(
            CheckDirection checkDirection) {
        int rowCorrection = checkDirection.getRowCorrection();
        int columnCorrection = checkDirection.getColumnCorrection();

        int newRowIndex = CyclicIndexer.findCyclicIndex(
                lastLetterIndex,
                rowCorrection, dimension);
        int newColumnIndex = CyclicIndexer.findCyclicIndex(
                lastLetterIndex,
                columnCorrection, dimension);

        return boardSquareArray[newRowIndex][newColumnIndex].
                getBoardSquareType();
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

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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Board {
    private int dimension;
    private Map<Tile, Integer> frequencyTile;
    private BoardSquare[][] boardSquareArray;
    private InputChoice inputChoice;

    public Board(InputChoice inputChoice) {
        this.inputChoice = inputChoice;
        frequencyTile = new TreeMap<>(new TileComparator());

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

    public List<WordInPlay> findWordsInPlay() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BoardSquare currentBoardSquare = boardSquareArray[i][j];
                if (currentBoardSquare.getBoardSquareType()
                        == BoardSquareType.LETTER) {
                    if (currentBoardSquare.isHorizontalCheck()) {

                    }
                }
            }
        }
    }

    // FIXME: use the correction from the CheckDirection instead (also, maybe
    //  put an error to the cyclic indexer...)
    private WordInPlay checkLetterDirection(PlayDirection playDirection,
                                            BoardSquare boardSquare) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            int leftIndex;
            int rightIndex;
            int rowIndex = boardSquare.getRowIndex();

            leftIndex = boardSquare.getColumnIndex();
            rightIndex = boardSquare.getColumnIndex();

            boolean edgeLeftLetter = false;
            boolean edgeRightLetter = false;

            while (boardSquareArray[rowIndex][leftIndex].getBoardSquareType()
                    == BoardSquareType.LETTER) {
                if (leftIndex == 0) {
                    edgeLeftLetter = true;
                    break;
                } else {
                    leftIndex--;
                }
            }

            while (boardSquareArray[rowIndex][rightIndex].getBoardSquareType()
                    == BoardSquareType.LETTER) {
                if (rightIndex == dimension - 1) {
                    edgeRightLetter = true;
                    break;
                } else {
                    rightIndex++;
                }
            }

            if (!edgeLeftLetter) {
                leftIndex += 1;
            }

            if (!edgeRightLetter) {
                rightIndex -= 1;
            }

            return new WordInPlay(playDirection, )
        } else {

        }
    }

    private BoardSquareType getNextBoardSquareType(
            CheckDirection checkDirection, BoardSquare boardSquare) {
        int currentRowIndex = boardSquare.getRowIndex();
        int currentColumnIndex = boardSquare.getColumnIndex();
        int rowCorrection = checkDirection.getRowCorrection();
        int columnCorrection = checkDirection.getColumnCorrection();

        int newRowIndex = CyclicIndexer.findCyclicIndex(
                currentRowIndex,
                rowCorrection, dimension);
        int newColumnIndex = CyclicIndexer.findCyclicIndex(
                currentColumnIndex,
                columnCorrection, dimension);

        return boardSquareArray[newRowIndex][newColumnIndex].
                getBoardSquareType();
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

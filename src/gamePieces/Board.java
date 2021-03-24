package gamePieces;

import constants.*;
import exceptions.InputErrorException;
import utilities.CustomParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {
    private int dimension;
    private BoardSquare[][] boardSquareArray;
    private InputChoice inputChoice;
    private TileBag tileBag;
    private Scanner scanner;

    public Board(InputChoice inputChoice, TileBag tileBag, Scanner scanner) {
        this.inputChoice = inputChoice;
        this.tileBag = tileBag;
        this.scanner = scanner;

        if (this.inputChoice == InputChoice.FILE) {
            String boardFilePath = "resources/scrabble_board.txt";
            setupBoard(boardFilePath);
        } else {
            setupBoard();
        }

        printBoard();
    }

    public TileBag getTileBag() {
        return tileBag;
    }

    public Board(String boardFilePath, TileBag tileBag, Scanner scanner) {
        this.tileBag = tileBag;
        this.scanner = scanner;

        setupBoard(boardFilePath);

        printBoard();
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
                    String currentString = scanner.next();

//                    if (MainWordSolver.DEBUG) {
//                        System.out.println(currentString);
//                    }

                    boardSquareArray[i][j] = new BoardSquare(
                            currentString, i, j,
                            dimension,
                            tileBag.getFullLetterSet());

                    if (boardSquareArray[i][j].getBoardSquareType()
                            == BoardSquareType.LETTER) {
                        Tile tile =
                                tileBag.findTileInFrequencyMap(
                                        boardSquareArray[i][j]
                                                .getLetter());
                        boardSquareArray[i][j].placeTile(tile);
                    }
                }

                scanner.nextLine();

//                if (i != dimension - 1) {
//                    scanner.nextLine();
//                }
            }
        } catch (InputErrorException | FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setupBoard() {
        try {
            dimension = CustomParser.parseInt(scanner.nextLine());
            boardSquareArray = new BoardSquare[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    boardSquareArray[i][j] = new BoardSquare(
                            scanner.next(), i, j,
                            dimension,
                            tileBag.getFullLetterSet());
                }

                scanner.nextLine();

//                if (i != dimension - 1) {
//                    scanner.nextLine();
//                }
            }
        } catch (InputErrorException inputErrorException) {
            System.out.println(inputErrorException.getMessage());
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

        //System.out.println();
    }
}

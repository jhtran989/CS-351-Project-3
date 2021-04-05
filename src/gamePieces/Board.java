package gamePieces;

import constants.*;
import exceptions.InputErrorException;
import utilities.CustomParser;
import wordSolver.MainWordSolver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * The class that holds the information to construct the board as a 2D array
 * of BoardSquare objects
 */
public class Board {
    private int dimension;
    private BoardSquare[][] boardSquareArray;
    private InputChoice inputChoice;
    private final TileBag tileBag;
    private Scanner scanner;

    private final boolean PRINT_BOARD = false;

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

        if (PRINT_BOARD) {
            printBoard();
        }
    }

    public Board(TileBag tileBag) {
        this.tileBag = tileBag;

        setupBoard(new InputStreamReader(
                MainWordSolver.class.getResourceAsStream(
                        "/scrabble_board.txt")));

        if (PRINT_BOARD) {
            printBoard();
        }
    }

    public Board(String boardFilePath, TileBag tileBag, Scanner scanner) {
        this.tileBag = tileBag;
        this.scanner = scanner;

        setupBoard(boardFilePath);

        if (PRINT_BOARD) {
            printBoard();
        }
    }

    public Board(TileBag tileBag, Scanner scanner) {
        this.tileBag = tileBag;
        this.scanner = scanner;

        setupBoard();

        if (PRINT_BOARD) {
            printBoard();
        }
    }

    public TileBag getTileBag() {
        return tileBag;
    }

    public BoardSquare getBoardSquare(int rowIndex, int columnIndex) {
        return boardSquareArray[rowIndex][columnIndex];
    }

    public int getDimension() {
        return dimension;
    }

    private void setupBoard(InputStreamReader inputStreamReader) {
        try (Scanner scanner =
                     new Scanner(inputStreamReader)) {
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
                            == TrueBoardSquareType.LETTER) {
                        Tile tile =
                                tileBag.findTileInFrequencyMap(
                                        boardSquareArray[i][j]
                                                .getLetter());
                        boardSquareArray[i][j].placeTile(tile);

                        if (MainWordSolver.BOARD_SETUP) {
                            System.out.println();
                            System.out.println("Setting up letter tile...");
                            boardSquareArray[i][j].printFullBoardSquareInfo();
                            System.out.println("Tile: " + tile);
                        }
                    }
                }

                scanner.nextLine();
            }
        } catch (InputErrorException exception) {
            System.out.println(exception.getMessage());
        }
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
                            == TrueBoardSquareType.LETTER) {
                        Tile tile =
                                tileBag.findTileInFrequencyMap(
                                        boardSquareArray[i][j]
                                                .getLetter());
                        boardSquareArray[i][j].placeTile(tile);

                        if (MainWordSolver.BOARD_SETUP) {
                            System.out.println();
                            System.out.println("Setting up letter tile...");
                            boardSquareArray[i][j].printFullBoardSquareInfo();
                            System.out.println("Tile: " + tile);
                        }
                    }
                }

                scanner.nextLine();
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

                    if (boardSquareArray[i][j].getBoardSquareType()
                            == TrueBoardSquareType.LETTER) {
                        Tile tile =
                                tileBag.findTileInFrequencyMap(
                                        boardSquareArray[i][j]
                                                .getLetter());

                        if (MainWordSolver.BOARD_SETUP) {
                            System.out.println();
                            System.out.println("Setting up tile");
                        }

                        boardSquareArray[i][j].placeTile(tile);

                        if (MainWordSolver.BOARD_SETUP) {
                            System.out.println();
                            System.out.println("Setting up letter tile...");
                            boardSquareArray[i][j].printFullBoardSquareInfo();
                            System.out.println("Tile: " + tile);
                        }
                    }
                }

                scanner.nextLine();
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
    }

    public void printBoardGUI() {
        System.out.println("Board");

        for (BoardSquare[] boardSquares : boardSquareArray) {
            for (BoardSquare boardSquare : boardSquares) {
                System.out.print((boardSquare.getActiveTile() == null ? 0 :
                        1) + " ");
            }

            System.out.println();
        }
    }

    @Override
    public String toString() {
        String boardString = "";
        int columnIndex = 0;
        int rowIndex = 0;

        for (BoardSquare[] boardSquares : boardSquareArray) {
            for (BoardSquare boardSquare : boardSquares) {
                if (columnIndex != 0) {
                    boardString += " ";
                }

                boardString += boardSquare;

                columnIndex++;
            }

            if (rowIndex != dimension - 1) {
                boardString += "\n";
            }

            columnIndex = 0;
            rowIndex++;
        }

        return boardString;
    }
}

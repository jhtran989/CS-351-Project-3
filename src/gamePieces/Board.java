package gamePieces;

import exceptions.InputErrorException;
import utilities.CustomParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Board {
    private Map<Tile, Integer> frequencyTile;
    private BoardSquare[][] boardSquareArray;
    private int dimension;

    public Board(int dimension) {
        this.dimension = dimension;
        frequencyTile = new TreeMap<>(new TileComparator());

        String boardFilePath = "resources/scrabble_board.txt";
        String tilesFilePath = "resources/scrabble_tiles.txt";
        setupBoard(boardFilePath);
        setupTiles(tilesFilePath);

        printBoard();
        printTiles();
    }

    private void setupBoard(String filePath) {
        try (Scanner scanner =
                     new Scanner(new FileReader(filePath))) {
            dimension = CustomParser.parseInt(scanner.nextLine());
            boardSquareArray = new BoardSquare[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    boardSquareArray[i][j] = new BoardSquare(
                            scanner.next());
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
                            scanner.next());
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

package gui;

import constants.MultiplierType;
import constants.TrueBoardSquareType;
import gamePieces.*;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import players.ComputerPlayer;
import players.HumanPlayer;
import players.Player;
import wordSearch.WordSearchTrie;
import wordSolver.MainWordSolver;
import wordSolver.WordSolver;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrabbleGUIController {
    private static final boolean GUI_DEBUG = false;
    private static final boolean BOARD_GUI = true;
    public static final boolean CROSS_CHECK_GUI = true;
    public static final boolean COMPUTER_TILE_GUI = true;
    public static final boolean PRINT_COMPUTER_GUI_SOLUTION = true;

    @FXML
    Pane pane;

    @FXML
    HBox humanRackHBox;

    @FXML
    VBox temporaryComputerRackVBox;

    @FXML
    Button humanPlayButton;

    @FXML
    Button humanTileRemoveButton;

    @FXML
    Label humanWordLabel;

    @FXML
    Label humanScoreLabel;

    @FXML
    Label computerWordLabel;

    @FXML
    Label computerScoreLabel;

    // The grid used is a square, so the weight is the same as the height
    private int gridSize = 900; // fixed to 900 x 900
    private int gridDimension = 15; // fixed to 15 x 15 board
    private int gridSquareSize = gridSize / gridDimension;
    private double xCorrection;
    private double yCorrection;
    private double gridSquareCenter = gridSquareSize / 2;

    private TileBag tileBag;
    private Board board;
    private WordSearchTrie wordSearchTrie;
    private WordSolver wordSolver;
    private Player humanPlayer;
    private Player computerPlayer;

    private List<TileGUIPiece> humanRackTileGUIList = new ArrayList<>();
    private List<ImageView> humanRackTileImageViewList;
    private Rack humanRack;

    private GuiStuff guiStuff;
    private boolean firstTurn = true;

    private int vboxIndexCorrectionIndex = 0;

    @FXML
    public void initialize() {
//        xCorrection = pane.getLayoutX();
//        yCorrection = pane.getLayoutY();
//
//        printPanePosition();

        tileBag = new TileBag(
                new InputStreamReader(
                        MainWordSolver.class.getResourceAsStream(
                                "/scrabble_tiles.txt")));
        board = new Board(tileBag);
        wordSearchTrie =
                new WordSearchTrie(new InputStreamReader(
                        MainWordSolver.class.getResourceAsStream(
                                "/sowpods.txt")), tileBag);

        humanPlayer = new HumanPlayer(board, tileBag,
                null);
        computerPlayer = new ComputerPlayer(board, tileBag,
                null);

        // assume that the human player ALWAYS goes first, initializing the
        // rack with the human player's rack
        // TODO: switch racks when changing active player...
        wordSolver = new WordSolver(board,
                wordSearchTrie, humanPlayer.getRack());
        humanPlayer.setWordSolver(wordSolver);
        computerPlayer.setWordSolver(wordSolver);

        // Create the GUI object
        guiStuff = new GuiStuff(humanRackHBox,
                humanWordLabel, humanScoreLabel,
                computerWordLabel,
                computerScoreLabel);

        // Follows the color convention given in the project spec
        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {
                Rectangle rectangle = new Rectangle(i * gridSquareSize,
                        j * gridSquareSize, gridSquareSize,
                        gridSquareSize);

                TrueBoardSquareType trueBoardSquareType =
                        board.getBoardSquare(i, j)
                                .getBoardSquareType();
                MultiplierType multiplierType =
                        trueBoardSquareType.getMultiplierType();

                if (multiplierType != MultiplierType.NO_MULTIPLIER) {
                    if (multiplierType == MultiplierType.LETTER_MULTIPLIER) {
                        int letterMultiplier =
                                trueBoardSquareType.getLetterMultiplier();

                        if (letterMultiplier == 2) {
                            rectangle.setFill(Color.LIGHTBLUE);
                        } else if (letterMultiplier == 3) {
                            rectangle.setFill(Color.DARKBLUE);
                        }
                    } else { // has to be a word multiplier
                        int wordMultiplier =
                                trueBoardSquareType.getWordMultiplier();

                        if (wordMultiplier == 2) {
                            rectangle.setFill(Color.PINK);
                        } else if (wordMultiplier == 3) {
                            rectangle.setFill(Color.RED);
                        }
                    }
                } else {
                    rectangle.setFill(Color.ROSYBROWN);
                }

                rectangle.setStroke(Color.BLACK);
                pane.getChildren().add(rectangle);
            }
        }

        // FIXME: "REMOVE" button DEBUG
        ImageView lastTileImageInRack = null;

        //humanRackTileImageViewList = humanPlayer.getRackImages();
        humanRack = humanPlayer.getRack();
        for (Tile tile: humanRack.getRackMap().keySet()) {
            ImageView currentTileImageView = tile.getTileImage();
            lastTileImageInRack = currentTileImageView;

            humanRackHBox.getChildren().add(currentTileImageView);

            TileGUIPiece tileGUIPiece = new TileGUIPiece(0, 0,
                    gridSquareSize, tile,
                    currentTileImageView);
            humanRackTileGUIList.add(tileGUIPiece);
            tileGUIPiece.draw();

            currentTileImageView.setOnMousePressed(event -> mouseClicked(
                    event,
                    tileGUIPiece));
            currentTileImageView.setOnMouseDragged(event -> mouseDragged(
                    event,
                    tileGUIPiece));
            currentTileImageView.setOnMouseReleased(event -> mouseReleased(
                    event,
                    tileGUIPiece));
        }

        humanPlayButton.setOnAction(event -> humanPlay());
        ImageView finalLastTileImageInRack = lastTileImageInRack;
        humanTileRemoveButton.setOnAction(event -> humanRemoveTile(
                finalLastTileImageInRack));

        //humanRackHBox.getChildren().addAll(humanPlayer.getRackImages());
    }

    private void mouseClicked(MouseEvent mouseEvent,
                              TileGUIPiece tileGUIPiece) {
        //TODO: set on board (active tile) and word solver...
        // FIXME: maybe change -1 into a constant...
        if (!(tileGUIPiece.getRowIndex() == -1
                || tileGUIPiece.getColumnIndex() == -1)) {
            board.getBoardSquare(tileGUIPiece.getRowIndex(),
                    tileGUIPiece
                            .getColumnIndex()).setActiveTile(null);
        }

        // FIXME
        if (BOARD_GUI) {
            System.out.println(tileGUIPiece + " clicked");
        }
    }

    private void mouseDragged(MouseEvent mouseEvent,
                              TileGUIPiece tileGUIPiece) {
        //printTileGUI(tileGUIPiece.getTileImage());
        ImageView tileImageView = tileGUIPiece.getTileImage();
        Bounds tileBounds = tileImageView.localToScene(
                tileImageView.getBoundsInLocal());

//        tileGUIPiece.setX(tileBounds.getMinX() + mouseEvent.getX());
//        tileGUIPiece.setY(tileBounds.getMinY() + mouseEvent.getY());

        // So when moving the tile, the center of the tile moves with the
        // mouse (correction with center of square from the LEFT CORNER)
        tileGUIPiece.setX(tileGUIPiece.getX() + mouseEvent.getX()
                - gridSquareCenter);
        tileGUIPiece.setY(tileGUIPiece.getY() + mouseEvent.getY()
                - gridSquareCenter);
        tileGUIPiece.draw();
    }

    private void mouseReleased(MouseEvent mouseEvent,
                               TileGUIPiece tileGUIPiece) {
        printPanePosition();
        printTileImageView(tileGUIPiece.getTileImage());
        printTileGUI(tileGUIPiece);

        ImageView tileImageView = tileGUIPiece.getTileImage();

        Bounds paneBounds = pane.localToScene(
                pane.getBoundsInLocal());
        Bounds tileBounds = tileImageView.localToScene(
                tileImageView.getBoundsInLocal());

        double tileX = tileBounds.getMinX() + gridSquareCenter;
        double tileY = tileBounds.getMinY() + gridSquareCenter;

        xCorrection = paneBounds.getMinX();
        yCorrection = paneBounds.getMinY();

        // Accounts for correction when moving relative to the CENTER of the
        // tile
        double gridXIndex = (tileX - xCorrection) / gridSquareSize;
        double gridYIndex = (tileY - yCorrection) / gridSquareSize;

        int closestGridXIndex =
                (int) (tileX - xCorrection) / gridSquareSize;
        int closestGridYIndex =
                (int) (tileY - yCorrection) / gridSquareSize;

        // Locks the tile to one of the grids (if outside the board, finds
        // the closest one)
        if (closestGridXIndex >= gridDimension) {
            closestGridXIndex = gridDimension - 1;
        } else if (closestGridXIndex < 0) {
            closestGridXIndex = 0;
        }

        if (closestGridYIndex >= gridDimension) {
            closestGridYIndex = gridDimension - 1;
        } else if (closestGridYIndex < 0) {
            closestGridYIndex = 0;
        }

        // Accounts for correction when moving relative to the CENTER of the
        // tile
        double xDisplacement =
                (closestGridXIndex - gridXIndex) * gridSquareSize
                        + gridSquareCenter;
        double yDisplacement =
                (closestGridYIndex - gridYIndex) * gridSquareSize
                        + gridSquareCenter;

//        tileGUIPiece.setX(gridSquareCenter + closestGridXIndex * gridSquareSize - tileX);
//        tileGUIPiece.setY(gridSquareCenter + closestGridYIndex * gridSquareSize - tileY);
        tileGUIPiece.setX(xDisplacement + tileGUIPiece.getX());
        tileGUIPiece.setY(yDisplacement + tileGUIPiece.getY());
        tileGUIPiece.draw();

        // sets the corresponding index on the board object (y - row, x -
        // column)
        tileGUIPiece.setRowIndex(closestGridYIndex);
        tileGUIPiece.setColumnIndex(closestGridXIndex);

        // sets the corresponding board square with the current tile being
        // released
        board.getBoardSquare(tileGUIPiece.getRowIndex(),
                tileGUIPiece.getColumnIndex()).setActiveTile(
                        tileGUIPiece.getTile());

        printTileImageView(tileGUIPiece.getTileImage());
        printTileGUI(tileGUIPiece);

        printBoardGUI();
    }

    private void humanPlay() {
        if (BOARD_GUI) {
            System.out.println();
            System.out.println("First turn: " + firstTurn);
        }

        Map.Entry<WordInPlay, Integer> humanWord =
                wordSolver.performHumanInitialPreliminarySearch(
                        firstTurn);

        if (firstTurn) {
            firstTurn = false;
        }

        if (humanWord != null) {
            //wordSolver.setRack(computerPlayer.getRack());
            guiStuff.update(humanPlayer, humanWord);
            humanPlayer.refreshRack();
            updateHumanRackGUI();

            Alert humanPlayAlert = new Alert(
                    Alert.AlertType.CONFIRMATION);
            humanPlayAlert.setContentText("You played "
                    + humanWord.getKey().getWord() + " and "
                    + "scored " + humanWord.getValue() + " points!");
            humanPlayAlert.setTitle("User Move");
            humanPlayAlert.show();

            wordSolver.setRack(computerPlayer.getRack());
            Map.Entry<WordInPlay, Integer> computerWord =
                    wordSolver.generateInitialHighestScoringMoveGUI();

            //FIXME: update computer's tile on board...
            List<TileGUIPiece> computerActiveTileGUIList =
                    wordSolver.placeSolutionOnBoardGUI(computerWord,
                    gridSquareSize);
            wordSolver.printOfficialSolution(computerWord);
            updateComputerBoardTilesGUI(computerActiveTileGUIList);

            guiStuff.update(computerPlayer,
                    computerWord);
            computerPlayer.refreshRack();

            Alert computerPlayAlert = new Alert(
                    Alert.AlertType.CONFIRMATION);
            computerPlayAlert.setContentText("The computer played "
                    + computerWord.getKey().getWord() + " and "
                    + "scored " + computerWord.getValue() + " points!");
            computerPlayAlert.setTitle("Computer Move");
            computerPlayAlert.show();

            wordSolver.setRack(humanRack);

            if (BOARD_GUI) {
                System.out.println();
                System.out.println("Final board:");
                System.out.println(board);
            }
        } else {
            Alert invalidPlayWord = new Alert(Alert.AlertType.WARNING);
            invalidPlayWord.setContentText("Sorry, please play only one word!");
            invalidPlayWord.setTitle("Invalid Word");
            invalidPlayWord.show();
        }
    }

    private void updateHumanRackGUI() {
        Map<Tile, Character> newHumanRack = humanPlayer.getNewHumanRackMap();
        for (Tile tile : newHumanRack.keySet()) {
            ImageView currentTileImageView = tile.getTileImage();

            humanRackHBox.getChildren().add(currentTileImageView);

            TileGUIPiece tileGUIPiece = new TileGUIPiece(0, 0,
                    gridSquareSize, tile,
                    currentTileImageView);
            humanRackTileGUIList.add(tileGUIPiece);
            tileGUIPiece.draw();

            currentTileImageView.setOnMousePressed(event -> mouseClicked(
                    event,
                    tileGUIPiece));
            currentTileImageView.setOnMouseDragged(event -> mouseDragged(
                    event,
                    tileGUIPiece));
            currentTileImageView.setOnMouseReleased(event -> mouseReleased(
                    event,
                    tileGUIPiece));
        }
    }

    private void updateComputerBoardTilesGUI(List<TileGUIPiece>
                                                     tileGUIPieceList) {
        for (TileGUIPiece tileGUIPiece : tileGUIPieceList) {
            ImageView currentImageView = tileGUIPiece
                    .getTileImage();
            temporaryComputerRackVBox.getChildren().add(currentImageView);
            tileGUIPiece.draw();
        }

        // FIXME: split...
        for (TileGUIPiece tileGUIPiece : tileGUIPieceList) {
            ImageView currentImageView = tileGUIPiece
                    .getTileImage();
            Bounds paneBounds = pane.localToScene(
                    pane.getBoundsInLocal());
            Bounds tileBounds = currentImageView.localToScene(
                    currentImageView.getBoundsInLocal());

            // FIXME
            if (COMPUTER_TILE_GUI) {
                System.out.println();
                System.out.println("Tile Bounds");
                System.out.println(tileBounds);
            }

            int imageXIndex = tileGUIPiece.getColumnIndex();
            int imageYIndex = tileGUIPiece.getRowIndex();

            // FIXME
            printComputerTileGUI(tileGUIPiece);

            int boardXPosition = imageXIndex * gridSquareSize;
            int boardYPosition = imageYIndex * gridSquareSize;

            double xDisplacement = paneBounds.getMinX() - tileBounds.getMinX();
            double yDisplacement =
                    paneBounds.getMinY() - tileBounds.getMinY()
                            - vboxIndexCorrectionIndex * gridSquareSize;
            // REALLY important (since bounds only work RELATIVE TO VBOX or
            // in general, the parent node)

            tileGUIPiece.setX(xDisplacement + boardXPosition
                    - paneBounds.getMinX());
            tileGUIPiece.setY(yDisplacement + boardYPosition
                    - paneBounds.getMinY());
            // ALSO REALLY IMPORTANT (displace and board positions calculated
            // above are RELATIVE to the board (pane), so we need to account
            // for the relative position of the pane relative to the scene)

            tileGUIPiece.draw();

            vboxIndexCorrectionIndex++;
        }
    }

    private void humanRemoveTile(ImageView imageView) {
        humanRackHBox.getChildren().remove(imageView);
    }

    // DEBUG stuff will be at the end of the file
    private void printPanePosition() {
        if (GUI_DEBUG) {
            System.out.println();
            System.out.println("Pane");
            System.out.println("Translate");
            System.out.println("X: " + pane.getTranslateX());
            System.out.println("Y: " + pane.getTranslateY());
            System.out.println("Layout");
            System.out.println("X: " + pane.getLayoutX());
            System.out.println("Y: " + pane.getLayoutY());

            Bounds bounds =
                    pane.localToScene(
                            pane.getBoundsInLocal());
            System.out.println("Bounds");
            System.out.println(bounds);
        }
    }

    private void printTileGUI(TileGUIPiece tileGUIPiece) {
        if (GUI_DEBUG) {
            System.out.println();
            System.out.println("Tile GUI");
            System.out.println("X: " + tileGUIPiece.getX());
            System.out.println("Y: " + tileGUIPiece.getY());
        }
    }

    private void printTileImageView(ImageView imageView) {
        if (GUI_DEBUG) {
            System.out.println();
            System.out.println("Tile");
            System.out.println("Raw");
            System.out.println("X: " + imageView.getX());
            System.out.println("Y: " + imageView.getY());
            System.out.println("Translate");
            System.out.println("X: " + imageView.getTranslateX());
            System.out.println("Y: " + imageView.getTranslateY());
            System.out.println("Layout");
            System.out.println("X: " + imageView.getLayoutX());
            System.out.println("Y: " + imageView.getLayoutY());

            Bounds bounds =
                    imageView.localToScene(
                            imageView.getBoundsInLocal());
            System.out.println("Bounds");
            System.out.println(bounds);

//            Point2D point2D = imageView.localToScreen(
//                    -imageView.getLayoutX(),
//                    -imageView.getLayoutY());
//            System.out.println("Point");
//            System.out.println(point2D);
        }
    }

    private void printBoardGUI() {
        // prints out the active tiles on the board
        if (BOARD_GUI) {
            System.out.println();
            board.printBoardGUI();
        }
    }

    private void printComputerTileGUI(TileGUIPiece tileGUIPiece) {
        if (COMPUTER_TILE_GUI) {
            System.out.println();
            System.out.println("Computer tile");
            System.out.println("row index: " + tileGUIPiece.getRowIndex());
            System.out.println("column index: " + tileGUIPiece.getColumnIndex());
        }
    }
}

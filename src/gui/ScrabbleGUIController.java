package gui;

import constants.MultiplierType;
import constants.TrueBoardSquareType;
import gamePieces.Board;
import gamePieces.TileBag;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import players.ComputerPlayer;
import players.HumanPlayer;
import players.Player;

import java.util.ArrayList;
import java.util.List;

public class ScrabbleGUIController {
    private static final boolean GUI_DEBUG = false;

    @FXML
    Pane pane;

    @FXML
    HBox humanRackHBox;

    // The grid used is a square, so the weight is the same as the height
    private int gridSize = 900; // fixed to 900 x 900
    private int gridDimension = 15; // fixed to 15 x 15 board
    private int gridSquareSize = gridSize / gridDimension;
    private double xCorrection;
    private double yCorrection;
    private double gridSquareCenter = gridSquareSize / 2;

    private Player humanPlayer;
    private Player computerPlayer;

    private List<TileGUIPiece> humanRackGUI = new ArrayList<>();

    @FXML
    public void initialize() {
//        xCorrection = pane.getLayoutX();
//        yCorrection = pane.getLayoutY();
//
//        printPanePosition();

        TileBag tileBag = new TileBag();
        Board board = new Board(tileBag);

        humanPlayer = new HumanPlayer(board, tileBag);
        computerPlayer = new ComputerPlayer(board, tileBag);

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

        List<ImageView> currentHumanRackImages = humanPlayer.getRackImages();
        for (ImageView imageView : currentHumanRackImages) {
            humanRackHBox.getChildren().add(imageView);

            TileGUIPiece tileGUIPiece = new TileGUIPiece(0, 0,
                    gridSquareSize, imageView);
            humanRackGUI.add(tileGUIPiece);
            tileGUIPiece.draw();

            imageView.setOnMouseClicked(event -> mouseClicked(event,
                    tileGUIPiece));
            imageView.setOnMouseDragged(event -> mouseDragged(event,
                    tileGUIPiece));
            imageView.setOnMouseReleased(event -> mouseReleased(event,
                    tileGUIPiece));
        }

        //humanRackHBox.getChildren().addAll(humanPlayer.getRackImages());
    }

    private void mouseClicked(MouseEvent mouseEvent,
                              TileGUIPiece tileGUIPiece) {
        //TODO: set on board (active tile) and word solver...
    }

    private void mouseDragged(MouseEvent mouseEvent,
                              TileGUIPiece tileGUIPiece) {
        //printTileGUI(tileGUIPiece.getTileImage());
        ImageView tileImageView = tileGUIPiece.getTileImage();
        Bounds tileBounds = tileImageView.localToScene(
                tileImageView.getBoundsInLocal());

//        tileGUIPiece.setX(tileBounds.getMinX() + mouseEvent.getX());
//        tileGUIPiece.setY(tileBounds.getMinY() + mouseEvent.getY());
        tileGUIPiece.setX(tileGUIPiece.getX() + mouseEvent.getX());
        tileGUIPiece.setY(tileGUIPiece.getY() + mouseEvent.getY());
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

        double tileX = tileBounds.getMinX();
        double tileY = tileBounds.getMinY();

        xCorrection = paneBounds.getMinX();
        yCorrection = paneBounds.getMinY();

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

        double xDisplacement =
                (closestGridXIndex - gridXIndex) * gridSquareSize;
        double yDisplacement =
                (closestGridYIndex - gridYIndex) * gridSquareSize;

//        tileGUIPiece.setX(gridSquareCenter + closestGridXIndex * gridSquareSize - tileX);
//        tileGUIPiece.setY(gridSquareCenter + closestGridYIndex * gridSquareSize - tileY);
        tileGUIPiece.setX(xDisplacement + tileGUIPiece.getX());
        tileGUIPiece.setY(yDisplacement + tileGUIPiece.getY());
        tileGUIPiece.draw();

        printTileImageView(tileGUIPiece.getTileImage());
        printTileGUI(tileGUIPiece);
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
}

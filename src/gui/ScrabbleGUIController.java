package gui;

import constants.MultiplierType;
import constants.TrueBoardSquareType;
import gamePieces.Board;
import gamePieces.TileBag;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ScrabbleGUIController {
    @FXML
    Pane pane;

    // The grid used is a square, so the weight is the same as the height
    private int gridSize = 900; // fixed to 900 x 900
    private int gridDimension = 15; // fixed to 15 x 15 board
    private int gridSquareSize = gridSize / gridDimension;

    @FXML
    public void initialize() {
        TileBag tileBag = new TileBag();
        Board board = new Board(tileBag);

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
    }
}

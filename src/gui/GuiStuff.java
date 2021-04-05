package gui;

import gamePieces.WordInPlay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import players.HumanPlayer;
import players.Player;

import java.util.Map;

public class GuiStuff {
    private HBox humanRackHBox;
    private Label humanWordLabel;
    private Label humanScoreLabel;
    private Label computerWordLabel;
    private Label computerScoreLabel;

    private CustomLabel humanWordCustomLabel;
    private CustomLabel humanScoreCustomLabel;
    private CustomLabel computerWordCustomLabel;
    private CustomLabel computerScoreCustomLabel;

    public GuiStuff(HBox humanRackHBox, Label humanWordLabel,
                    Label humanScoreLabel, Label computerWordLabel,
                    Label computerScoreLabel) {
        this.humanRackHBox = humanRackHBox;
        this.humanWordLabel = humanWordLabel;
        this.humanScoreLabel = humanScoreLabel;
        this.computerWordLabel = computerWordLabel;
        this.computerScoreLabel = computerScoreLabel;

        humanWordCustomLabel = new CustomLabel(humanWordLabel);
        humanScoreCustomLabel = new CustomLabel(humanScoreLabel);
        computerWordCustomLabel = new CustomLabel(computerWordLabel);
        computerScoreCustomLabel = new CustomLabel(computerScoreLabel);
    }

    /**
     * Assumign the current player is either the human player or the computer
     * player
     * @param currentPlayer
     */
    public void update(Player currentPlayer,
                       Map.Entry<WordInPlay, Integer> wordInPlayScore) {
        if (currentPlayer instanceof HumanPlayer) {
//            currentPlayer.refreshRack();
//            humanRackHBox.getChildren().addAll(
//                    currentPlayer.getNewTileInRackImageView());

            WordInPlay wordInPlay = wordInPlayScore.getKey();
            int score = wordInPlayScore.getValue();

            String currentWords = humanWordCustomLabel.getOutputMessage();
            currentWords += "\n" + wordInPlay.getWord() + " - " + score;
            humanWordCustomLabel.updateLabel(currentWords);
            String newScore =
                    Integer.toString(
                            humanScoreCustomLabel.getIntOutputMessage()
                                    + score);
            humanScoreCustomLabel.updateLabel(newScore);

            humanWordLabel.setText(humanWordCustomLabel.getText());
            humanScoreLabel.setText(humanScoreCustomLabel.getText());
        } else { // has to be the computer player...
//            currentPlayer.refreshRack();

            // FIXME: add computer tiles to board...
//            humanRackHBox.getChildren().addAll(
//                    currentPlayer.getNewTileInRackImageView());

            WordInPlay wordInPlay = wordInPlayScore.getKey();
            int score = wordInPlayScore.getValue();

            String currentWords = computerWordCustomLabel.getOutputMessage();
            currentWords += "\n" + wordInPlay.getWord() + " - " + score;
            computerWordCustomLabel.updateLabel(currentWords);
            String newScore =
                    Integer.toString(
                            computerScoreCustomLabel.getIntOutputMessage()
                                    + score);
            computerScoreCustomLabel.updateLabel(newScore);

            computerWordLabel.setText(computerWordCustomLabel.getText());
            computerScoreLabel.setText(computerScoreCustomLabel.getText());
        }
    }
}

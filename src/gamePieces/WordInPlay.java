package gamePieces;

import constants.PlayDirection;

public class WordInPlay {
    private PlayDirection playDirection;
    private String word;
    private int firstIndex;
    private int lastIndex;
    private int rowColumnIndex;

    public WordInPlay(PlayDirection playDirection, String word,
                      int firstIndex, int lastIndex, int rowColumnIndex) {
        this.playDirection = playDirection;
        this.word = word;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.rowColumnIndex = rowColumnIndex;
    }

    public PlayDirection getPlayDirection() {
        return playDirection;
    }

    public String getWord() {
        return word;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int getRowColumnIndex() {
        return rowColumnIndex;
    }
}

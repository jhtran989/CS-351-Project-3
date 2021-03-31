package constants;

public class TrueBoardSquareType {
    private int letterMultiplier;
    private int wordMultiplier;

    public static final TrueBoardSquareType LETTER =
            new TrueBoardSquareType(1, 1);

    public TrueBoardSquareType(int letterMultiplier, int wordMultiplier) {
        this.letterMultiplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
    }

    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }
}

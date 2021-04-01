package constants;

/**
 * Holds the revised board square type information with a constructor to
 * create custom multipliers (as evidenced by the third example test case
 * that went beyond the normal 15x15 size...)
 */
public class TrueBoardSquareType {
    private final int letterMultiplier;
    private final int wordMultiplier;

    // Even though both have the same multipliers, I use the reference
    // throughout the code (sort of like a "marker" to check)
    public static final TrueBoardSquareType LETTER =
            new TrueBoardSquareType(1, 1);
    public static final TrueBoardSquareType NO_MULTIPLIER =
            new TrueBoardSquareType(1, 1);

    public TrueBoardSquareType(int letterMultiplier, int wordMultiplier) {
        this.letterMultiplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
    }

    public TrueBoardSquareType(MultiplierType multiplierType, int multiplier) {
        if (multiplierType == MultiplierType.LETTER_MULTIPLIER) {
            letterMultiplier = multiplier;
            wordMultiplier = 1;
        } else {
            letterMultiplier = 1;
            wordMultiplier = multiplier;
        }
    }

    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }
}

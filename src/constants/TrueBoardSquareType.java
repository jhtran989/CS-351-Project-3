package constants;

/**
 * Holds the revised board square type information with a constructor to
 * create custom multipliers (as evidenced by the third example test case
 * that went beyond the normal 15x15 size...)
 */
public class TrueBoardSquareType {
    private final int letterMultiplier;
    private final int wordMultiplier;
    private MultiplierType multiplierType;

    // Even though both have the same multipliers, I use the reference
    // throughout the code (sort of like a "marker" to check)
    public static final TrueBoardSquareType LETTER =
            new TrueBoardSquareType(1, 1,
                    MultiplierType.NO_MULTIPLIER);
    public static final TrueBoardSquareType NO_MULTIPLIER =
            new TrueBoardSquareType(1, 1,
                    MultiplierType.NO_MULTIPLIER);

    public TrueBoardSquareType(int letterMultiplier, int wordMultiplier,
                               MultiplierType multiplierType) {
        this.letterMultiplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
        this.multiplierType = multiplierType;
    }

    public TrueBoardSquareType(MultiplierType multiplierType, int multiplier) {
        if (multiplierType == MultiplierType.LETTER_MULTIPLIER) {
            letterMultiplier = multiplier;
            wordMultiplier = 1;
            this.multiplierType = MultiplierType.LETTER_MULTIPLIER;
        } else {
            letterMultiplier = 1;
            wordMultiplier = multiplier;
            this.multiplierType = MultiplierType.WORD_MULTIPLIER;
        }
    }

    public MultiplierType getMultiplierType() {
        return multiplierType;
    }

    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }
}

package constants;

/**
 * Should be deprecated...since bigger boards are included in the input of
 * the word solver and creating a lot of different multipliers is not ideal...
 * ideal
 */
public enum BoardSquareType {
    LETTER(1, 1),
    LETTER_MULTIPLIER_2(2, 1),
    LETTER_MULTIPLIER_3(3, 1),
    WORD_MULTIPLIER_2(1, 2),
    WORD_MULTIPLIER_3(1, 3),
    NO_MULTIPLIER(1, 1);

    private int letterMultiplier;
    private int wordMultiplier;

    BoardSquareType(int letterMultiplier, int wordMultiplier) {
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

package constants;

public enum BoardSquareType {
    LETTER(-1),
    LETTER_MULTIPLIER_2(2),
    LETTER_MULTIPLIER_3(3),
    WORD_MULTIPLIER_2(2),
    WORD_MULTIPLIER_3(3),
    NO_MULTIPLIER(1);

    private int value;

    BoardSquareType(int value) {
        this.value = value;
    }
}

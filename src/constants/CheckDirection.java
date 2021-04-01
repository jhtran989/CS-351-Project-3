package constants;

/**
 * To differentiate from the PlayDirection class, this holds constants in the
 * four cardinal directions with corrections corresponding to the respective
 * direction
 */
public enum CheckDirection {
    UP(-1, 0),
    LEFT(0, -1),
    DOWN(1, 0),
    RIGHT(0, 1);

    private final int rowCorrection;
    private final int columnCorrection;

    CheckDirection(int rowCorrection, int columnCorrection) {
        this.rowCorrection = rowCorrection;
        this.columnCorrection = columnCorrection;
    }

    public int getRowCorrection() {
        return rowCorrection;
    }

    public int getColumnCorrection() {
        return columnCorrection;
    }
}

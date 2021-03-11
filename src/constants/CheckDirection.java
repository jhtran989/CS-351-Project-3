package constants;

public enum CheckDirection {
    UP(-1, 0),
    LEFT(0, -1),
    DOWN(1, 0),
    RIGHT(0, 1);

    private int rowCorrection;
    private int columnCorrection;

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

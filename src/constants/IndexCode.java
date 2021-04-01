package constants;

/**
 * Holds the necessary information for the index of something with the
 * context if the index is out of bounds of a given board (dimension)
 */
public class IndexCode {
    private final boolean inBounds;
    private int index;
    public static final IndexCode OUT_OF_BOUNDS = new IndexCode(false,
            -1);

    public IndexCode(boolean inBounds, int index) {
        this.inBounds = inBounds;
        this.index = index;
    }

    public IndexCode(int index) {
        inBounds = true;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

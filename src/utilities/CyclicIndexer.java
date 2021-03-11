package utilities;

public class CyclicIndexer {
    /**
     * Assuming the bounds is the length to correct (NOT MAX INDEX)
     * @param originalIndex
     * @param correction
     * @param bounds
     * @return
     */
    public static int findCyclicIndex(int originalIndex, int correction,
                                      int bounds) {
        int newIndex = originalIndex + correction;

        if (newIndex < 0) {
            return 0;
        } else if (newIndex >= bounds) {
            return bounds - 1;
        } else {
            return newIndex;
        }
    }
}

package utilities;

import constants.IndexCode;

/**
 * Contains static methods relating to finding indices with a given bounds
 * (and correction)
 */
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

    /**
     * Returns the absolute index with the applied correction to the
     * originalIndex
     * @param originalIndex
     * @param correction
     * @param bounds
     * @return the index with the correction if within bounds; else, return
     */
    public static IndexCode findAbsoluteIndex(int originalIndex, int correction,
                                              int bounds) {
        int newIndex = originalIndex + correction;

        if (newIndex < 0 || newIndex >= bounds) {
            return IndexCode.OUT_OF_BOUNDS;
        } else {
            return new IndexCode(newIndex);
        }
    }
}

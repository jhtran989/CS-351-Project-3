package gamePieces;

import constants.CheckDirection;
import constants.IndexCode;
import utilities.CyclicIndexer;

/**
 * An encapsulation of the position on the board that includes the row and
 * column index of some object
 */
public class RowColumn {
    private int rowIndex;
    private int columnIndex;
    private int dimension;

    public RowColumn(int rowIndex, int columnIndex, int dimension) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.dimension = dimension;
    }

    /**
     * Updates the indices in the given CheckDirection
     * @param checkDirection
     * @return
     */
    public boolean applyCheckDirection(CheckDirection checkDirection) {
        IndexCode rowIndexCode = CyclicIndexer.findAbsoluteIndex(
                rowIndex, checkDirection.getRowCorrection(),
                dimension);
        IndexCode columnIndexCode = CyclicIndexer.findAbsoluteIndex(
                columnIndex,
                checkDirection.getColumnCorrection(),
                dimension);

        if (rowIndexCode != IndexCode.OUT_OF_BOUNDS) {
            rowIndex += checkDirection.getRowCorrection();
        } else {
            return false;
        }

        if (columnIndexCode != IndexCode.OUT_OF_BOUNDS) {
            columnIndex += checkDirection.getColumnCorrection();
            return true;
        } else {
            return false;
        }
    }
}

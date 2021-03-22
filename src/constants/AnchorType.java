package constants;

public enum AnchorType {
    // FIXME: new revised types (in Board...)
//    PRIMARY_HEAD(true,
//            InsideOutsideAnchor.INSIDE_ANCHOR),
    PRIMARY_CENTER_HEAD(true,
            InsideOutsideAnchor.INSIDE_ANCHOR),
    PRIMATE_SIDE_HEAD(true,
            InsideOutsideAnchor.OUTSIDE_ANCHOR),
    PRIMARY_BODY(false,
            InsideOutsideAnchor.OUTSIDE_ANCHOR),
    SECONDARY_END(true,
            InsideOutsideAnchor.OUTSIDE_ANCHOR),
    SECONDARY_BODY(false,
            InsideOutsideAnchor.INSIDE_ANCHOR);

    private boolean pivotLeft;
    private InsideOutsideAnchor insideOutsideAnchor;

    AnchorType(boolean pivotLeft, InsideOutsideAnchor insideOutsideAnchor) {
        this.pivotLeft = pivotLeft;
        this.insideOutsideAnchor = insideOutsideAnchor;
    }

    public boolean isPivotLeft() {
        return pivotLeft;
    }

    public InsideOutsideAnchor getInsideOutsideAnchor() {
        return insideOutsideAnchor;
    }
}

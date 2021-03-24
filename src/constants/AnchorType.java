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
    SECONDARY_BODY(true,
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

    @Override
    public String toString() {
        String initialPart;

        switch (this) {
            case PRIMARY_CENTER_HEAD:
                initialPart = "Anchor type: PRIMARY CENTER HEAD";
                break;
            case PRIMATE_SIDE_HEAD:
                initialPart = "Anchor type: PRIMARY SIDE HEAD";
                break;
            case PRIMARY_BODY:
                initialPart = "Anchor type: PRIMARY BODY";
                break;
            case SECONDARY_END:
                initialPart = "Anchor type: SECONDARY END";
                break;
            case SECONDARY_BODY:
                initialPart = "Anchor type: SECONDARY BODY";
                break;
            default:
                initialPart = "EMPTY";
        }

        return initialPart + " pivot left: " + pivotLeft + " " +
                insideOutsideAnchor;
    }

    public static String nullToString() {
        return "Anchor type: EMPTY";
    }

    public static String customToString(AnchorType anchorType) {
        if (anchorType == null) {
            return nullToString();
        } else {
            return anchorType.toString();
        }
    }
}

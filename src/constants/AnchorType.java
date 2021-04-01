package constants;

/**
 * Enum for the anchor types (more in the README)
 */
public enum AnchorType {
    PRIMARY_CENTER_HEAD(true,
            InsideOutsideAnchor.INSIDE_ANCHOR),
    PRIMARY_SIDE_HEAD(true,
            InsideOutsideAnchor.OUTSIDE_ANCHOR),
    PRIMARY_SIDE_BODY(false,
            InsideOutsideAnchor.OUTSIDE_ANCHOR),
    SECONDARY_END(true,
            InsideOutsideAnchor.OUTSIDE_ANCHOR),
    SECONDARY_BODY(true,
            InsideOutsideAnchor.INSIDE_ANCHOR);

    private boolean leftExtend;
    private InsideOutsideAnchor insideOutsideAnchor;

    AnchorType(boolean leftExtend, InsideOutsideAnchor insideOutsideAnchor) {
        this.leftExtend = leftExtend;
        this.insideOutsideAnchor = insideOutsideAnchor;
    }

    public boolean isLeftExtend() {
        return leftExtend;
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
            case PRIMARY_SIDE_HEAD:
                initialPart = "Anchor type: PRIMARY SIDE HEAD";
                break;
            case PRIMARY_SIDE_BODY:
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

        return initialPart + " pivot left: " + leftExtend + " " +
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

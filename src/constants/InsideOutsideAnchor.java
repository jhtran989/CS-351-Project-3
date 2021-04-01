package constants;

/**
 * The first layer of distinction with the anchor types (see the README)
 */
public enum InsideOutsideAnchor {
    INSIDE_ANCHOR,
    OUTSIDE_ANCHOR;

    @Override
    public String toString() {
        String insideOutside = "Inside/Outside ";
        switch (this) {
            case INSIDE_ANCHOR:
                insideOutside += "INSIDE ANCHOR";
                break;
            case OUTSIDE_ANCHOR:
                insideOutside += "OUTSIDE ANCHOR";
                break;
            default:
                insideOutside += "EMPTY";
        }

        return insideOutside;
    }
}

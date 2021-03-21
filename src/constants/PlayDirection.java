package constants;

public enum PlayDirection {
    HORIZONTAL,
    VERTICAL;

    public static PlayDirection getOtherPlayDirection(
            PlayDirection playDirection) {
        if (playDirection == HORIZONTAL) {
            return VERTICAL;
        } else {
            return HORIZONTAL;
        }
    }

    public CheckDirection getCheckDirection() {
        if (this == HORIZONTAL) {
            return CheckDirection.RIGHT;
        } else {
            return CheckDirection.DOWN;
        }
    }

    public CheckDirection getReverseCheckDirection() {
        if (this == HORIZONTAL) {
            return CheckDirection.LEFT;
        } else {
            return CheckDirection.UP;
        }
    }
}

package constants;

/**
 * The enum that holds the play direction for a given word (vertical or
 * horizontal -- also has methods to get the direction of play and the
 * reverse direction of play as a CheckDirection constant)
 */
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

    @Override
    public String toString() {
        if (this == HORIZONTAL) {
            return "Play direction: HORIZONTAL";
        } else {
            return "Play direction: VERTICAL";
        }
    }
}

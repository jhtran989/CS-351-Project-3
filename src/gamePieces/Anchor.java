package gamePieces;

import constants.AnchorType;
import constants.InsideOutsideAnchor;
import constants.PlayDirection;

/**
 * The class that holds the major information about an anchor square (play
 * direction, anchor type, left limit, if applicable, etc.)
 */
public class Anchor {
    private final PlayDirection primaryDirection;
    private final PlayDirection secondaryDirection;
    private final AnchorType primaryAnchorType;
    private final AnchorType secondaryAnchorType;
    private int primaryLeftLimit;
    private int secondaryLeftLimit;
    private final InsideOutsideAnchor insideOutsideAnchor;

    // New addition (anchor overlap)
    private boolean initialCrossCheck;
    private boolean anchorOverlap;

    public Anchor(PlayDirection primaryDirection, AnchorType primaryAnchorType,
                  AnchorType secondaryAnchorType) {
        this.primaryDirection = primaryDirection;
        this.secondaryDirection = PlayDirection.getOtherPlayDirection(
                primaryDirection);
        this.primaryAnchorType = primaryAnchorType;
        this.secondaryAnchorType = secondaryAnchorType;

        if (primaryAnchorType == AnchorType.PRIMARY_SIDE_HEAD
                || primaryAnchorType == AnchorType.PRIMARY_SIDE_BODY
                || secondaryAnchorType == AnchorType.SECONDARY_END) {
            insideOutsideAnchor = InsideOutsideAnchor.OUTSIDE_ANCHOR;
        }  else {
            insideOutsideAnchor = InsideOutsideAnchor.INSIDE_ANCHOR;
        }

        primaryLeftLimit = 0; // default value...
        secondaryLeftLimit = 0; // default value...

        // probably a better way to do this...
        initialCrossCheck = true;
        anchorOverlap = false;
    }

    public PlayDirection getPrimaryDirection() {
        return primaryDirection;
    }

    public PlayDirection getSecondaryDirection() {
        return secondaryDirection;
    }

    public void setLeftLimit(int leftLimit,
                             PlayDirection playDirection) {
        if (playDirection == primaryDirection) {
            this.primaryLeftLimit = leftLimit;
        } else {
            this.secondaryLeftLimit = leftLimit;
        }
    }

    public AnchorType getPrimaryAnchorType() {
        return primaryAnchorType;
    }

    public AnchorType getSecondaryAnchorType() {
        return secondaryAnchorType;
    }

    public int getLeftLimit(PlayDirection playDirection) {
        if (playDirection == primaryDirection) {
            return primaryLeftLimit;
        } else {
            return secondaryLeftLimit;
        }
    }

    public InsideOutsideAnchor getInsideOutsideAnchor() {
        return insideOutsideAnchor;
    }

    public boolean isAnchorOverlap() {
        return anchorOverlap;
    }

    public boolean isInitialCrossCheck() {
        return initialCrossCheck;
    }

    public void turnOffInitialCrossCheck() {
        initialCrossCheck = false;
    }

    public void turnOnAnchorOverlap() {
        anchorOverlap = true;
    }
}

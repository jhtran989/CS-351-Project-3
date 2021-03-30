package gamePieces;

import constants.AnchorType;
import constants.InsideOutsideAnchor;
import constants.PlayDirection;

public class Anchor {
    private PlayDirection primaryDirection;
    private PlayDirection secondaryDirection;
    private AnchorType primaryAnchorType;
    private AnchorType secondaryAnchorType;
    private int primaryLeftLimit;
    private int secondaryLeftLimit;
    private InsideOutsideAnchor insideOutsideAnchor;
//    private boolean crossCheckHorizontal;
//    private boolean crossCheckVertical;

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

//        crossCheckHorizontal = false;
//        crossCheckVertical = false;

        primaryLeftLimit = 0; // default value...
        secondaryLeftLimit = 0; // default value...
    }

//    public void printCrossChecks() {
//        System.out.println("Cross checks - Horizontal: " +
//                crossCheckHorizontal + ", " + "Vertical: " +
//                crossCheckVertical);
//    }

//    public boolean getCrossCheck(PlayDirection playDirection) {
//        if (playDirection == PlayDirection.HORIZONTAL) {
//            return crossCheckHorizontal;
//        } else {
//            return crossCheckVertical;
//        }
//    }

//    public void initiateCrossChecks(PlayDirection playDirection) {
//        if (primaryAnchorType != null &&
//                primaryAnchorType.getInsideOutsideAnchor()
//                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
//            if (playDirection == PlayDirection.HORIZONTAL) {
//                crossCheckVertical = true;
//            } else {
//                crossCheckHorizontal = true;
//            }
//        }
//
//        if (secondaryAnchorType != null &&
//                secondaryAnchorType.getInsideOutsideAnchor()
//                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
//            if (playDirection == PlayDirection.HORIZONTAL) {
//                crossCheckHorizontal = true;
//            } else {
//                crossCheckVertical = true;
//            }
//        }
//    }

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
}

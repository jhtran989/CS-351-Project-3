package gamePieces;

import constants.AnchorType;
import constants.InsideOutsideAnchor;
import constants.PlayDirection;

public class Anchor {
    private PlayDirection primaryDirection;
    private PlayDirection secondaryDirection;
    private AnchorType primaryAnchorType;
    private AnchorType secondaryAnchorType;
    private int leftLimit;
//    private boolean crossCheckHorizontal;
//    private boolean crossCheckVertical;

    public Anchor(PlayDirection primaryDirection, AnchorType primaryAnchorType,
                  AnchorType secondaryAnchorType) {
        this.primaryDirection = primaryDirection;
        this.secondaryDirection = PlayDirection.getOtherPlayDirection(
                primaryDirection);
        this.primaryAnchorType = primaryAnchorType;
        this.secondaryAnchorType = secondaryAnchorType;

//        crossCheckHorizontal = false;
//        crossCheckVertical = false;

        leftLimit = 0; // default value...
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

    public void setLeftLimit(int leftLimit) {
        this.leftLimit = leftLimit;
    }

    public AnchorType getPrimaryAnchorType() {
        return primaryAnchorType;
    }

    public AnchorType getSecondaryAnchorType() {
        return secondaryAnchorType;
    }

    public int getLeftLimit() {
        return leftLimit;
    }
}

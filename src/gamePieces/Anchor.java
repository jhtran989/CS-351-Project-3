package gamePieces;

import constants.AnchorType;
import constants.PlayDirection;

public class Anchor {
    private PlayDirection primaryDirection;
    private PlayDirection secondaryDirection;
    private AnchorType primaryAnchorType;
    private AnchorType secondaryAnchorType;
    private int leftLimit;

    public Anchor(PlayDirection primaryDirection, AnchorType primaryAnchorType,
                  AnchorType secondaryAnchorType) {
        this.primaryDirection = primaryDirection;
        this.secondaryDirection = PlayDirection.getOtherPlayDirection(
                primaryDirection);
        this.primaryAnchorType = primaryAnchorType;
        this.secondaryAnchorType = secondaryAnchorType;
    }

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

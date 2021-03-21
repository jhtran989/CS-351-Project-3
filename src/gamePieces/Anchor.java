package gamePieces;

import constants.AnchorType;
import constants.PlayDirection;

public class Anchor {
    private PlayDirection primaryDirection;
    private PlayDirection secondaryDirection;
    private AnchorType primaryAnchorType;
    private AnchorType secondaryAnchorType;

    public Anchor(PlayDirection primaryDirection, AnchorType primaryAnchorType,
                  AnchorType secondaryAnchorType) {
        this.primaryDirection = primaryDirection;
        this.secondaryDirection = PlayDirection.getOtherPlayDirection(
                primaryDirection);
        this.primaryAnchorType = primaryAnchorType;
        this.secondaryAnchorType = secondaryAnchorType;
    }
}

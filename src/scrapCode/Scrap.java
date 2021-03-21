package scrapCode;

import constants.AnchorType;
import constants.CheckDirection;
import constants.PlayDirection;
import gamePieces.BoardSquare;
import gamePieces.WordInPlay;

import java.util.ArrayList;
import java.util.List;

public class Scrap {

//    /**
//     * The anchor squares found depending on the orientation of the word on
//     * the board -- thinking about it, the orientation might not matter, but
//     */
//    private void findAnchorBoardSquares() {
//        for (WordInPlay wordInPlay : wordInPlayList) {
//            firstLetterIndex = wordInPlay.getFirstIndex();
//            lastLetterIndex = wordInPlay.getLastIndex();
//            rowColumnIndex = wordInPlay.getRowColumnIndex();
//
//            BoardSquare firstBoardSquare;
//
//            List<BoardSquare> wordBoardSquareList = new ArrayList<>();
//            PlayDirection wordPlayDirection = wordInPlay.getPlayDirection();
//
//            for (int letterIndex = firstLetterIndex;
//                 letterIndex <= lastLetterIndex; letterIndex++) {
//                if (wordPlayDirection == PlayDirection.HORIZONTAL) {
//                    wordBoardSquareList.add(
//                            boardSquareArray[rowColumnIndex][letterIndex]);
//                } else {
//                    wordBoardSquareList.add(
//                            boardSquareArray[letterIndex][rowColumnIndex]);
//                }
//            }
//
//            firstBoardSquare = wordBoardSquareList.get(0);
//            addAnchorSquare(firstBoardSquare,
//                    wordPlayDirection,
//                    AnchorType.PRIMARY_HEAD,
//                    AnchorType.SECONDARY_BODY);
//
//
//
//            if (wordPlayDirection == PlayDirection.HORIZONTAL) {
//                if (!firstBoardSquare.isTopEdge()) {
//                    boolean first = true;
//                    for (BoardSquare boardSquare : wordBoardSquareList) {
//                        BoardSquare topBoardSquare =
//                                getBoardSquareInCheckDirection(
//                                        boardSquare,
//                                        CheckDirection.UP);
//
//                        if (topBoardSquare == null) {
//                            break;
//                        }
//
//                        if (first) {
//                            addAnchorSquare(topBoardSquare,
//                                    wordPlayDirection,
//                                    AnchorType.PRIMARY_HEAD,
//                                    null);
//
//                            addAnchorSquare(boardSquare,
//                                    wordPlayDirection,
//                                    AnchorType.PRIMARY_HEAD,
//                                    AnchorType.SECONDARY_BODY);
//                            first = false;
//                        } else {
//                            addAnchorSquare(topBoardSquare,
//                                    wordPlayDirection,
//                                    AnchorType.PRIMARY_BODY,
//                                    null);
//
//                            addAnchorSquare(boardSquare,
//                                    wordPlayDirection,
//                                    null,
//                                    AnchorType.SECONDARY_BODY);
//                        }
//                    }
//                }
//
//                if (!firstBoardSquare.isBottomEdge()) {
//                    boolean first = true;
//                    for (BoardSquare boardSquare : wordBoardSquareList) {
//                        BoardSquare bottomBoardSquare =
//                                getBoardSquareInCheckDirection(
//                                        boardSquare,
//                                        CheckDirection.DOWN);
//
//                        if (bottomBoardSquare == null) {
//                            break;
//                        }
//
//                        if (first) {
//                            addAnchorSquare(bottomBoardSquare,
//                                    wordPlayDirection,
//                                    AnchorType.PRIMARY_HEAD,
//                                    null);
//                            first = false;
//                        } else {
//                            addAnchorSquare(boardSquare,
//                                    wordPlayDirection,
//                                    AnchorType.PRIMARY_BODY,
//                                    null);
//                        }
//                    }
//                }
//            } else {
//                if (!firstBoardSquare.isLeftEdge()) {
//                    addAnchorSquare(boardSquareArray
//                                    [firstLetterIndex][rowColumnIndex - 1],
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_HEAD,
//                            AnchorType.SECONDARY_BODY);
//                }
//
//                if (!firstBoardSquare.isRightEdge()) {
//                    addAnchorSquare(boardSquareArray
//                                    [firstLetterIndex][rowColumnIndex + 1],
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_HEAD,
//                            null);
//                }
//            }
//
//
//        }
//    }

//    /**
//     * The anchor squares found depending on the orientation of the word on
//     * the board -- thinking about it, the orientation might not matter, but
//     */
//    private void findAnchorBoardSquares() {
//        for (WordInPlay wordInPlay : wordInPlayList) {
//            firstLetterIndex = wordInPlay.getFirstIndex();
//            lastLetterIndex = wordInPlay.getLastIndex();
//            rowColumnIndex = wordInPlay.getRowColumnIndex();
//
//            BoardSquare firstBoardSquare;
//            BoardSquare nextBoardSquare;
//
//            List<BoardSquare> wordBoardSquareList = new ArrayList<>();
//            PlayDirection wordPlayDirection = wordInPlay.getPlayDirection();
//
//            for (int letterIndex = firstLetterIndex;
//                 letterIndex <= lastLetterIndex; letterIndex++) {
//                if (wordPlayDirection == PlayDirection.HORIZONTAL) {
//                    wordBoardSquareList.add(
//                            boardSquareArray[rowColumnIndex][letterIndex]);
//                } else {
//                    wordBoardSquareList.add(
//                            boardSquareArray[letterIndex][rowColumnIndex]);
//                }
//            }
//
//            firstBoardSquare = wordBoardSquareList.get(0);
////            firstBoardSquare.setAnchor(new Anchor(
////                    wordPlayDirection,
////                    AnchorType.PRIMARY_HEAD,
////                    AnchorType.SECONDARY_BODY));
////            anchorBoardSquares.add(firstBoardSquare);
//            addAnchorSquare(firstBoardSquare,
//                    wordPlayDirection,
//                    AnchorType.PRIMARY_HEAD,
//                    AnchorType.SECONDARY_BODY);
//
//            if (wordPlayDirection == PlayDirection.HORIZONTAL) {
//                if (!firstBoardSquare.isTopEdge()) {
////                    nextBoardSquare =
////                            boardSquareArray
////                                    [rowColumnIndex - 1][firstLetterIndex];
////                    nextBoardSquare.setAnchor(new Anchor(
////                            wordPlayDirection,
////                            AnchorType.PRIMARY_HEAD,
////                            null));
//                    addAnchorSquare(boardSquareArray
//                                    [rowColumnIndex - 1][firstLetterIndex],
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_HEAD,
//                            AnchorType.SECONDARY_BODY);
//
//                    boolean first = true;
//                    for (BoardSquare boardSquare : wordBoardSquareList) {
//                        if (first) {
//                            addAnchorSquare(boardSquare,
//                                    wordPlayDirection,
//                                    AnchorType.PRIMARY_HEAD,
//                                    AnchorType.SECONDARY_BODY);
//                            first = false;
//                        } else {
//                            addAnchorSquare(boardSquare, wordPlayDirection, );
//                        }
//                    }
//
//                }
//
//                if (!firstBoardSquare.isBottomEdge()) {
////                    nextBoardSquare =
////                            boardSquareArray
////                                    [rowColumnIndex + 1][firstLetterIndex];
////                    nextBoardSquare.setAnchor(new Anchor(
////                            wordPlayDirection,
////                            AnchorType.PRIMARY_HEAD,
////                            null));
//                    addAnchorSquare(boardSquareArray
//                                    [rowColumnIndex + 1][firstLetterIndex],
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_HEAD,
//                            null);
//                }
//            } else {
//                if (!firstBoardSquare.isLeftEdge()) {
//                    addAnchorSquare(boardSquareArray
//                                    [firstLetterIndex][rowColumnIndex - 1],
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_HEAD,
//                            AnchorType.SECONDARY_BODY);
//                }
//
//                if (!firstBoardSquare.isRightEdge()) {
//                    addAnchorSquare(boardSquareArray
//                                    [firstLetterIndex][rowColumnIndex + 1],
//                            wordPlayDirection,
//                            AnchorType.PRIMARY_HEAD,
//                            null);
//                }
//            }
//
//
//        }
//    }
}

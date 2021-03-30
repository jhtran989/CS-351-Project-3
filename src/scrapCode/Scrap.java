package scrapCode;

import constants.AnchorType;
import constants.BoardSquareType;
import constants.CheckDirection;
import constants.PlayDirection;
import gamePieces.BoardSquare;
import gamePieces.MainGamePieces;
import gamePieces.WordInPlay;

import java.util.ArrayList;
import java.util.List;

public class Scrap {

    //    // TODO: fix so that the board squares will temporarily have active tiles
//    //  on them just to calculate the score and find the best move...
//    private void addLegalWord(WordInPlay completedWord) {
//        if (MainWordSolver.SCORE_LEGAL_WORD) {
//            System.out.println();
//            System.out.println("Completed word: " + completedWord);
//            completedWord.printWordBoardSquares();
//        }
//
//        int totalScore = completedWord.calculateScore();
//        int currentWordIndex = completedWord.getFirstIndex();
//        int startWordIndex = currentWordIndex;
//
//        for (BoardSquare wordBoardSquare : completedWord.getWordBoardSquares()) {
//            CrossCheckWord horizontalCrossCheckWord =
//                    wordBoardSquare.getHorizontalCrossCheckWord();
//            CrossCheckWord verticalCrossCheckWord =
//                    wordBoardSquare.getVerticalCrossCheckWord();
//
//            if (MainWordSolver.SCORE_LEGAL_WORD) {
//                System.out.println();
//                System.out.println("Current board square:");
//                wordBoardSquare.printFullBoardSquareInfo();
//                System.out.println("Current index");
//            }
//
//            if (horizontalCrossCheckWord != null) {
//                if (MainWordSolver.SCORE_LEGAL_WORD) {
//                    System.out.println("Horizontal word: " +
//                            horizontalCrossCheckWord);
//                    System.out.println("Horizontal index: " +
//                            horizontalCrossCheckWord.getFinalCharIndex());
//                }
//
//                horizontalCrossCheckWord.setLetterChoice(completedWord
//                        .getLetterAtIndex(currentWordIndex),
//                        startWordIndex);
//                totalScore += horizontalCrossCheckWord.calculateScore();
//                horizontalCrossCheckWord.resetLetterChoice(startWordIndex);
//            }
//
//            if (verticalCrossCheckWord != null) {
//                if (MainWordSolver.SCORE_LEGAL_WORD) {
//                    System.out.println("Vertical word: " + verticalCrossCheckWord);
//                    System.out.println("Vertical index: " +
//                            verticalCrossCheckWord.getFinalCharIndex());
//                }
//
//                verticalCrossCheckWord.setLetterChoice(completedWord
//                        .getLetterAtIndex(currentWordIndex),
//                        startWordIndex);
//                totalScore += verticalCrossCheckWord.calculateScore();
//                verticalCrossCheckWord.resetLetterChoice(startWordIndex);
//            }
//
//            currentWordIndex++;
//        }
//
//        if (MainWordSolver.SCORE_LEGAL_WORD) {
//            System.out.println("Legal word: " + completedWord + ", score: " +
//                    totalScore);
//        }
//
//        legalWordsMap.put(completedWord, totalScore);
//    }

    //            for (Map.Entry<Character, CharacterNode> childNodeEntry
//                    : currentNode.getChildrenMap().entrySet()) {
//                currentCharacter = childNodeEntry.getKey();
//                Tile currentTile = rack.searchLetter(
//                        currentCharacter);
//
//                if (currentTile != null) {
//                    // FIXME
////                    WordInPlay newLeftPart = new WordInPlay(
////                            currentLeftPart);
////                    newLeftPart.updateLeftPartWord(
////                            currentCharacter);
////                    partialWord.updateWord(currentCharacter);
//
//                    rack.removeTile(currentTile);
//
//                    updateBlankTile(currentTile);
//                    updateLeftPart(playDirection);
//
//                    leftPart(new WordInPlay(currentLeftPart),
//                            childNodeEntry.getValue(),
//                            leftLimit - 1, anchorSquare,
//                            playDirection);
//
//                    rack.addTile(currentTile);
//
//                    revertBlankTile(currentTile);
//                    revertLeftPart(currentTile,
//                            anchorSquare);
//                } else {
//                    //FIXME
//                    if (MainWordSolver.WORD_RECURSIVE) {
//                        System.out.println();
//                        System.out.println("Letter not in rack: " +
//                                currentCharacter);
//                    }
//                }
//            }

    //    /**
////     * Checks if the given board square was already added as an anchor square
////     * (overlapping anchor squares should only be counted once since the
////     * orientation could just be rotated to cover both play directions)
////     *
////     * @param boardSquare
////     * @param playDirection
////     * @return
////     */
////    private boolean checkNewAnchorSquare(BoardSquare boardSquare,
////                                         PlayDirection playDirection) {
////        BoardSquare previousBoardSquare = boardSquare;
////
////        for (int i = 0; i < 2; i++) {
////            previousBoardSquare =
////                    getBoardSquareInCheckDirection(
////                            previousBoardSquare,
////                            playDirection
////                                    .getReverseCheckDirection());
////            if (previousBoardSquare == null) {
////                return true;
////            } else if (previousBoardSquare.getBoardSquareType()
////                    == BoardSquareType.LETTER) {
////                return false;
////            }
////        }
////
////        return true;
////    }

    //    private void addAnchorSquare(BoardSquare boardSquare,
//                                 PlayDirection wordPlayDirection,
//                                 AnchorType primaryAnchorType,
//                                 AnchorType secondaryAnchorType) {
//        Anchor anchor = new Anchor(wordPlayDirection,
//                primaryAnchorType,
//                secondaryAnchorType);
//        boardSquare.setAnchor(anchor);
//        boardSquare.
//                initiateCrossChecks(
//                wordPlayDirection);
//
//        // FIXME
//        if (MainWordSolver.ANCHOR_DEBUG) {
//            boardSquare.printAnchorSquare();
//        }
//
//        if (primaryAnchorType != null) {
//            // FIXME: something is wrong here...
//            anchor.setLeftLimit(findLeftLimitAnchor(
//                    boardSquare,
//                    wordPlayDirection), wordPlayDirection);
//        }
//
//        anchorBoardSquaresList.add(boardSquare);
//    }

    //    /**
//     * Checks if there is a letter in the board two squares "behind" in in
//     * the word play direction (so when we're left extending, we don't
//     * over check with the word "behind" it)
//     *
//     * @param boardSquare
//     * @param playDirection
//     * @return
//     */
//    private boolean checkLeftExtend(BoardSquare boardSquare,
//                                    PlayDirection playDirection) {
//        BoardSquare previousBoardSquare = boardSquare;
//
//        for (int i = 0; i < 2; i++) {
//            previousBoardSquare =
//                    getBoardSquareInCheckDirection(
//                            previousBoardSquare,
//                            playDirection
//                                    .getReverseCheckDirection());
//            if (previousBoardSquare == null) {
//                return true;
//            } else if (previousBoardSquare.getBoardSquareType()
//                    == BoardSquareType.LETTER) {
//                return false;
//            }
//        }
//
//        return true;
//    }

    //    private int findLeftLimitAnchor(BoardSquare boardSquare,
//                                    PlayDirection playDirection) {
//        int leftLimit = 0;
//        BoardSquare previousBoardSquare = getBoardSquareInCheckDirection(
//                boardSquare,
//                playDirection.getReverseCheckDirection());
//
//        while (previousBoardSquare != null) {
//            if (previousBoardSquare.getBoardSquareType()
//                    == BoardSquareType.LETTER) {
//                return leftLimit - 1; // so the left part doesn't touch
//                // another word
//            }
//
//            // FIXME: fix 1
//            previousBoardSquare = getBoardSquareInCheckDirection(
//                    previousBoardSquare,
//                    playDirection.getReverseCheckDirection());
//            leftLimit++;
//        }
//
//        return leftLimit;
//    }

    //            for (Map.Entry<Character, CharacterNode> childNodeEntry
//                    : currentNode.getChildrenMap().entrySet()) {
//                currentCharacter = childNodeEntry.getKey();
//                Tile currentTile = rack.searchLetter(
//                        currentCharacter);
//
//                if (currentTile != null) {
//                    // FIXME
////                    WordInPlay newLeftPart = new WordInPlay(
////                            currentLeftPart);
////                    newLeftPart.updateLeftPartWord(
////                            currentCharacter);
////                    partialWord.updateWord(currentCharacter);
//
//                    rack.removeTile(currentTile);
//
//                    updateBlankTile(currentTile);
//                    updateLeftPart(playDirection);
//
//                    leftPart(new WordInPlay(currentLeftPart),
//                            childNodeEntry.getValue(),
//                            leftLimit - 1, anchorSquare,
//                            playDirection);
//
//                    rack.addTile(currentTile);
//
//                    revertBlankTile(currentTile);
//                    revertLeftPart(currentTile,
//                            anchorSquare);
//                } else {
//                    //FIXME
//                    if (MainWordSolver.WORD_RECURSIVE) {
//                        System.out.println();
//                        System.out.println("Letter not in rack: " +
//                                currentCharacter);
//                    }
//                }
//            }

    // FIXME
//            WordInPlay blankPrimaryWordInPlay = new WordInPlay(
//                    primaryPlayDirection, "",
//                    firstLetterIndex, lastLetterIndex,
//                    rowColumnLetterIndex,
//                    new ArrayList<>());

    //                // FIXME: had to create a new object so references doesn't
//                //  get mixed up...
//                if (currentAnchorType == AnchorType.PRIMARY_CENTER_HEAD) {
//                    for (WordInPlay wordInPlay : wordInPlayList) {
//                        if (!partialWord.equals(wordInPlay)
//                                && partialWord.getWordLength()
//                                > wordInPlay.getWordLength()) {
//                            addLegalWord(new WordInPlay(
//                                    partialWord));
//                        }
//                    }
//                } else {
//                    addLegalWord(new WordInPlay(
//                            partialWord));
//                }
//
//                if (MainWordSolver.FIND_WORD_DEBUG) {
//                    System.out.println("Legal word added: " + partialWord);
//                }

    //                    for (Character crossCheckCharacter : crossCheckSet) {
//                        String potentialWord =
//                                firstPart + crossCheckCharacter + secondPart;
//
//                        if (!wordSearchTrie.searchWord(potentialWord)) {
//                            if (MainWordSolver.DEBUG) {
//                                System.out.println("Invalid word: " +
//                                        potentialWord);
//                            }
//
////                            char invalidLetter =
////                                    anchorBoardSquare.getLetter();
//                            crossCheckSet.remove(crossCheckCharacter);
//
//                            if (MainWordSolver.DEBUG) {
//                                System.out.println("Removing the character " +
//                                        anchorBoardSquare.getLetter());
//                                System.out.println("Updated cross check set:");
//                                System.out.println(crossCheckSet);
//                            }
//                        } else {
//                            if (MainWordSolver.DEBUG) {
//                                System.out.println("Found word: " +
//                                        potentialWord);
//                            }
//                        }
//                    }

    //    public void initiateCrossChecks(PlayDirection playDirection) {
//        AnchorType primaryAnchorType = anchor.getPrimaryAnchorType();
//        AnchorType secondaryAnchorType = anchor.getSecondaryAnchorType();
//
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

    //    public boolean getCrossCheck(PlayDirection playDirection) {
//        if (playDirection == PlayDirection.HORIZONTAL) {
//            return crossCheckHorizontal;
//        } else {
//            return crossCheckVertical;
//        }
//    }

    //                if (first) {
//                    secondHeadBoardSquare =
//                            getBoardSquareInCheckDirection(
//                                    boardSquare,
//                                    oppositePlayCheckDirection);
//                } else {
//                    secondHeadBoardSquare =
//                            getBoardSquareInCheckDirection(
//                                    boardSquare,
//                                    wordPlayDirection
//                                            .getCheckDirection());
//                }

    //                if (first) {
//                    CheckDirection oppositePlayCheckDirection =
//                            PlayDirection.getOtherPlayDirection(
//                                    wordPlayDirection)
//                                    .getReverseCheckDirection();
//                    firstHeadBoardSquare =
//                            getBoardSquareInCheckDirection(
//                                    boardSquare,
//                                    oppositePlayCheckDirection);
//                } else {
//                    firstHeadBoardSquare =
//                            getBoardSquareInCheckDirection(
//                                    boardSquare,
//                                    wordPlayDirection
//                                            .getCheckDirection());
//                }

    /**
     //     * Returns the next BoardSquareType in the specified CheckDirection
     //     *
     //     * @param checkDirection direction to get the next Tile
     //     * @return BoardType of the next Tile in the specified CheckDirection;
     //     * returns null if the edge of the board is reached
     //     */
//    private BoardSquareType getBoardSquareTypeInCheckDirection(
//            CheckDirection checkDirection) {
//        int rowCorrection = checkDirection.getRowCorrection();
//        int columnCorrection = checkDirection.getColumnCorrection();
//
//        IndexCode rowIndexCode =
//                CyclicIndexer.findAbsoluteIndex(lastLetterIndex,
//                rowCorrection, dimension);
//        IndexCode columnIndexCode = CyclicIndexer.findAbsoluteIndex(
//                lastLetterIndex,
//                columnCorrection, dimension);
//
//        if (rowIndexCode == IndexCode.OUT_OF_BOUNDS
//                || columnIndexCode == IndexCode.OUT_OF_BOUNDS) {
//            return null;
//        } else {
//            int newRowIndex = rowIndexCode.getIndex();
//            int newColumnIndex = columnIndexCode.getIndex();
//
//            return boardSquareArray[newRowIndex][newColumnIndex].
//                    getBoardSquareType();
//        }
//    }

    //    private void applyPlayDirectionCorrection(
//            CheckDirection checkDirection) {
//        if (checkDirection == CheckDirection.RIGHT) {
//            lastLetterIndex += checkDirection.getRowCorrection();
//        } else if (checkDirection == CheckDirection.DOWN) {
//            lastLetterIndex += checkDirection.getColumnCorrection();
//        }
//    }

    //    private BoardSquareType getNextBoardSquareType(
//            CheckDirection checkDirection, BoardSquare boardSquare) {
//        int currentRowIndex = boardSquare.getRowIndex();
//        int currentColumnIndex = boardSquare.getColumnIndex();
//        int rowCorrection = checkDirection.getRowCorrection();
//        int columnCorrection = checkDirection.getColumnCorrection();
//
//        int newRowIndex = CyclicIndexer.findCyclicIndex(
//                currentRowIndex,
//                rowCorrection, dimension);
//        int newColumnIndex = CyclicIndexer.findCyclicIndex(
//                currentColumnIndex,
//                columnCorrection, dimension);
//
//        return boardSquareArray[newRowIndex][newColumnIndex].
//                getBoardSquareType();
//    }

//    private void applyPlayDirectionCorrection(
//            CheckDirection checkDirection, BoardSquare boardSquare) {
//        boardSquare.setRowIndex(
//                boardSquare.getRowIndex() + checkDirection.getRowCorrection());
//        boardSquare.setColumnIndex(
//                boardSquare.getColumnIndex() +
//                        checkDirection.getColumnCorrection());
//
//
//    }

//        for (BoardSquare anchorBoardSquare : anchorBoardSquares) {
//            Anchor currentAnchor = anchorBoardSquare.getAnchor();
//            if (currentAnchor.getPrimaryAnchorType() != null) {
//                currentAnchor.setLeftLimit(findLeftLimitAnchor(anchorBoardSquare, pla));
//            }
//        }

    //    private void addAnchorSquare(int rowIndex, int columnIndex,
//                                 PlayDirection wordPlayDirection,
//                                 AnchorType primaryAnchorType,
//                                 AnchorType secondaryAnchorType) {
//        BoardSquare boardSquare = boardSquareArray[rowIndex][columnIndex];
//        boardSquare.setAnchor(new Anchor(wordPlayDirection,
//                primaryAnchorType,
//                secondaryAnchorType));
//        anchorBoardSquares.add(boardSquare);
//    }

//    /**
//     * Assuming there's no one letter word
//     * Also, the letters are formed from left to right or top to bottom (only
//     * one direction to check for each axis -- horizontal or vertical,
//     * respectively)
//     * @param playDirection
//     * @param boardSquare
//     */
//    private void checkWordAlongDirection(PlayDirection playDirection,
//                                         BoardSquare boardSquare) {
//        String temporaryWord = "" + boardSquare.getLetter();
//        BoardSquare nextBoardSquare;
//
//        if (playDirection == PlayDirection.HORIZONTAL) {
//            firstLetterIndex = boardSquare.getColumnIndex();
//            lastLetterIndex = firstLetterIndex;
//            rowColumnIndex = boardSquare.getRowIndex();
//
//            boardSquare.setHorizontalCheck(false);
//
////            int nextLetterIndex = CyclicIndexer.findCyclicIndex(
////                    lastLetterIndex,
////                    CheckDirection.RIGHT.getRowCorrection(),
////                    dimension - 1);
//
//            while (getBoardSquareTypeInCheckDirection(CheckDirection.RIGHT)
//                    == BoardSquareType.LETTER) {
//                nextBoardSquare =
//                        boardSquareArray[rowColumnIndex][lastLetterIndex];
//                nextBoardSquare.setHorizontalCheck(false);
//
//                applyPlayDirectionCorrection(CheckDirection.RIGHT);
//                temporaryWord += nextBoardSquare.getLetter();
//
////                if (nextLetterIndex == dimension - 1) {
////                    break;
////                }
//            }
//        } else {
//            firstLetterIndex = boardSquare.getRowIndex();
//            lastLetterIndex = firstLetterIndex;
//            rowColumnIndex = boardSquare.getColumnIndex();
//
//            boardSquare.setVerticalCheck(false);
//
////            int nextLetterIndex = CyclicIndexer.findCyclicIndex(
////                    lastLetterIndex,
////                    CheckDirection.DOWN.getRowCorrection(),
////                    dimension + 1);
//
//            while (getBoardSquareTypeInCheckDirection(CheckDirection.DOWN)
//                    == BoardSquareType.LETTER) {
//                nextBoardSquare =
//                        boardSquareArray[lastLetterIndex][rowColumnIndex];
//                nextBoardSquare.setVerticalCheck(false);
//
//                applyPlayDirectionCorrection(CheckDirection.DOWN);
//                temporaryWord += nextBoardSquare.getLetter();
//
////                if (nextLetterIndex == dimension - 1) {
////                    break;
////                }
//            }
//        }
//
//        if (temporaryWord.length() > 1) {
//            WordInPlay wordInPlay = new WordInPlay(playDirection,
//                    temporaryWord, firstLetterIndex,
//                    lastLetterIndex, rowColumnIndex);
//            wordInPlayList.add(wordInPlay);
//
//            if (MainGamePieces.DEBUG) {
//                System.out.println(wordInPlay);
//            }
//        }


//        if (playDirection == PlayDirection.HORIZONTAL) {
//            int currentLeftIndex;
//            int currentRightIndex;
//            int rowIndex = boardSquare.getRowIndex();
//
//            currentLeftIndex = boardSquare.getColumnIndex();
//            currentRightIndex = boardSquare.getColumnIndex();
//
//            int nextLeftIndex = currentLeftIndex - 1;
//            int nextRightIndex = currentRightIndex + 1;
//
//            while (boardSquareArray[rowIndex][nextLeftIndex]
//                    .getBoardSquareType() == BoardSquareType.LETTER) {
//                if (nextLeftIndex == 0) {
//                    applyPlayDirectionCorrection(CheckDirection.LEFT,
//                            boardSquare);
//                    break;
//                }
//
//                applyPlayDirectionCorrection(CheckDirection.LEFT,
//                        boardSquare);
//                nextLeftIndex--;
//            }
//
//            while (boardSquareArray[rowIndex][nextRightIndex]
//                    .getBoardSquareType() == BoardSquareType.LETTER) {
//                if (nextRightIndex == dimension - 1) {
//                    break;
//                }
//
//                applyPlayDirectionCorrection(CheckDirection.RIGHT,
//                        boardSquare);
//                nextRightIndex++;
//            }
//
//
//
//
//            return new WordInPlay(playDirection, )
//        } else {
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

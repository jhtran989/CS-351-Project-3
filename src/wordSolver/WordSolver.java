package wordSolver;

import comparators.LegalWordComparator;
import constants.AnchorType;
import constants.BoardSquareType;
import constants.CheckDirection;
import constants.PlayDirection;
import gamePieces.*;
import wordSearch.CharacterNode;
import wordSearch.WordSearchTrie;

import java.util.*;

public class WordSolver {
    private Board board;
    private WordSearchTrie wordSearchTrie;

    private List<WordInPlay> wordInPlayList;
    private int firstLetterIndex;
    private int lastLetterIndex;
    private int rowColumnLetterIndex;
    private List<BoardSquare> anchorBoardSquaresList;
    private List<BoardSquare> crossCheckBoardSquareList;

    private Rack rack;
    private HashMap<WordInPlay, Integer> legalWordsMap;

    public WordSolver(Board board, WordSearchTrie wordSearchTrie, Rack rack) {
        this.board = board;
        this.wordSearchTrie = wordSearchTrie;
        this.rack = rack;

        wordInPlayList = new ArrayList<>();
        anchorBoardSquaresList = new ArrayList<>();
        legalWordsMap = new HashMap<>();

        crossCheckBoardSquareList = new ArrayList<>();
    }

    public void generateInitialHighestScoringMove() {
        findInitialPossibleWords();
        findHighestScoringMove();
    }

    public void generateHighestScoringMove() {

    }

    private void findHighestScoringMove() {
        List<Map.Entry<WordInPlay, Integer>> legalWordsList =
                new ArrayList<>(legalWordsMap.entrySet());

        Collections.sort(legalWordsList, new LegalWordComparator());

        Map.Entry<WordInPlay, Integer> bestMove =
                legalWordsList.get(legalWordsList.size() - 1);

        System.out.println("Highest scoring move: " +
                bestMove.getKey());
        System.out.println("Score: " + bestMove.getValue());
    }

    private void findPossibleWords() {

    }

    private void findInitialPossibleWords() {
        performInitialPreliminarySearch();
        CharacterNode wordSearchTrieRoot = wordSearchTrie.getRoot();

        for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
            Anchor currentAnchor = anchorBoardSquare.getAnchor();
            AnchorType primaryAnchorType =
                    currentAnchor.getPrimaryAnchorType();
            AnchorType secondaryAnchorType =
                    currentAnchor.getSecondaryAnchorType();

            int leftLimit;


            PlayDirection primaryPlayDirection =
                    currentAnchor.getPrimaryDirection();
            setLetterIndices(primaryPlayDirection,
                    anchorBoardSquare);

            WordInPlay blankPrimaryWordInPlay = new WordInPlay(
                    primaryPlayDirection, "",
                    firstLetterIndex, lastLetterIndex,
                    rowColumnLetterIndex,
                    new ArrayList<>());

            switch (primaryAnchorType) {
                case PRIMARY_CENTER_HEAD:
                case PRIMATE_SIDE_HEAD:
                    leftLimit = currentAnchor.getLeftLimit();
                    leftPart(blankPrimaryWordInPlay,
                            wordSearchTrieRoot,
                            leftLimit,
                            anchorBoardSquare,
                            primaryPlayDirection);
                    break;
                case PRIMARY_BODY:
                    extendRight(blankPrimaryWordInPlay,
                            wordSearchTrieRoot,
                            anchorBoardSquare,
                            primaryPlayDirection);
                    break;
                default:
                    //throw new InternalError("Invalid Primary Anchor type");
            }

            PlayDirection secondaryPlayDirection =
                    currentAnchor.getSecondaryDirection();
            setLetterIndices(secondaryPlayDirection,
                    anchorBoardSquare);

            WordInPlay blankSecondaryWordInPlay = new WordInPlay(
                    secondaryPlayDirection, "",
                    firstLetterIndex, lastLetterIndex,
                    rowColumnLetterIndex,
                    new ArrayList<>());

            switch (secondaryAnchorType) {
                case SECONDARY_END:
                case SECONDARY_BODY:
                    leftLimit = currentAnchor.getLeftLimit();
                    leftPart(blankSecondaryWordInPlay,
                            wordSearchTrieRoot,
                            leftLimit, anchorBoardSquare,
                            secondaryPlayDirection);
                    break;
                default:
                    //throw new InternalError("Invalid Secondary Anchor type");
            }
        }
    }

    private void leftPart(WordInPlay partialWord, CharacterNode currentNode,
                          int leftLimit, BoardSquare anchorSquare,
                          PlayDirection playDirection) {
        extendRight(partialWord, currentNode,
                anchorSquare, playDirection);

        if (leftLimit > 0) {
            for (Map.Entry<Character, CharacterNode> childNodeEntry
                    : currentNode.getChildrenMap().entrySet()) {
                char currentCharacter = childNodeEntry.getKey();
                Tile currentTile = rack.searchCharacter(
                        currentCharacter);

                if (currentTile != null) {
                    partialWord.updateWord(currentCharacter);
                    rack.removeTile(currentTile);

                    leftPart(partialWord,
                            childNodeEntry.getValue(),
                            leftLimit - 1, anchorSquare,
                            playDirection);

                    rack.addTile(currentTile);
                }
            }
        }
    }

    private void extendRight(WordInPlay partialWord,
                             CharacterNode currentNode,
                             BoardSquare currentBoardSquare,
                             PlayDirection playDirection) {
        if (currentBoardSquare != null
                && currentBoardSquare.getBoardSquareType()
                != BoardSquareType.LETTER) {
            if (currentNode.isTerminalNode()) {
                addLegalWord(partialWord);
            }

            for (Map.Entry<Character, CharacterNode> childNodeEntry
                    : currentNode.getChildrenMap().entrySet()) {
                char currentCharacter = childNodeEntry.getKey();
                Tile currentTile = rack.searchCharacter(
                        currentCharacter);

                if (currentTile != null) {
                    List<Character> crossCheckList =
                            currentBoardSquare.getCrossCheckList();
                    if (crossCheckList.contains(currentCharacter)) {
                        rack.removeTile(currentTile);

                        BoardSquare nextBoardSquare =
                                getBoardSquareInCheckDirection(
                                        currentBoardSquare,
                                        playDirection
                                                .getCheckDirection());
                        extendRight(partialWord,
                                childNodeEntry.getValue(),
                                nextBoardSquare,
                                playDirection);

                        rack.addTile(currentTile);
                    }
                }
            }
        }
    }

    private void addLegalWord(WordInPlay completedWord) {
        int totalScore = completedWord.calculateScore();

        for (BoardSquare wordBoardSquare : completedWord.getWordBoardSquares()) {
            CrossCheckWord horizontalCrossCheckWord =
                    wordBoardSquare.getHorizontalCrossCheckWord();
            CrossCheckWord verticalCrossCheckWord =
                    wordBoardSquare.getVerticalCrossCheckWord();

            if (horizontalCrossCheckWord != null) {
                totalScore += horizontalCrossCheckWord.calculateScore();
            }

            if (verticalCrossCheckWord != null) {
                totalScore += verticalCrossCheckWord.calculateScore();
            }

            legalWordsMap.put(completedWord, totalScore);
        }
    }

    private void resetTurn() {

    }

    private void performInitialPreliminarySearch() {
        findInitialAnchorBoardSquares();
        performInitialCrossChecks();
    }

    // TODO: add the search...
    private void performInitialCrossChecks() {
        for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
            for (PlayDirection playDirection : PlayDirection.values()) {
                if (anchorBoardSquare.getCrossCheck(playDirection)) {
                    crossCheckBoardSquareList = new ArrayList<>();
                    List<Character> crossCheckList =
                            anchorBoardSquare.getCrossCheckList();

                    String firstPart = firstPartCrossCheck(
                            anchorBoardSquare,
                            playDirection);
                    String secondPart = secondPartCrossCheck(
                            anchorBoardSquare,
                            playDirection);

                    setLetterIndices(playDirection,
                            anchorBoardSquare);
                    CrossCheckWord crossCheckWord =
                            new CrossCheckWord(playDirection,
                                    firstPart + secondPart,
                                    firstLetterIndex,
                                    lastLetterIndex,
                                    rowColumnLetterIndex,
                                    crossCheckBoardSquareList);
                    anchorBoardSquare.addCrossCheckWord(playDirection,
                            crossCheckWord);

                    for (Character crossCheckCharacter : crossCheckList) {
                        String potentialWord =
                                firstPart + crossCheckCharacter + secondPart;

                        if (!wordSearchTrie.searchWord(potentialWord)) {
                            Character invalidLetter =
                                    anchorBoardSquare.getLetter();
                            crossCheckList.remove(invalidLetter);
                        }
                    }
                }
            }
        }
    }

    private void performWordCrossCheck() {

    }

    private String firstPartCrossCheck(BoardSquare anchorBoardSquare,
                                       PlayDirection playDirection) {
        String firstPart = "";
        List<BoardSquare> dummyBoardSquare = new ArrayList<>();

        dummyBoardSquare.add(getBoardSquareInCheckDirection(
                anchorBoardSquare,
                playDirection.getReverseCheckDirection()));
        BoardSquare previousBoardSquare = dummyBoardSquare.get(0);

        while (previousBoardSquare != null
                && previousBoardSquare.getBoardSquareType()
                == BoardSquareType.LETTER) {
            crossCheckBoardSquareList.add(0, previousBoardSquare);
            firstPart = previousBoardSquare.getLetter() + firstPart;

            dummyBoardSquare.clear();
            dummyBoardSquare.add(getBoardSquareInCheckDirection(
                    anchorBoardSquare,
                    playDirection.getReverseCheckDirection()));
            previousBoardSquare = dummyBoardSquare.get(0);
        }

        return firstPart;
    }

    private String secondPartCrossCheck(BoardSquare anchorBoardSquare,
                                        PlayDirection playDirection) {
        String secondPart = "";
        List<BoardSquare> dummyBoardSquare = new ArrayList<>();

        dummyBoardSquare.add(getBoardSquareInCheckDirection(
                anchorBoardSquare,
                playDirection.getReverseCheckDirection()));
        BoardSquare nextBoardSquare = dummyBoardSquare.get(0);

        while (nextBoardSquare != null
                && nextBoardSquare.getBoardSquareType()
                == BoardSquareType.LETTER) {
            crossCheckBoardSquareList.add(dummyBoardSquare.get(0));
            secondPart += nextBoardSquare.getLetter();

            dummyBoardSquare.clear();
            dummyBoardSquare.add(getBoardSquareInCheckDirection(
                    anchorBoardSquare,
                    playDirection.getReverseCheckDirection()));
            nextBoardSquare = dummyBoardSquare.get(0);
        }

        return secondPart;
    }

    private void findAnchorBoardSquares() {

    }

    /**
     * The anchor squares found depending on the orientation of the word on
     * the board -- thinking about it, the orientation might not matter, but
     */
    private void findInitialAnchorBoardSquares() {
        for (WordInPlay wordInPlay : wordInPlayList) {
            firstLetterIndex = wordInPlay.getFirstIndex();
            lastLetterIndex = wordInPlay.getLastIndex();
            rowColumnLetterIndex = wordInPlay.getRowColumnIndex();

            List<BoardSquare> wordBoardSquareList = new ArrayList<>();
            PlayDirection wordPlayDirection = wordInPlay.getPlayDirection();

            // adds all the board squares that make up the word into the list
            for (int letterIndex = firstLetterIndex;
                 letterIndex <= lastLetterIndex; letterIndex++) {
                if (wordPlayDirection == PlayDirection.HORIZONTAL) {
                    wordBoardSquareList.add(
                            board.getBoardSquare(rowColumnLetterIndex,
                                    letterIndex));
                } else {
                    wordBoardSquareList.add(
                            board.getBoardSquare(letterIndex,
                                    rowColumnLetterIndex));
                }
            }

            // add the inside anchor squares (only the first board square is
            // in the primary direction, and the rest are used to check in
            // the opposite play direction -- i.e. if the word is played
            // horizontally, then the body anchors are checked vertically)
            boolean first = true;
            for (BoardSquare boardSquare : wordBoardSquareList) {
                if (first) {
                    addAnchorSquare(boardSquare,
                            wordPlayDirection,
                            AnchorType.PRIMARY_CENTER_HEAD,
                            AnchorType.SECONDARY_BODY);

                } else {
                    addAnchorSquare(boardSquare,
                            wordPlayDirection,
                            null,
                            AnchorType.SECONDARY_BODY);
                }

                first = false;
            }

            // add the side body anchor squares (in the "buffer" region that
            // doesn't contain a letter yet -- adjacent to the word and
            // played along the same play direction)
            first = true;
            for (BoardSquare boardSquare : wordBoardSquareList) {
                BoardSquare firstHeadBoardSquare =
                        getBoardSquareInCheckDirection(
                                boardSquare,
                                wordPlayDirection
                                        .getCheckDirection());

                if (firstHeadBoardSquare == null) {
                    break;
                }

                if (checkPrimaryBodyAnchor(
                        firstHeadBoardSquare,
                        wordPlayDirection)) {
                    if (first) {
                        addAnchorSquare(firstHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMATE_SIDE_HEAD,
                                null);
                    } else {
                        addAnchorSquare(firstHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_BODY,
                                null);
                    }
                }

                first = false;
            }

            first = true;
            for (BoardSquare boardSquare : wordBoardSquareList) {
                BoardSquare secondHeadBoardSquare =
                        getBoardSquareInCheckDirection(
                                boardSquare,
                                wordPlayDirection
                                        .getReverseCheckDirection());

                if (secondHeadBoardSquare == null) {
                    break;
                }

                if (checkPrimaryBodyAnchor(
                        secondHeadBoardSquare,
                        wordPlayDirection)) {
                    if (first) {
                        addAnchorSquare(secondHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMATE_SIDE_HEAD,
                                null);
                    } else {
                        addAnchorSquare(boardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_BODY,
                                null);
                    }
                }

                first = false;
            }

            BoardSquare firstBoardSquare = wordBoardSquareList.get(0);
            BoardSquare lastBoardSquare =
                    wordBoardSquareList.get(wordBoardSquareList.size() - 1);

            if (checkSecondaryEndAnchor(firstBoardSquare,
                    wordPlayDirection.getReverseCheckDirection())) {
                BoardSquare frontEnd =
                        getBoardSquareInCheckDirection(
                                firstBoardSquare,
                                wordPlayDirection
                                        .getReverseCheckDirection());

                assert frontEnd != null;
                addAnchorSquare(frontEnd,
                        wordPlayDirection, null,
                        AnchorType.SECONDARY_END);
            }

            if (checkSecondaryEndAnchor(lastBoardSquare,
                    wordPlayDirection.getCheckDirection())) {
                BoardSquare backEnd =
                        getBoardSquareInCheckDirection(
                                lastBoardSquare,
                                wordPlayDirection
                                        .getCheckDirection());

                assert backEnd != null;
                addAnchorSquare(backEnd,
                        wordPlayDirection, null,
                        AnchorType.SECONDARY_END);
            }
        }
    }

    private int findLeftLimitAnchor(BoardSquare boardSquare,
                                    PlayDirection playDirection) {
        int leftLimit = 0;
        BoardSquare previousBoardSquare = getBoardSquareInCheckDirection(
                boardSquare,
                playDirection.getReverseCheckDirection());

        while (previousBoardSquare != null) {
            if (previousBoardSquare.getBoardSquareType()
                    == BoardSquareType.LETTER) {
                return leftLimit - 1; // so the left part doesn't touch
                // another word
            }

            previousBoardSquare = getBoardSquareInCheckDirection(
                    boardSquare,
                    playDirection.getReverseCheckDirection());
            leftLimit++;
        }

        return leftLimit;
    }

    /**
     * Checks if there is a letter in the board two squares "behind" in in
     * the word play direction (so when we're left extending, we don't
     * over check with the word "behind" it)
     * @param boardSquare
     * @param playDirection
     * @return
     */
    private boolean checkPrimaryBodyAnchor(BoardSquare boardSquare,
                                           PlayDirection playDirection) {
        BoardSquare previousBoardSquare = boardSquare;
        for (int i = 0; i < 2; i++) {
            previousBoardSquare =
                    getBoardSquareInCheckDirection(
                            previousBoardSquare,
                            playDirection
                                    .getReverseCheckDirection());
            if (previousBoardSquare == null) {
                return true;
            } else if (previousBoardSquare.getBoardSquareType()
                    == BoardSquareType.LETTER) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the ends in the opposite play direction of the word are letters
     * Note: the ends of the word cannot contain a letter (else, something
     * might be wrong with the word search algorithm...)
     *
     * @param boardSquare
     * @param checkDirection
     * @return
     */
    private boolean checkSecondaryEndAnchor(BoardSquare boardSquare,
                                            CheckDirection checkDirection) {
        return !boardSquare.checkEdge(checkDirection);
    }

    /**
     * Gets the board square in the said direction relative to a given
     * board square
     *
     * @param boardSquare
     * @param checkDirection
     * @return
     */
    private BoardSquare getBoardSquareInCheckDirection(BoardSquare boardSquare,
                                                       CheckDirection
                                                               checkDirection) {
        if (checkDirection == CheckDirection.UP) {
            if (!boardSquare.isTopEdge()) {
                return board.getBoardSquare(boardSquare.getRowIndex() - 1,
                        boardSquare.getColumnIndex());
            }
        } else if (checkDirection == CheckDirection.DOWN) {
            if (!boardSquare.isBottomEdge()) {
                return board.getBoardSquare(boardSquare.getRowIndex() + 1,
                        boardSquare.getColumnIndex());
            }
        } else if (checkDirection == CheckDirection.LEFT) {
            if (!boardSquare.isLeftEdge()) {
                return board.getBoardSquare(boardSquare.getRowIndex(),
                        boardSquare.getColumnIndex() - 1);
            }
        } else { // Right
            if (!boardSquare.isRightEdge()) {
                return board.getBoardSquare(boardSquare.getRowIndex(),
                        boardSquare.getColumnIndex() + 1);
            }
        }

        return null;
    }

    private void addAnchorSquare(BoardSquare boardSquare,
                                 PlayDirection wordPlayDirection,
                                 AnchorType primaryAnchorType,
                                 AnchorType secondaryAnchorType) {
        Anchor anchor = new Anchor(wordPlayDirection,
                primaryAnchorType,
                secondaryAnchorType);
        boardSquare.setAnchor(anchor);
        boardSquare.initiateCrossChecks(wordPlayDirection);

        if (primaryAnchorType != null) {
            anchor.setLeftLimit(findLeftLimitAnchor(boardSquare,
                    wordPlayDirection));
        }

        anchorBoardSquaresList.add(boardSquare);
    }

    public void findWordsInPlay() {
        int dimension = board.getDimension();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BoardSquare currentBoardSquare = board.getBoardSquare(i,
                        j);

                if (currentBoardSquare.getBoardSquareType()
                        == BoardSquareType.LETTER) {
                    if (currentBoardSquare.isWordHorizontalCheck()) {
                        checkWordAlongDirection(
                                PlayDirection.HORIZONTAL,
                                currentBoardSquare);
                    }

                    if (currentBoardSquare.isWordVerticalCheck()) {
                        checkWordAlongDirection(
                                PlayDirection.VERTICAL,
                                currentBoardSquare);
                    }
                }
            }
        }
    }


    // FIXME: realized getting the next board square would probably be the
    //  easiest method, but retained old design (wouldn't want to waste work,
    //  right...)
    /**
     * Assuming there's no one letter word
     * Also, the letters are formed from left to right or top to bottom (only
     * one direction to check for each axis -- horizontal or vertical,
     * respectively)
     * @param playDirection
     * @param boardSquare
     */
    private void checkWordAlongDirection(PlayDirection playDirection,
                                         BoardSquare boardSquare) {
        String temporaryWord = "" + boardSquare.getLetter();
        BoardSquare nextBoardSquare;
        List<BoardSquare> dummyBoardSquare = new ArrayList<>();

        setLetterIndices(playDirection, boardSquare);

        List<BoardSquare> wordBoardSquares = new ArrayList<>();
        wordBoardSquares.add(boardSquare);

//        RowColumn rowColumn = new RowColumn(
//                boardSquare.getRowIndex(),
//                boardSquare.getColumnIndex(), dimension);

        boardSquare.checkPlayDirection(playDirection);

        dummyBoardSquare.add(getBoardSquareInCheckDirection(
                boardSquare,
                playDirection.getCheckDirection()));
        nextBoardSquare = dummyBoardSquare.get(0);

        while (nextBoardSquare != null && nextBoardSquare
                .getBoardSquareType()
                == BoardSquareType.LETTER) {
            nextBoardSquare.checkPlayDirection(playDirection);
            wordBoardSquares.add(dummyBoardSquare.get(0));

            temporaryWord += nextBoardSquare.getLetter();
//            rowColumn.applyCheckDirection(
//                    playDirection.getCheckDirection());
            lastLetterIndex++;

            dummyBoardSquare.clear();
            dummyBoardSquare.add(getBoardSquareInCheckDirection(
                    nextBoardSquare,
                    playDirection.getCheckDirection()));
            nextBoardSquare = dummyBoardSquare.get(0);
        }

        if (temporaryWord.length() > 1) {
            WordInPlay wordInPlay = new WordInPlay(playDirection,
                    temporaryWord, firstLetterIndex,
                    lastLetterIndex, rowColumnLetterIndex,
                    wordBoardSquares);
            wordInPlayList.add(wordInPlay);

            if (MainGamePieces.DEBUG) {
                System.out.println(wordInPlay);
            }
        }
    }

    private void setLetterIndices(PlayDirection playDirection,
                                  BoardSquare boardSquare) {
        if (playDirection == PlayDirection.HORIZONTAL) {
            firstLetterIndex = boardSquare.getColumnIndex();
            lastLetterIndex = boardSquare.getColumnIndex();
            rowColumnLetterIndex = boardSquare.getRowIndex();
        } else {
            firstLetterIndex = boardSquare.getRowIndex();
            lastLetterIndex = boardSquare.getRowIndex();
            rowColumnLetterIndex = boardSquare.getColumnIndex();
        }
    }
}

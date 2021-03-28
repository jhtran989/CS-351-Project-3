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
    private List<BoardSquare> crossCheckBoardSquareWordList;

    private Rack rack;
    private HashMap<WordInPlay, Integer> legalWordsMap;

    // there were problems with object references and recursion in the
    // leftPart() and extendRight() methods, so a new constructor for WordInPlay
    // had to be created and a copy of the left part during each call of
    // leftPart() in the stack had to be created using the constructor
    // mentioned above
    private WordInPlay currentLeftPart;

    // since I decided to split up the kinds of anchors, I needed a way to
    // access the current anchor type (i.e. in the extendRight(), I had to make
    // sure that current words in play (or even parts of
    // those words) aren't added to the legal words)
    private AnchorType currentAnchorType;

    // I decided to include a List of BoardSquare objects when finding the
    // possible words since that was the only way (with the way I designed it)
    // for me to find any additional words that were formed (i.e. the two or
    // so letter words formed when the possible word is placed adjacent along
    // the same play direction of a word already in play)
    private BoardSquare currentLeftBoardSquare;
    private BoardSquare currentRightBoardSquare;

    //
    private List<BoardSquare> dummyBoardSquare = new ArrayList<>();

    private char currentCharacter;
    private char currentRightmostLeftPartCharacter;

    private Set<Character> fullLetterSet;

    public WordSolver(Board board, WordSearchTrie wordSearchTrie, Rack rack) {
        this.board = board;
        this.wordSearchTrie = wordSearchTrie;
        this.rack = rack;

        wordInPlayList = new ArrayList<>();
        anchorBoardSquaresList = new ArrayList<>();
        legalWordsMap = new HashMap<>();

        crossCheckBoardSquareWordList = new ArrayList<>();

        fullLetterSet = board.getTileBag().getFullLetterSet();
    }

    public void generateInitialHighestScoringMove() {
        findInitialLegaWords();
        findHighestScoringMove();

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println("Finished word solver...");
        }
    }

    public void generateHighestScoringMove() {

    }

    private void findHighestScoringMove() {
        List<Map.Entry<WordInPlay, Integer>> legalWordsList =
                new ArrayList<>(legalWordsMap.entrySet());

        if (MainWordSolver.HIGHEST_SCORING_MOVE) {
            System.out.println();
            System.out.println("Legal moves (entire list): ");

            for (Map.Entry<WordInPlay, Integer> legalWordEntry :
                    legalWordsList) {
                System.out.println(legalWordEntry.getKey());
                System.out.println("Score: " + legalWordEntry.getValue());
            }
        }

        legalWordsList.sort(new LegalWordComparator());

        Map.Entry<WordInPlay, Integer> bestMove =
                legalWordsList.get(legalWordsList.size() - 1);

        System.out.println("Highest scoring move: " +
                bestMove.getKey());
        System.out.println("Score: " + bestMove.getValue());
    }

    private void findLegaWords() {

    }

    private void findInitialLegaWords() {
        findWordsInPlay();
        performInitialPreliminarySearch();
        CharacterNode wordSearchTrieRoot = wordSearchTrie.getRoot();

        // FIXME
        if (MainWordSolver.FIND_LEGAL_WORD) {
            System.out.println();
            System.out.println("Now finding possible words");
            System.out.println();

            System.out.println("Anchor squares:");

            for (BoardSquare boardSquare : anchorBoardSquaresList) {
                boardSquare.printAnchorSquare();
            }

            System.out.println();
        }

        for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
            Anchor currentAnchor = anchorBoardSquare.getAnchor();
            AnchorType primaryAnchorType =
                    currentAnchor.getPrimaryAnchorType();
            AnchorType secondaryAnchorType =
                    currentAnchor.getSecondaryAnchorType();

            if (MainWordSolver.FIND_LEGAL_WORD) {
                System.out.println();
                System.out.println("NEW ANCHOR SQUARE");
            }

            PlayDirection primaryPlayDirection =
                    currentAnchor.getPrimaryDirection();
            setLetterIndices(primaryPlayDirection,
                    anchorBoardSquare);

            // FIXME
            if (MainWordSolver.FIND_LEGAL_WORD) {
                System.out.println();
                System.out.println("Current anchor square: ");
                anchorBoardSquare.printAnchorSquare();
                System.out.println("Left limit (" + primaryPlayDirection
                        + "):" +
                        currentAnchor.getLeftLimit(
                                primaryPlayDirection));
            }

            currentLeftBoardSquare = anchorBoardSquare;
            currentRightBoardSquare = null;

            if (MainWordSolver.FIND_LEGAL_WORD) {
                System.out.println();
                printLetterIndices();
            }

            currentLeftPart = new WordInPlay(
                    primaryPlayDirection, "",
                    firstLetterIndex, lastLetterIndex,
                    rowColumnLetterIndex,
                    new ArrayList<>());

            int leftLimit;
            if (primaryAnchorType != null) {
                if (MainWordSolver.FIND_LEGAL_WORD) {
                    System.out.println();
                    System.out.println("Checking " + primaryPlayDirection +
                            " for:");
                    anchorBoardSquare.printFullBoardSquareInfo();
                }

                switch (primaryAnchorType) {
                    case PRIMARY_CENTER_HEAD:
                        currentAnchorType = AnchorType.PRIMARY_CENTER_HEAD;
                        leftLimit = currentAnchor.getLeftLimit(
                                primaryPlayDirection);

//                        if (MainWordSolver.FIND_WORD_DEBUG) {
//                            System.out.println();
//                            System.out.println("Current left part: " +
//                                    currentLeftPart);
//                        }

                        leftPart(new WordInPlay(
                                        currentLeftPart),
                                wordSearchTrieRoot,
                                leftLimit,
                                anchorBoardSquare,
                                primaryPlayDirection);
                        break;
                    case PRIMATE_SIDE_HEAD:
                        currentAnchorType = AnchorType.PRIMATE_SIDE_HEAD;
                        leftLimit = currentAnchor.getLeftLimit(
                                primaryPlayDirection);

//                        if (MainWordSolver.FIND_WORD_DEBUG) {
//                            System.out.println();
//                            System.out.println("Current left part: " +
//                                    currentLeftPart);
//                        }

                        leftPart(new WordInPlay(
                                currentLeftPart),
                                wordSearchTrieRoot,
                                leftLimit,
                                anchorBoardSquare,
                                primaryPlayDirection);
                        break;
                    case PRIMARY_BODY:
                        currentAnchorType = AnchorType.PRIMARY_BODY;
                        extendRight(new WordInPlay(
                                        currentLeftPart),
                                wordSearchTrieRoot,
                                anchorBoardSquare,
                                primaryPlayDirection);
                        break;
                    default:
                        //throw new InternalError("Invalid Primary Anchor type");
                }
            }

            PlayDirection secondaryPlayDirection =
                    currentAnchor.getSecondaryDirection();
            setLetterIndices(secondaryPlayDirection,
                    anchorBoardSquare);

            // FIXME
            if (MainWordSolver.FIND_LEGAL_WORD) {
                System.out.println();
                System.out.println("Current anchor square: ");
                anchorBoardSquare.printAnchorSquare();
                System.out.println("Left limit (" + secondaryPlayDirection
                        + "):" +
                        currentAnchor.getLeftLimit(
                                secondaryPlayDirection));
            }

            currentLeftBoardSquare = anchorBoardSquare;
            currentRightBoardSquare = null;

            currentLeftPart = new WordInPlay(
                    secondaryPlayDirection, "",
                    firstLetterIndex, lastLetterIndex,
                    rowColumnLetterIndex,
                    new ArrayList<>());

            if (secondaryAnchorType != null) {
                if (MainWordSolver.FIND_LEGAL_WORD) {
                    System.out.println();
                    System.out.println("Checking " + secondaryPlayDirection +
                            " for:");
                    anchorBoardSquare.printFullBoardSquareInfo();
                }

                switch (secondaryAnchorType) {
                    case SECONDARY_END:
                        currentAnchorType = AnchorType.SECONDARY_END;
                        leftLimit = currentAnchor.getLeftLimit(
                                secondaryPlayDirection);
                        leftPart(new WordInPlay(
                                currentLeftPart),
                                wordSearchTrieRoot,
                                leftLimit, anchorBoardSquare,
                                secondaryPlayDirection);
                        break;
                    case SECONDARY_BODY:
                        currentAnchorType = AnchorType.SECONDARY_BODY;
                        leftLimit = currentAnchor.getLeftLimit(
                                secondaryPlayDirection);
                        leftPart(new WordInPlay(
                                        currentLeftPart),
                                wordSearchTrieRoot,
                                leftLimit, anchorBoardSquare,
                                secondaryPlayDirection);
                        break;
                    default:
                        //throw new InternalError("Invalid Secondary Anchor type");
                }
            }
        }
    }

    private void printLetterIndices() {
        System.out.println("Letter indices:");
        System.out.println("First letter: " + firstLetterIndex);
        System.out.println("Last letter: " + lastLetterIndex);
        System.out.println("Row/column: " + rowColumnLetterIndex);
    }

    // TODO: Add the possibility of a blank tile... - DONE
    // TODO: Now, fix the traversal of the trie...(search the entire thing
    //  for each new part...)
    private void leftPart(WordInPlay partialWord, CharacterNode currentNode,
                          int leftLimit, BoardSquare anchorSquare,
                          PlayDirection playDirection) {
        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current left part: " +
                    currentLeftPart);
            System.out.println("Word play direction: " + playDirection);
            System.out.println("Left part: current left limit - " + leftLimit);
            anchorSquare.printAnchorSquare();
            currentLeftPart.printWordBoardSquares();
        }

        extendRight(partialWord, currentNode,
                anchorSquare, playDirection);

        if (leftLimit > 0) {
            for (Map.Entry<Tile, Character> tileCharacterEntry :
                    rack.getRackMap().entrySet()) {
                Tile currentTile = tileCharacterEntry.getKey();

                if (Tile.isBlankTile(currentTile)) {
                    for (Character letter : fullLetterSet) {
                        currentCharacter = letter;

                        updateAndRevertLeftPart(leftLimit,
                                anchorSquare,
                                playDirection, currentTile);
                    }
                }

                currentCharacter = currentTile.getLetter();

                updateAndRevertLeftPart(leftLimit,
                        anchorSquare, playDirection,
                        currentTile);
            }

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
        }
    }

    private void updateAndRevertLeftPart(int leftLimit,
                                         BoardSquare anchorSquare,
                                         PlayDirection playDirection,
                                         Tile currentTile) {
        rack.removeTile(currentTile);

        updateBlankTile(currentTile);
        updateLeftPart(playDirection);

        CharacterNode youngestChild =
                wordSearchTrie.getYoungestChild(
                        currentLeftPart.getLeftPart());

        leftPart(new WordInPlay(
                currentLeftPart),
                youngestChild,
                leftLimit - 1, anchorSquare,
                playDirection);

        rack.addTile(currentTile);

        revertBlankTile(currentTile);
        revertLeftPart(currentTile,
                anchorSquare);
    }

    private void updateLeftPart(PlayDirection playDirection) {
        currentLeftPart.updateLeftPartWord(
                currentCharacter);
        currentLeftBoardSquare = addBoardSquareToDummy(
                getBoardSquareInCheckDirection(
                        currentLeftBoardSquare,
                        playDirection
                                .getReverseCheckDirection()));
        currentLeftPart.addWordBoardSquareToBeginning(
                getBoardSquareFromDummy());

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current left board square (add): ");
            currentLeftBoardSquare.printFullBoardSquareInfo();
        }
    }

    private void revertLeftPart(Tile currentTile, BoardSquare anchorSquare) {
        currentLeftPart.removeLeftPartWord();
        currentLeftPart.removeWordBoardSquareAtBeginning();
        currentLeftBoardSquare =
                currentLeftPart.getWordBoardSquareAtBeginning();
        if (currentLeftBoardSquare == null) {
            currentLeftBoardSquare = anchorSquare;
        }

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current left board square " +
                    "(remove): ");
            currentLeftBoardSquare.printFullBoardSquareInfo();
        }
    }

    // TODO: Add the possibility of a blank tile...
    // TODO: also, make sure that current words in play (or even parts of
    //  those words) aren't added to the legal words
    private void extendRight(WordInPlay partialWord,
                             CharacterNode currentNode,
                             BoardSquare currentBoardSquare,
                             PlayDirection playDirection) {
        if (currentBoardSquare != null
                && currentBoardSquare.getBoardSquareType()
                != BoardSquareType.LETTER) {
            checkLegalWord(partialWord, currentNode);

            if (currentNode.getChildrenMap() != null) {
                for (Map.Entry<Character, CharacterNode> childNodeEntry
                        : currentNode.getChildrenMap().entrySet()) {
                    currentCharacter = childNodeEntry.getKey();
                    Tile currentTile = rack.searchLetter(
                            currentCharacter);

                    if (currentTile != null) {
                        Set<Character> crossCheckSet =
                                currentBoardSquare.
                                        getCrossCheckSet(
                                                playDirection);
                        if (crossCheckSet.contains(currentCharacter)) {
                            rack.removeTile(currentTile);

                            updateBlankTile(currentTile);
                            updateRightExtend(partialWord,
                                    currentCharacter,
                                    playDirection,
                                    currentBoardSquare);

                            extendRight(partialWord,
                                    childNodeEntry.getValue(),
                                    currentRightBoardSquare,
                                    playDirection);

                            rack.addTile(currentTile);

                            revertBlankTile(currentTile);
                            revertRightExtend(partialWord);
                        }
                    }
                }
            }
        } else if (currentBoardSquare != null) { // where the
            // currentBoardSquare IS a letter tile

            // TODO: implement the recursive part...add tile after back to
            //  rack after

            BoardSquare nextBoardSquare =
                    getBoardSquareInCheckDirection(currentBoardSquare,
                    playDirection.getCheckDirection());
            if (nextBoardSquare == null || nextBoardSquare.getBoardSquareType()
                    != BoardSquareType.LETTER) {
                checkLegalWord(partialWord, currentNode);
            }

            currentCharacter = currentBoardSquare.getLetter();

            // FIXME
            if (MainWordSolver.WORD_RECURSIVE) {
                System.out.println();
                System.out.println("Word chain...");
                System.out.println("Current character (letter tile): "  +
                        currentCharacter);
                System.out.println("Current node: " + currentNode);
            }

            CharacterNode childNode =
                    currentNode.getChildNode(currentCharacter);

            if (childNode != null) {
                updateRightExtend(partialWord,
                        currentCharacter, playDirection,
                        currentBoardSquare);

                // FIXME
                if (MainWordSolver.WORD_RECURSIVE) {
                    System.out.println();
                    System.out.println("Current word: " + partialWord);
                    partialWord.printWordBoardSquares();

                    if (!(nextBoardSquare == null
                            || nextBoardSquare.getBoardSquareType()
                            != BoardSquareType.LETTER)) {
                        // FIXME
                        if (MainWordSolver.WORD_RECURSIVE) {
                            System.out.println();
                            System.out.println("Chain is not complete yet...");
                        }
                    } else {
                        System.out.println();
                        System.out.println("Finished chain reached on " +
                                "next letter...");
                    }
                }

                extendRight(partialWord,
                        childNode,
                        currentRightBoardSquare,
                        playDirection);

                revertRightExtend(partialWord);
            } else {
                System.out.println();
                System.out.println("Chain broken");
                System.out.println("The word " + partialWord.getWord() +
                        currentCharacter + " is not a word...");
                System.out.println("Word before break: " + partialWord);
                partialWord.printWordBoardSquares();
            }
        } else { // edge of the board has been reached...
            checkLegalWord(partialWord, currentNode);
        }
    }

    private void updateBlankTile(Tile currentTile) {
        if (Tile.isBlankTile(currentTile)) {
            currentCharacter =
                    Character.toUpperCase(
                            currentCharacter);
            currentTile.updateBlankLetter(
                    currentCharacter);

            if (MainWordSolver.WORD_RECURSIVE) {
                System.out.println();
                System.out.println("Using blank tile as: " +
                        currentCharacter);
            }
        }
    }

    private void revertBlankTile(Tile currentTile) {
        if (Tile.isBlankTile(currentTile)) {
            currentTile.revertBlankLetter();
        }
    }

    private void updateRightExtend(WordInPlay partialWord,
                                   char currentCharacter,
                                   PlayDirection playDirection,
                                   BoardSquare currentBoardSquare) {
        partialWord.updateRightExtendWord(
                currentCharacter);

//        if (currentRightBoardSquare == null) {
//            currentRightBoardSquare =
//                    addBoardSquareToDummy(
//                            currentBoardSquare);
//        } else {
//            currentRightBoardSquare =
//                    addBoardSquareToDummy(
//                            getBoardSquareInCheckDirection(
//                                    currentRightBoardSquare,
//                                    playDirection
//                                            .getCheckDirection()));
//        }

        currentRightBoardSquare =
                addBoardSquareToDummy(
                        currentBoardSquare);

        partialWord.addWordBoardSquareToEnd(
                getBoardSquareFromDummy());

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current right board " +
                    "square (add): ");
            currentRightBoardSquare
                    .printFullBoardSquareInfo();
        }

//            BoardSquare nextBoardSquare =
//                    getBoardSquareInCheckDirection(
//                            currentBoardSquare,
//                            playDirection
//                                    .getCheckDirection());
        currentRightBoardSquare =
                getBoardSquareInCheckDirection(
                        currentBoardSquare,
                        playDirection
                                .getCheckDirection());
    }

    private void revertRightExtend(WordInPlay partialWord) {
        partialWord.removeRightExtendWord();
        partialWord.removeWordBoardSquareAtEnd();
        currentRightBoardSquare =
                partialWord.getWordBoardSquareAtEnd();

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current right board " +
                    "square " +
                    "(remove): ");
            BoardSquare.printFullBoardSquareInfo(
                    currentRightBoardSquare);
        }
    }

    private void checkLegalWord(WordInPlay partialWord,
                                CharacterNode currentNode) {
        if (currentNode.isTerminalNode()) {
            if (MainWordSolver.FIND_LEGAL_WORD) {
                System.out.println();
                System.out.println("Found word at node: " + currentNode);
                System.out.println("Left part: " +
                        partialWord.getLeftPart());
                System.out.println("Right part: " +
                        partialWord.getRightPart());
                partialWord.printWordBoardSquares();
                System.out.println(partialWord);

                if (wordInPlayList.contains(partialWord)) {
                    if (MainWordSolver.WORD_RECURSIVE) {
                        System.out.println(partialWord.getWord() +
                                " is already " +
                                "in play...");
                    }

                    return;
                }
            }

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

            if (!partialWord.getRightPart().isEmpty()) {
                if (wordSearchTrie.searchWord(partialWord.getWord())) {
                    // FIXME: almost there...
//                        addLegalWord(new WordInPlay(
//                                partialWord));

                    if (MainWordSolver.FIND_LEGAL_WORD) {
                        System.out.println("Legal word added: " +
                                partialWord);
                    }
                } else {
                    if (MainWordSolver.FIND_LEGAL_WORD) {
                        System.out.println("Not a real word...");
                    }
                }
            } else {
                if (MainWordSolver.FIND_LEGAL_WORD) {
                    System.out.println("Not a legal move...(not touching " +
                            "another word)");
                }
            }
        }
    }

    // TODO: fix so that the board squares will temporarily have active tiles
    //  on them just to calculate the score and find the best move...
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
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Current anchor square:");
                anchorBoardSquare.printAnchorSquare();
                anchorBoardSquare.printCrossChecks();
            }

            for (PlayDirection playDirection : PlayDirection.values()) {
                // FIXME
                if (MainWordSolver.ANCHOR_DEBUG) {
                    System.out.println();
                    System.out.println("Current direction: " +
                            playDirection);
                }

                if (anchorBoardSquare.getCrossCheck(
                        playDirection)) {
                    crossCheckBoardSquareWordList = new ArrayList<>();
                    Set<Character> crossCheckSet =
                            anchorBoardSquare.
                                    getCrossCheckSet(playDirection);

                    // FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("The checks should be in the " +
                                "OPPOSITE direction of word play direction of" +
                                " a legal word...so playing a horizontal word" +
                                " uses the vertical cross check set");
                        System.out.println("Initial cross check list:");
                        System.out.println(crossCheckSet);
                        System.out.println();
                    }

                    String firstPart = firstPartCrossCheck(
                            anchorBoardSquare,
                            playDirection);

                    // FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("Cross check first part: " +
                                firstPart);
                        System.out.println();
                    }

                    String secondPart = secondPartCrossCheck(
                            anchorBoardSquare,
                            playDirection);

                    // FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("Cross check second part: " +
                                secondPart);
                        System.out.println();
                    }

                    setLetterIndices(playDirection,
                            anchorBoardSquare);

                    // FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println("Cross check word:");
                        System.out.println(crossCheckBoardSquareWordList);
                    }

                    CrossCheckWord crossCheckWord =
                            new CrossCheckWord(playDirection,
                                    firstPart + secondPart,
                                    firstLetterIndex,
                                    lastLetterIndex,
                                    rowColumnLetterIndex,
                                    crossCheckBoardSquareWordList);
                    anchorBoardSquare.addCrossCheckWord(playDirection,
                            crossCheckWord);

                    // FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("Cross check word:");
                        System.out.println(crossCheckWord);
                    }

                    Iterator<Character> crossCheckIterator =
                            crossCheckSet.iterator();
                    while (crossCheckIterator.hasNext()) {
                        char currentCrossCheckCharacter =
                                crossCheckIterator.next();

                        String potentialWord =
                                firstPart + currentCrossCheckCharacter +
                                        secondPart;

                        if (!wordSearchTrie.searchWord(potentialWord)) {
                            if (MainWordSolver.ANCHOR_DEBUG) {
                                System.out.println("Invalid word: " +
                                        potentialWord);
                            }

//                            char invalidLetter =
//                                    anchorBoardSquare.getLetter();
                            crossCheckIterator.remove();

//                            if (MainWordSolver.DEBUG) {
//                                System.out.println("Removing the character " +
//                                        anchorBoardSquare.getLetter());
//                                System.out.println("Updated cross check set:");
//                                System.out.println(crossCheckSet);
//                            }
                        } else {
                            if (MainWordSolver.ANCHOR_DEBUG) {
                                System.out.println("Found word: " +
                                        potentialWord);
                            }
                        }
                    }

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

                    // FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("Final cross check list:");
                        System.out.println(crossCheckSet);
                    }
                } else {
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("No cross check needed...");
                    }
                }

                if (anchorBoardSquare.getCrossCheck(
                        PlayDirection.HORIZONTAL)
                        && anchorBoardSquare.getCrossCheck(
                        PlayDirection.VERTICAL)) {
                    //FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("Merging the two sets...");
                    }

                    BoardSquare.mergeCrossCheckSets(anchorBoardSquare);

                    //FIXME
                    if (MainWordSolver.ANCHOR_DEBUG) {
                        System.out.println();
                        System.out.println("Horizontal cross check list:");
                        System.out.println(anchorBoardSquare.getCrossCheckSet(
                                PlayDirection.HORIZONTAL));
                        System.out.println("Vertical cross check list:");
                        System.out.println(anchorBoardSquare.getCrossCheckSet(
                                PlayDirection.VERTICAL));
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

        BoardSquare previousBoardSquare = addBoardSquareToDummy(
                getBoardSquareInCheckDirection(
                anchorBoardSquare,
                playDirection.getReverseCheckDirection()));

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println("First part call: ");
            assert previousBoardSquare != null;
            previousBoardSquare.printFullBoardSquareInfo();
        }

        while (previousBoardSquare != null
                && previousBoardSquare.getBoardSquareType()
                == BoardSquareType.LETTER) {
            crossCheckBoardSquareWordList.add(0,
                    getBoardSquareFromDummy());
            firstPart = previousBoardSquare.getLetter() + firstPart;

            previousBoardSquare = addBoardSquareToDummy(
                    getBoardSquareInCheckDirection(
                    previousBoardSquare,
                    playDirection.getReverseCheckDirection()));

            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println("First part call: ");
                assert previousBoardSquare != null;
                previousBoardSquare.printFullBoardSquareInfo();
            }
        }

        return firstPart;
    }

    private String secondPartCrossCheck(BoardSquare anchorBoardSquare,
                                        PlayDirection playDirection) {
        String secondPart = "";

        BoardSquare nextBoardSquare = addBoardSquareToDummy(
                getBoardSquareInCheckDirection(
                        anchorBoardSquare,
                playDirection.getCheckDirection()));

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println("Second part call: ");
            assert nextBoardSquare != null;
            nextBoardSquare.printFullBoardSquareInfo();
        }

        while (nextBoardSquare != null
                && nextBoardSquare.getBoardSquareType()
                == BoardSquareType.LETTER) {
            crossCheckBoardSquareWordList.add(getBoardSquareFromDummy());
            secondPart += nextBoardSquare.getLetter();

            nextBoardSquare = addBoardSquareToDummy(
                    getBoardSquareInCheckDirection(
                    nextBoardSquare,
                    playDirection.getCheckDirection()));

            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println("Second part call: ");
                assert nextBoardSquare != null;
                nextBoardSquare.printFullBoardSquareInfo();
            }
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

            // FIXME
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Current word board square list: ");
                System.out.println();

                for (BoardSquare boardSquare : wordBoardSquareList) {
                    boardSquare.printFullBoardSquareInfo();
                }
            }

            // FIXME
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Inside anchors:");
                System.out.println();
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

            // FIXME
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Outside anchors:");
                System.out.println();
            }

            // FIXME
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Side body anchors - up/right");
                System.out.println();
            }

            // add the side body anchor squares (in the "buffer" region that
            // doesn't contain a letter yet -- adjacent to the word and
            // played along the same play direction)
            CheckDirection oppositePlayCheckDirection =
                    PlayDirection.getOtherPlayDirection(
                            wordPlayDirection)
                            .getReverseCheckDirection();

            first = true;
            for (BoardSquare boardSquare : wordBoardSquareList) {
                BoardSquare firstHeadBoardSquare =
                        getBoardSquareInCheckDirection(
                                boardSquare,
                                oppositePlayCheckDirection);

                if (firstHeadBoardSquare == null) {
                    break;
                }

                if (first) {
                    if (checkNewAnchorSquare(
                            firstHeadBoardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(firstHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMATE_SIDE_HEAD,
                                null);
                    }
                } else {
                    addAnchorSquare(firstHeadBoardSquare,
                            wordPlayDirection,
                            AnchorType.PRIMARY_BODY,
                            null);
                }

                first = false;
            }

            // FIXME
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Side body anchors - down/left");
                System.out.println();
            }

            oppositePlayCheckDirection = PlayDirection.getOtherPlayDirection(
                            wordPlayDirection).getCheckDirection();

            first = true;
            for (BoardSquare boardSquare : wordBoardSquareList) {
                BoardSquare secondHeadBoardSquare =
                        getBoardSquareInCheckDirection(
                                boardSquare,
                                oppositePlayCheckDirection);

                if (secondHeadBoardSquare == null) {
                    break;
                }

                if (first) {
                    if (checkNewAnchorSquare(
                            secondHeadBoardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(secondHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMATE_SIDE_HEAD,
                                null);
                    }
                } else {
                    addAnchorSquare(secondHeadBoardSquare,
                            wordPlayDirection,
                            AnchorType.PRIMARY_BODY,
                            null);
                }


                first = false;
            }

            // FIXME
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Checking the (secondary) end anchors");
                System.out.println();
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

    /**
     * Checks if the given board square was already added as an anchor square
     * (overlapping anchor squares should only be counted once since the
     * orientation could just be rotated to cover both play directions)
     *
     * @param boardSquare
     * @param playDirection
     * @return
     */
    private boolean checkNewAnchorSquare(BoardSquare boardSquare,
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
     * Note: the ends of the word cannot contain a letter along the play
     * direction of the word (else, something might be wrong with the word
     * search algorithm...)
     *
     * @param boardSquare
     * @param checkDirection
     * @return
     */
    private boolean checkSecondaryEndAnchor(BoardSquare boardSquare,
                                            CheckDirection checkDirection) {
        return !boardSquare.checkEdge(checkDirection);
    }

    private void addAnchorSquare(BoardSquare boardSquare,
                                 PlayDirection wordPlayDirection,
                                 AnchorType primaryAnchorType,
                                 AnchorType secondaryAnchorType) {
        Anchor anchor = new Anchor(wordPlayDirection,
                primaryAnchorType,
                secondaryAnchorType);
        boardSquare.setAnchor(anchor);
        boardSquare.
                initiateCrossChecks(
                wordPlayDirection);

        // FIXME
        if (MainWordSolver.ANCHOR_DEBUG) {
            boardSquare.printAnchorSquare();
        }

        if (primaryAnchorType != null) {
            // FIXME: something is wrong here...
            anchor.setLeftLimit(findLeftLimitAnchor(
                    boardSquare,
                    wordPlayDirection), wordPlayDirection);
        }

        anchorBoardSquaresList.add(boardSquare);
    }

    private int findLeftLimitAnchor(BoardSquare boardSquare,
                                    PlayDirection playDirection) {
        int leftLimit = 0;
        BoardSquare previousBoardSquare = getBoardSquareInCheckDirection(
                boardSquare,
                playDirection.getReverseCheckDirection());

        while (previousBoardSquare != null) {
            if (previousBoardSquare.getAnchor() != null) {
                return leftLimit - 1; // so the left part doesn't touch
                // another anchor square
            }

            // FIXME: fix 1
            previousBoardSquare = getBoardSquareInCheckDirection(
                    previousBoardSquare,
                    playDirection.getReverseCheckDirection());
            leftLimit++;
        }

        return leftLimit;
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
                return board
                        .getBoardSquare(boardSquare.getRowIndex() - 1,
                                boardSquare.getColumnIndex());
            }
        } else if (checkDirection == CheckDirection.DOWN) {
            if (!boardSquare.isBottomEdge()) {
                return board
                        .getBoardSquare(boardSquare.getRowIndex() + 1,
                                boardSquare.getColumnIndex());
            }
        } else if (checkDirection == CheckDirection.LEFT) {
            if (!boardSquare.isLeftEdge()) {
                return board
                        .getBoardSquare(boardSquare.getRowIndex(),
                                boardSquare.getColumnIndex() - 1);
            }
        } else { // Right
            if (!boardSquare.isRightEdge()) {
                return board
                        .getBoardSquare(boardSquare.getRowIndex(),
                                boardSquare.getColumnIndex() + 1);
            }
        }

        return null;
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

        // FIXME
        if (MainWordSolver.WORDS_IN_PLAY) {
            System.out.println();
            System.out.println("Words in play:");

            for (WordInPlay wordInPlay : wordInPlayList) {
                System.out.print("Word in play - ");
                System.out.println(wordInPlay);
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
     *
     * @param playDirection
     * @param boardSquare
     */
    private void checkWordAlongDirection(PlayDirection playDirection,
                                         BoardSquare boardSquare) {
        String temporaryWord = "" + boardSquare.getLetter();
        BoardSquare nextBoardSquare;

        setLetterIndices(playDirection, boardSquare);

        List<BoardSquare> wordBoardSquares = new ArrayList<>();
        wordBoardSquares.add(boardSquare);

//        RowColumn rowColumn = new RowColumn(
//                boardSquare.getRowIndex(),
//                boardSquare.getColumnIndex(), dimension);

        boardSquare.checkPlayDirection(playDirection);

        nextBoardSquare = addBoardSquareToDummy(
                getBoardSquareInCheckDirection(boardSquare,
                playDirection.getCheckDirection()));

        while (nextBoardSquare != null && nextBoardSquare
                .getBoardSquareType()
                == BoardSquareType.LETTER) {
            nextBoardSquare.checkPlayDirection(playDirection);
            wordBoardSquares.add(getBoardSquareFromDummy());

            temporaryWord += nextBoardSquare.getLetter();
//            rowColumn.applyCheckDirection(
//                    playDirection.getCheckDirection());
            lastLetterIndex++;

            nextBoardSquare = addBoardSquareToDummy(
                    getBoardSquareInCheckDirection(
                            nextBoardSquare,
                    playDirection.getCheckDirection()));
        }

        if (temporaryWord.length() > 1) {
            WordInPlay wordInPlay = new WordInPlay(playDirection,
                    temporaryWord, firstLetterIndex,
                    lastLetterIndex, rowColumnLetterIndex,
                    wordBoardSquares);
            wordInPlayList.add(wordInPlay);
        }
    }

    private BoardSquare addBoardSquareToDummy(BoardSquare boardSquare) {
        dummyBoardSquare.clear();
        dummyBoardSquare.add(boardSquare);
        return boardSquare;
    }

    private BoardSquare getBoardSquareFromDummy() {
        return dummyBoardSquare.get(0);
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

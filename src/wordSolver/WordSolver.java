package wordSolver;

import comparators.LegalWordComparator;
import constants.*;
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

        // Sort before printing out the list...
        legalWordsList.sort(new LegalWordComparator());

        if (MainWordSolver.HIGHEST_SCORING_MOVE) {
            System.out.println();
            System.out.println("Legal moves (entire list): ");

            for (Map.Entry<WordInPlay, Integer> legalWordEntry :
                    legalWordsList) {
                System.out.println(legalWordEntry.getKey());
                System.out.println("Score: " + legalWordEntry.getValue());
            }
        }

//        // Sort before printing out the list...
//        legalWordsList.sort(new LegalWordComparator());

        Map.Entry<WordInPlay, Integer> bestMove =
                legalWordsList.get(legalWordsList.size() - 1);

        if (MainWordSolver.HIGHEST_SCORING_MOVE) {
            System.out.println();
            System.out.println("Highest scoring move: " +
                    bestMove.getKey());
            System.out.println("Score: " + bestMove.getValue());
        }
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
            currentRightBoardSquare = anchorBoardSquare;

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
                    case PRIMARY_SIDE_HEAD:
                        currentAnchorType = AnchorType.PRIMARY_SIDE_HEAD;
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
                    case PRIMARY_SIDE_BODY:
                        currentAnchorType = AnchorType.PRIMARY_SIDE_BODY;
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
            currentRightBoardSquare = anchorBoardSquare;

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

        if (currentNode != null) {
            extendRight(partialWord, currentNode,
                    anchorSquare, playDirection);

            if (leftLimit > 0) {
                if (MainWordSolver.WORD_RECURSIVE) {
                    System.out.println();
                    System.out.println("Current rack:");
                    rack.printRack();
                }

                // Only used for iterating so concurrent modification errors
                // doesn't occur...
                Map<Tile, Character> rackIterateMap =
                        new HashMap<>(rack.getRackMap());

                for (Map.Entry<Tile, Character> tileCharacterEntry :
                        rackIterateMap.entrySet()) {
                    Tile currentTile =
                            tileCharacterEntry.getKey();

                    if (Tile.isBlankTile(currentTile)) {
                        for (Character letter : fullLetterSet) {
                            currentCharacter = letter;

                            updateAndRevertLeftPart(leftLimit,
                                    anchorSquare,
                                    playDirection,
                                    currentTile);
                        }
                    } else {
                        currentCharacter = currentTile.getLetter();

                        updateAndRevertLeftPart(leftLimit,
                                anchorSquare,
                                playDirection,
                                currentTile);
                    }
                }

//                for (Map.Entry<Tile, Character> tileCharacterEntry :
//                        rack.getRackMap().entrySet()) {
//                    Tile currentTile = tileCharacterEntry.getKey();
//
//                    if (Tile.isBlankTile(currentTile)) {
//                        for (Character letter : fullLetterSet) {
//                            currentCharacter = letter;
//
//                            updateAndRevertLeftPart(leftLimit,
//                                    anchorSquare,
//                                    playDirection,
//                                    currentTile);
//                        }
//                    } else {
//                        currentCharacter = currentTile.getLetter();
//
//                        updateAndRevertLeftPart(leftLimit,
//                                anchorSquare,
//                                playDirection,
//                                currentTile);
//                    }
//                }
            }
        } else {
            if (MainWordSolver.WORD_RECURSIVE) {
                System.out.println();
                System.out.println(currentLeftPart.getLeftPart() + ": invalid" +
                        " left part...");
            }
        }
    }

    private void updateAndRevertLeftPart(int leftLimit,
                                         BoardSquare anchorSquare,
                                         PlayDirection playDirection,
                                         Tile currentTile) {
        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current character: " + currentCharacter);
        }

        rack.removeTile(currentTile);

        updateBlankTile(currentTile);
        updateLeftPart(playDirection, currentTile);

        CharacterNode youngestChild =
                wordSearchTrie.getYoungestChild(
                        currentLeftPart.getLeftPart());

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println("Youngest child node: " + youngestChild);
        }

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

    private void updateLeftPart(PlayDirection playDirection, Tile currentTile) {
        currentLeftPart.updateLeftPartWord(
                currentCharacter);
        currentLeftBoardSquare = addBoardSquareToDummy(
                getBoardSquareInCheckDirection(
                        currentLeftBoardSquare,
                        playDirection
                                .getReverseCheckDirection()));
        currentLeftPart.addWordBoardSquareToBeginning(
                getBoardSquareFromDummy());

        if (currentLeftBoardSquare != null) {
            currentLeftBoardSquare.setActiveTile(currentTile);

            if (MainWordSolver.ACTIVE_TILE) {
                System.out.println();
                System.out.println("Left word part: " +
                        currentLeftPart.getLeftPart());
                System.out.println("Tile: " + currentTile);
                System.out.println("Adding active tile to: ");
                currentLeftBoardSquare.printFullBoardSquareInfo();
            }
        }

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current left board square (add): ");
            currentLeftBoardSquare.printFullBoardSquareInfo();
        }
    }

    private void revertLeftPart(Tile currentTile, BoardSquare anchorSquare) {
        currentLeftPart.removeLeftPartWord();
        currentLeftPart.removeWordBoardSquareAtBeginning();

        if (currentLeftBoardSquare != null) {
            currentLeftBoardSquare.removeActiveTile();

            if (MainWordSolver.ACTIVE_TILE) {
                System.out.println();
                System.out.println("Left word part: " +
                        currentLeftPart.getLeftPart());
                System.out.println("Removing active tile from: ");
                currentLeftBoardSquare.printFullBoardSquareInfo();
            }
        }

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
                if (MainWordSolver.WORD_RECURSIVE) {
                    System.out.println();
                    currentNode.printChildren();
                }

                if (MainWordSolver.PRINT_CHILDREN_NODES) {
                    // FIXME: index 2 - horizontal
                    if (currentBoardSquare.getRowIndex() == 2) {
                        System.out.println();
                        System.out.println("Current anchor square:");
                        currentBoardSquare.printFullBoardSquareInfo();
                        System.out.println("Partial word: " + partialWord);
                        System.out.println("Children nodes:");
                        currentNode.printChildren();
                    }
                }

                // FIXME: decide to use currentRightBoardSquare...
                BoardSquare nextBoardSquare;

                if (partialWord.getRightPart().isEmpty()) {
                    nextBoardSquare = currentBoardSquare;
                } else {
                    nextBoardSquare =
                            getBoardSquareInCheckDirection(
                                    currentBoardSquare,
                                    playDirection
                                            .getCheckDirection());
                }

                if (nextBoardSquare != null) {
                    for (Map.Entry<Character, CharacterNode> childNodeEntry
                            : currentNode.getChildrenMap().entrySet()) {
                        currentCharacter = childNodeEntry.getKey();
                        Tile currentTile = rack.searchLetter(
                                currentCharacter);

                        if (MainWordSolver.ACTIVE_TILE) {
                            System.out.println();
                            System.out.println("Current: not letter square");
                            System.out.println("Tile: " + currentTile);
                            partialWord.printWordBoardSquares();
                        }

                        if (MainWordSolver.WORD_RECURSIVE) {
                            System.out.println();
                            System.out.println("Current character (child): " +
                                    currentCharacter);
                        }

                        if (currentTile != null) {
                            // FIXME: need to check the OPPOSITE play direction
                            //  for the cross check...can't fit all in 80
                            //  characters...

                            // FIXME: forgot to check the NEXT squares cross
                            //  check set...
                            Set<Character> crossCheckSet =
                                    nextBoardSquare
                                            .getCrossCheckSet(
                                                    PlayDirection
                                                            .getOtherPlayDirection(
                                                                    playDirection));

                            if (MainWordSolver.WORD_RECURSIVE) {
                                System.out.println();
                                System.out.println("Cross set: " + crossCheckSet);
                            }

                            if (MainWordSolver.CHECK_CROSS_SET_WHEN_FINDING_WORD) {
                                // FIXME: row index: 2
                                if (currentBoardSquare.getRowIndex() == 2) {
//                                    System.out.println();
//                                    System.out.println("Word: " + partialWord.getWord());
//                                    System.out.println("Word play direction: " +
//                                            playDirection);
//                                    System.out.println("Current anchor square:");
//                                    currentBoardSquare.printFullBoardSquareInfo();
//                                    System.out.println("Tile: " + currentTile);
//                                    System.out.println("Cross set: " + crossCheckSet);
                                }

                                System.out.println();
                                System.out.println("Word: " + partialWord.getWord());
                                System.out.println("Word play direction: " +
                                        playDirection);
                                System.out.println("Current anchor square:");
                                currentBoardSquare.printFullBoardSquareInfo();
                                System.out.println("Tile: " + currentTile);
                                System.out.println("Cross set: " + crossCheckSet);
                            }

                            if (crossCheckSet.contains(currentCharacter)) {
                                rack.removeTile(currentTile);

                                updateBlankTile(currentTile);
                                updateRightExtend(partialWord,
                                        currentCharacter,
                                        playDirection,
                                        currentBoardSquare,
                                        currentTile);

                                extendRight(partialWord,
                                        childNodeEntry.getValue(),
                                        currentRightBoardSquare,
                                        playDirection);

                                rack.addTile(currentTile);

                                revertBlankTile(currentTile);
                                revertRightExtend(partialWord);
                            } else {
                                if (MainWordSolver.WORD_RECURSIVE) {
                                    System.out.println();
                                    System.out.println("Character " +
                                            currentCharacter +
                                            " not in the cross set...");
                                }
                            }
                        } else {
                            if (MainWordSolver.WORD_RECURSIVE) {
                                System.out.println();
                                System.out.println("Tile not found...");
                            }
                        }
                    }
                } else {
                    if (MainWordSolver.CHECK_CROSS_SET_WHEN_FINDING_WORD) {
                        System.out.println();
                        System.out.println("Edge of the board has been " +
                                "reached...");
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
                        currentBoardSquare,
                        currentBoardSquare.getActiveTile());

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
                if (MainWordSolver.WORD_RECURSIVE) {
                    System.out.println();
                    System.out.println("Chain broken");
                    System.out.println("The word " + partialWord.getWord() +
                            currentCharacter + " is not a word...");
                    System.out.println("Word before break: " + partialWord);
                    partialWord.printWordBoardSquares();
                }
            }
        }
        // FIXME...redundant case...
//        else { // edge of the board has been reached...
//            checkLegalWord(partialWord, currentNode);
//        }
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
                                   BoardSquare currentBoardSquare,
                                   Tile currentTile) {
        if (partialWord.getRightPart().isEmpty()) {
            currentRightBoardSquare = addBoardSquareToDummy(
                    currentBoardSquare);
        } else {
            currentRightBoardSquare = addBoardSquareToDummy(
                    getBoardSquareInCheckDirection(
                            currentBoardSquare,
                            playDirection
                                    .getCheckDirection()));
        }

        partialWord.updateRightExtendWord(
                currentCharacter);
        partialWord.addWordBoardSquareToEnd(
                getBoardSquareFromDummy());

        if (currentRightBoardSquare != null
                && currentRightBoardSquare.getBoardSquareType()
                != BoardSquareType.LETTER) {
            currentRightBoardSquare.setActiveTile(currentTile);

            if (MainWordSolver.ACTIVE_TILE) {
                System.out.println();
                System.out.println("Right word part: " +
                        partialWord.getRightPart());
                System.out.println("Tile: " + currentTile);
                System.out.println("Adding active tile to: ");
                currentRightBoardSquare.printFullBoardSquareInfo();
            }
        }

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

//        currentRightBoardSquare =
//                addBoardSquareToDummy(
//                        currentBoardSquare);

        if (MainWordSolver.WORD_RECURSIVE) {
            System.out.println();
            System.out.println("Current right board " +
                    "square (add): ");
//            currentRightBoardSquare
//                    .printFullBoardSquareInfo();
            BoardSquare.printFullBoardSquareInfo(currentBoardSquare);
        }

//            BoardSquare nextBoardSquare =
//                    getBoardSquareInCheckDirection(
//                            currentBoardSquare,
//                            playDirection
//                                    .getCheckDirection());
//        currentRightBoardSquare =
//                getBoardSquareInCheckDirection(
//                        currentBoardSquare,
//                        playDirection
//                                .getCheckDirection());
    }

    private void revertRightExtend(WordInPlay partialWord) {
        partialWord.removeRightExtendWord();
        partialWord.removeWordBoardSquareAtEnd();

        if (currentRightBoardSquare != null
                && currentRightBoardSquare.getBoardSquareType()
                != BoardSquareType.LETTER) {
            currentRightBoardSquare.removeActiveTile();

            if (MainWordSolver.ACTIVE_TILE) {
                System.out.println();
                System.out.println("Right word part: " +
                        partialWord.getRightPart());
                System.out.println("Removing active tile from: ");
                currentRightBoardSquare.printFullBoardSquareInfo();
            }
        }

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
                    addLegalWord(new WordInPlay(
                                partialWord));

                    if (MainWordSolver.FIND_LEGAL_WORD) {
                        System.out.println("Legal word added: " +
                                partialWord);
                    }

                    if (MainWordSolver.PRINT_LEGAL_WORD) {
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
        if (MainWordSolver.SCORE_LEGAL_WORD) {
            System.out.println();
            System.out.println("Completed word: " + completedWord);
            completedWord.printWordBoardSquares();
        }

        int totalScore = completedWord.calculateScore();

        if (MainWordSolver.PRINT_BASE_SCORE) {
            System.out.println();
            System.out.println("Completed word: " + completedWord);
            System.out.println("Base score: " + totalScore);
        }

        // FIXME
        int currentWordIndex = completedWord.getFirstIndex();
        int constantIndex = completedWord.getRowColumnIndex();
        PlayDirection wordPlayDirection = completedWord.getPlayDirection();

        for (BoardSquare wordBoardSquare : completedWord.getWordBoardSquares()) {
            // opposite play direction to the completed word play direction
            CrossCheckWord crossCheckWord =
                    wordBoardSquare.getCrossCheckWord(
                            PlayDirection.getOtherPlayDirection(
                                    wordPlayDirection));

            if (MainWordSolver.SCORE_LEGAL_WORD) {
                System.out.println();
                System.out.println("Current board square:");
                wordBoardSquare.printFullBoardSquareInfo();
                System.out.println("Current index " + currentWordIndex);
            }

            if (crossCheckWord != null) {
                if (MainWordSolver.SCORE_LEGAL_WORD) {
                    System.out.println();
                    System.out.println("Cross check direction: " +
                            PlayDirection.getOtherPlayDirection(
                                    wordPlayDirection));
                    System.out.println("Cross check word: " +
                            crossCheckWord);
                    System.out.println("Cross check index: " +
                            crossCheckWord.getFinalCharIndex());
                }

                int startWordIndex = crossCheckWord.getFirstIndex();

                crossCheckWord.setLetterChoice(completedWord
                                .getLetterAtIndex(currentWordIndex),
                        startWordIndex);
                crossCheckWord.addFinalBoardSquare(wordBoardSquare,
                        startWordIndex);

                if (MainWordSolver.PRINT_CROSS_SCORE) {
                    System.out.println();
                    System.out.println("Cross word: " + crossCheckWord);

                    // FIXME: almost got the scoring to work...
                    //crossCheckWord.printWordBoardSquares();
                    System.out.println("Cross score: " +
                            crossCheckWord.calculateScore());
                }

                totalScore += crossCheckWord.calculateScore();
                crossCheckWord.resetLetterChoice(startWordIndex);
                crossCheckWord.removeFinalBoardSquare(startWordIndex);
            }

            currentWordIndex++;
        }

        if (rack.getRackMap().isEmpty()) {
            int bingo = 50;

            if (MainWordSolver.PRINT_BINGO) {
                System.out.println("BINGO! " + bingo + " points will be " +
                        "awarded to the " +
                        "final score");
            }

            totalScore += bingo;
        }

        if (MainWordSolver.SCORE_LEGAL_WORD) {
            System.out.println();
            System.out.println("Legal word: " + completedWord);
            System.out.println("Score: " + totalScore);
        }

        legalWordsMap.put(completedWord, totalScore);
    }

    private void resetTurn() {

    }

    private void performInitialPreliminarySearch() {
        findInitialAnchorBoardSquares();
        initializeInitialLeftLimits();
        performInitialCrossChecks();
        printAnchorBoardSquares();
        printCrossCheckSets();
    }

    private void printAnchorBoardSquares() {
        if (MainWordSolver.PRINT_ANCHOR_BOARD_SQUARES) {
            System.out.println();
            System.out.println("Anchor board square list:");

            for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
                anchorBoardSquare.printAnchorSquare();
            }
        }
    }

    private void printCrossCheckSets() {
        if (MainWordSolver.PRINT_CROSS_CHECK_SETS) {
            System.out.println();
            System.out.println("CROSS CHECK SETS FOR OUTSIDE ANCHORS");

            for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
                if (anchorBoardSquare.getAnchor().getInsideOutsideAnchor()
                        == InsideOutsideAnchor.OUTSIDE_ANCHOR) {
                    System.out.println();
                    System.out.println("Current board square:");
                    anchorBoardSquare.printFullBoardSquareInfo();
                    System.out.println("Horizontal cross check: " +
                            anchorBoardSquare.getCrossCheckSet(
                                    PlayDirection.HORIZONTAL));
                    System.out.println("Vertical cross check: " +
                            anchorBoardSquare.getCrossCheckSet(
                                    PlayDirection.VERTICAL));
                } else {
                    System.out.println();
                    System.out.println("No cross check sets since it's an " +
                            "INSIDE anchor");
                }
            }
        }
    }

    // TODO: add the search...
    private void performInitialCrossChecks() {
        for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
            if (MainWordSolver.CROSS_CHECK_WORD) {
                System.out.println();
                System.out.println("Current anchor square:");
                anchorBoardSquare.printAnchorSquare();
                anchorBoardSquare.printCrossChecks();
            }

            for (PlayDirection playDirection : PlayDirection.values()) {
                // FIXME
                if (MainWordSolver.CROSS_CHECK_WORD) {
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
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println();
                        System.out.println("The checks should be in the " +
                                "OPPOSITE direction of word play direction of" +
                                " a legal word...so playing a horizontal word" +
                                " uses the vertical cross check set");
                        System.out.println("Initial cross check list:");
                        System.out.println(crossCheckSet);
                        System.out.println();
                    }

                    // FIXME: indices updated in first and second parts...
                    setLetterIndices(playDirection,
                            anchorBoardSquare);

                    String firstPart = firstPartCrossCheck(
                            anchorBoardSquare,
                            playDirection);

                    // FIXME
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println();
                        System.out.println("Cross check first part: " +
                                firstPart);
                        System.out.println();
                    }

                    String secondPart = secondPartCrossCheck(
                            anchorBoardSquare,
                            playDirection);

                    // FIXME
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println();
                        System.out.println("Cross check second part: " +
                                secondPart);
                        System.out.println();
                    }

                    // FIXME
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println("Cross check word:");
                        System.out.println(crossCheckBoardSquareWordList);
                    }

                    int finalCharIndex =
                            anchorBoardSquare.getCrossCheckWordIndex(
                                    playDirection);

                    CrossCheckWord crossCheckWord =
                            new CrossCheckWord(playDirection,
                                    firstPart + secondPart,
                                    firstLetterIndex,
                                    lastLetterIndex,
                                    rowColumnLetterIndex,
                                    crossCheckBoardSquareWordList,
                                    finalCharIndex);
                    anchorBoardSquare.addCrossCheckWord(playDirection,
                            crossCheckWord);

                    // FIXME
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println();
                        System.out.println("Cross check word:");
                        System.out.println(crossCheckWord);
                    }

                    // Iterator used only for cross checks (so no concurrent
                    // errors occur while removing invalid letters in the
                    // cross check)
                    Iterator<Character> crossCheckIterator =
                            crossCheckSet.iterator();
                    while (crossCheckIterator.hasNext()) {
                        char currentCrossCheckCharacter =
                                crossCheckIterator.next();

                        String potentialWord =
                                firstPart + currentCrossCheckCharacter +
                                        secondPart;

                        if (!wordSearchTrie.searchWord(potentialWord)) {
                            if (MainWordSolver.CROSS_CHECK_WORD) {
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
                            if (MainWordSolver.CROSS_CHECK_WORD) {
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
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println();
                        System.out.println("Final cross check list:");
                        System.out.println(crossCheckSet);
                    }
                } else {
                    if (MainWordSolver.CROSS_CHECK_WORD) {
                        System.out.println();
                        System.out.println("No cross check needed...");
                    }
                }

//                if (anchorBoardSquare.getCrossCheck(
//                        PlayDirection.HORIZONTAL)
//                        && anchorBoardSquare.getCrossCheck(
//                        PlayDirection.VERTICAL)) {
//                    //FIXME
//                    if (MainWordSolver.ANCHOR_DEBUG) {
//                        System.out.println();
//                        System.out.println("Merging the two sets...");
//                    }
//
//                    BoardSquare.mergeCrossCheckSets(anchorBoardSquare);
//
//                    //FIXME
//                    if (MainWordSolver.ANCHOR_DEBUG) {
//                        System.out.println();
//                        System.out.println("Horizontal cross check list:");
//                        System.out.println(anchorBoardSquare.getCrossCheckSet(
//                                PlayDirection.HORIZONTAL));
//                        System.out.println("Vertical cross check list:");
//                        System.out.println(anchorBoardSquare.getCrossCheckSet(
//                                PlayDirection.VERTICAL));
//                    }
//                }
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
            firstLetterIndex--;

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
            lastLetterIndex++;

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
                    if (checkNewInsideAnchorSquare(boardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(boardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_CENTER_HEAD,
                                AnchorType.SECONDARY_BODY);
                    }

//                    boardSquare.
//                            initiateCrossChecks(
//                                    wordPlayDirection);
                } else {
                    if (checkNewInsideAnchorSquare(boardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(boardSquare,
                                wordPlayDirection,
                                null,
                                AnchorType.SECONDARY_BODY);
                    }

//                    boardSquare.
//                            initiateCrossChecks(
//                                    wordPlayDirection);
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
                    if (checkNewOutsideAnchorSquare(
                            firstHeadBoardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(firstHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_SIDE_HEAD,
                                null);
                    }

                    if (firstHeadBoardSquare.getAnchor() != null) {
//                        firstHeadBoardSquare.
//                                initiateCrossChecks(
//                                        wordPlayDirection);

                        firstHeadBoardSquare.
                                initiateCrossChecks(
                                        wordPlayDirection,
                                        AnchorType
                                                .PRIMARY_SIDE_HEAD,
                                        null);
                    }
                } else {
                    if (checkNewOutsideAnchorSquare(
                            firstHeadBoardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(firstHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_SIDE_BODY,
                                null);
                    }

                    if (firstHeadBoardSquare.getAnchor() != null) {
//                        firstHeadBoardSquare.
//                                initiateCrossChecks(
//                                        wordPlayDirection);

                        firstHeadBoardSquare.
                                initiateCrossChecks(
                                        wordPlayDirection,
                                        AnchorType
                                                .PRIMARY_SIDE_BODY,
                                        null);
                    }
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
                    if (checkNewOutsideAnchorSquare(
                            secondHeadBoardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(secondHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_SIDE_HEAD,
                                null);
                    }

                    if (secondHeadBoardSquare.getAnchor() != null) {
//                        secondHeadBoardSquare.
//                                initiateCrossChecks(
//                                        wordPlayDirection);

                        secondHeadBoardSquare.
                                initiateCrossChecks(
                                        wordPlayDirection,
                                        AnchorType
                                                .PRIMARY_SIDE_HEAD,
                                        null);
                    }
                } else {
                    if (checkNewOutsideAnchorSquare(
                            secondHeadBoardSquare,
                            wordPlayDirection)) {
                        addAnchorSquare(secondHeadBoardSquare,
                                wordPlayDirection,
                                AnchorType.PRIMARY_SIDE_BODY,
                                null);
                    }

                    if (secondHeadBoardSquare.getAnchor() != null) {
//                        secondHeadBoardSquare.
//                                initiateCrossChecks(
//                                        wordPlayDirection);

                        secondHeadBoardSquare.
                                initiateCrossChecks(
                                        wordPlayDirection,
                                        AnchorType
                                                .PRIMARY_SIDE_BODY,
                                        null);
                    }
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

                //assert frontEnd != null;
                if (frontEnd != null) {
                    if (checkNewOutsideAnchorSquare(frontEnd,
                            PlayDirection.getOtherPlayDirection(
                                    wordPlayDirection))) {
                        addAnchorSquare(frontEnd,
                                wordPlayDirection,
                                null,
                                AnchorType.SECONDARY_END);
                    }

                    if (frontEnd.getAnchor() != null) {
//                        frontEnd.initiateCrossChecks(
//                                wordPlayDirection);

                        frontEnd.initiateCrossChecks(
                                wordPlayDirection,
                                null,
                                AnchorType.SECONDARY_END);
                    }
                }
            }

            if (checkSecondaryEndAnchor(lastBoardSquare,
                    wordPlayDirection.getCheckDirection())) {
                BoardSquare backEnd =
                        getBoardSquareInCheckDirection(
                                lastBoardSquare,
                                wordPlayDirection
                                        .getCheckDirection());

                //assert backEnd != null;
                if (backEnd != null) {
                    if (checkNewOutsideAnchorSquare(backEnd,
                            PlayDirection.getOtherPlayDirection(
                            wordPlayDirection))) {
                        addAnchorSquare(backEnd,
                                wordPlayDirection,
                                null,
                                AnchorType.SECONDARY_END);
                    }

                    if (backEnd.getAnchor() != null) {
//                        backEnd.initiateCrossChecks(
//                                wordPlayDirection);

                        backEnd.initiateCrossChecks(
                                wordPlayDirection,
                                null,
                                AnchorType.SECONDARY_END);
                    }
                }
            }
        }
    }

    private void initializeCrossChecks() {
        for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
            AnchorType primaryAnchorType =
                    anchorBoardSquare.getAnchor().getPrimaryAnchorType();
            AnchorType secondaryAnchorType =
                    anchorBoardSquare.getAnchor().getSecondaryAnchorType();

            if (primaryAnchorType.getInsideOutsideAnchor()
                    == InsideOutsideAnchor.OUTSIDE_ANCHOR) {

            }
        }
    }

    private void initializeInitialLeftLimits() {
        for (BoardSquare anchorBoardSquare : anchorBoardSquaresList) {
            AnchorType primaryAnchorType =
                    anchorBoardSquare.getAnchor().getPrimaryAnchorType();
            PlayDirection primaryPlayDirection =
                    anchorBoardSquare.getAnchor().getPrimaryDirection();

            if (MainWordSolver.LEFT_LIMIT) {
                System.out.println();
                anchorBoardSquare.printAnchorSquare();
            }

            if (primaryAnchorType != null
                    && primaryAnchorType != AnchorType.PRIMARY_SIDE_BODY) {
                // FIXME: something is wrong here...
                anchorBoardSquare.getAnchor().setLeftLimit(
                        findLeftLimitAnchorSquare(
                                anchorBoardSquare,
                        primaryPlayDirection),
                        primaryPlayDirection);

                if (MainWordSolver.LEFT_LIMIT) {
                    System.out.println("Primary limit: " +
                            anchorBoardSquare.getAnchor().getLeftLimit(
                                    primaryPlayDirection));
                }
            }

            AnchorType secondaryAnchorType =
                    anchorBoardSquare.getAnchor().getSecondaryAnchorType();
            PlayDirection secondaryPlayDirection =
                    anchorBoardSquare.getAnchor().getSecondaryDirection();
            if (secondaryAnchorType != null) {
                anchorBoardSquare.getAnchor().setLeftLimit(
                        findLeftLimitAnchorSquare(
                                anchorBoardSquare,
                                secondaryPlayDirection),
                        secondaryPlayDirection);

                if (MainWordSolver.LEFT_LIMIT) {
                    System.out.println("Secondary limit: " +
                            anchorBoardSquare.getAnchor().getLeftLimit(
                                    secondaryPlayDirection));
                }
            }
        }
    }

    private int findLeftLimitAnchorSquare(BoardSquare boardSquare,
                                          PlayDirection playDirection) {
        int leftLimit = 0;
        BoardSquare previousBoardSquare = getBoardSquareInCheckDirection(
                boardSquare,
                playDirection.getReverseCheckDirection());

        while (previousBoardSquare != null) {
//            if (previousBoardSquare.getAnchor() != null) {
//                return leftLimit; // so the left part doesn't touch
//                // another anchor square
//            }

            if (previousBoardSquare.getBoardSquareType()
                    == BoardSquareType.LETTER) {
                return leftLimit - 1; // so the left part doesn't touch
                // another anchor square -- can't directly check
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
     * Checks if the given board square was already added as an anchor square
     * (overlapping anchor squares should only be counted once since the
     * orientation could just be rotated to cover both play directions)
     *
     * @param boardSquare
     * @param playDirection
     * @return
     */
    private boolean checkNewOutsideAnchorSquare(BoardSquare boardSquare,
                                                PlayDirection playDirection) {
        //FIXME: || boardSquare.getAnchor() != null
        BoardSquare previousBoardSquare =
                getBoardSquareInCheckDirection(boardSquare,
                        playDirection.getReverseCheckDirection());
        if (boardSquare.getBoardSquareType() == BoardSquareType.LETTER
                || boardSquare.getAnchor() != null) {
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Check: " + false);
            }

            return false;
        }

        // FIXME: late addition so the square left or above it will be
        //  counted, and reduce redundant checking...
        if (previousBoardSquare != null
                && previousBoardSquare.getBoardSquareType()
                == BoardSquareType.LETTER) {
            return false;
        }

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println();
            System.out.println("Check: " + true);
        }

        return true;
    }

    private boolean checkNewInsideAnchorSquare(BoardSquare boardSquare,
                                               PlayDirection wordPlayDirection) {
        // FIXME:
        PlayDirection crossPlayDirection =
                PlayDirection.getOtherPlayDirection(wordPlayDirection);
        BoardSquare previousCrossDirectionBoardSquare =
                getBoardSquareInCheckDirection(boardSquare,
                        crossPlayDirection
                                .getReverseCheckDirection());
        if (previousCrossDirectionBoardSquare != null
                && previousCrossDirectionBoardSquare.getBoardSquareType()
                == BoardSquareType.LETTER
                || boardSquare.getAnchor() != null) {
            if (MainWordSolver.ANCHOR_DEBUG) {
                System.out.println();
                System.out.println("Check: " + false);
            }

            return false;
        }

        if (MainWordSolver.ANCHOR_DEBUG) {
            System.out.println();
            System.out.println("Check: " + true);
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

        // FIXME
        if (MainWordSolver.ANCHOR_DEBUG) {
            boardSquare.printAnchorSquare();
        }

        anchorBoardSquaresList.add(boardSquare);
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

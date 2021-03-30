package wordSolver;

import constants.InputChoice;
import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.TileBag;
import wordSearch.WordSearchTrie;

import java.util.Scanner;

public class MainWordSolver {
    // ALL DEBUG CHECKS (should move to its own class next time...)
    public static final boolean BOARD_SETUP = false;
    public static final boolean WORDS_IN_PLAY = true;
    public static final boolean ANCHOR_DEBUG = false;
    public static final boolean LEFT_LIMIT = false;
    public static final boolean CROSS_CHECK_WORD = false;
    public static final boolean ACTIVE_TILE = false;
    public static final boolean WORD_RECURSIVE = false;
    public static final boolean CHECK_CROSS_SET_WHEN_FINDING_WORD = false;
    public static final boolean PRINT_CROSS_CHECK_SETS = false;
    public static final boolean PRINT_ANCHOR_BOARD_SQUARES = true;
    public static final boolean PRINT_CHILDREN_NODES = false;
    public static final boolean FIND_LEGAL_WORD = false;
    public static final boolean SCORE_LEGAL_WORD = false;
    public static final boolean PRINT_CROSS_SCORE = true;
    public static final boolean PRINT_BASE_SCORE = true;
    public static final boolean PRINT_BINGO = true;
    public static final boolean PRINT_LEGAL_WORD = false;
    public static final boolean HIGHEST_SCORING_MOVE = true;

    public static final int TARGET_INDEX = 2;

    public static void main(String[] args) {
        try (Scanner scanner =
                     new Scanner(System.in)) {
            TileBag tileBag = new TileBag(InputChoice.FILE,
                    scanner);
            Board board = new Board(InputChoice.CONSOLE,
                    tileBag, scanner);
//        Board board = new Board("resources/testCaseBoard1.txt",
//                tileBag);

            Rack rack = new Rack(tileBag, scanner);
            // Player computerPlayer = new ComputerPlayer(board, rack);
            WordSearchTrie wordSearchTrie =
                    new WordSearchTrie("resources/sowpods.txt",
                            tileBag);

            WordSolver wordSolver = new WordSolver(board,
                    wordSearchTrie, rack);
            wordSolver.generateInitialHighestScoringMove();
        }
    }
}

package wordSolver;

import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.TileBag;
import wordSearch.WordSearchTrie;

import java.util.Scanner;

public class MainWordSolver {
    // ALL DEBUG CHECKS (should move to its own class next time...)
    public static final boolean BOARD_SETUP = false;
    public static final boolean CHECK_WORDS_IN_PLAY = false;
    public static final boolean PRINT_WORDS_IN_PLAY = false;
    public static final boolean ANCHOR_DEBUG = false;
    public static final boolean LEFT_LIMIT = false;
    public static final boolean CROSS_CHECK_WORD = false;
    public static final boolean ACTIVE_TILE = false;
    public static final boolean WORD_RECURSIVE = false;
    public static final boolean LEFT_PART = false;
    public static final boolean RIGHT_PART = false;
    public static final boolean CHECK_CROSS_SET_WHEN_FINDING_WORD = false;
    public static final boolean PRINT_CROSS_CHECK_SETS = false;
    public static final boolean PRINT_ANCHOR_BOARD_SQUARES = false;
    public static final boolean PRINT_CHILDREN_NODES = false;
    public static final boolean FIND_LEGAL_WORD = false;
    public static final boolean SCORE_LEGAL_WORD = false;
    public static final boolean PRINT_CROSS_SCORE = false;
    public static final boolean PRINT_BASE_SCORE = false;
    public static final boolean PRINT_BINGO = false;
    public static final boolean PRINT_LEGAL_WORD = false;
    public static final boolean HIGHEST_SCORING_MOVE = false;
    public static final boolean PRINT_SOLUTION_BOARD = false;

    public static final int TARGET_ROW_INDEX = 2;
    public static final int TARGET_COLUMN_INDEX = 8;

    public static void main(String[] args) {
        try (Scanner scanner =
                     new Scanner(System.in)) {
            TileBag tileBag = new TileBag(
                    "resources/scrabble_tiles.txt");
            WordSearchTrie wordSearchTrie =
                    new WordSearchTrie("resources/sowpods.txt",
                            tileBag);

            while (true) { // Infinite loop (will not stop until the user
                // manually quits the program)
//                Board board = new Board(InputChoice.CONSOLE,
//                        tileBag, scanner);
                Board board = new Board(tileBag, scanner);

                Rack rack = new Rack(tileBag, scanner);

                WordSolver wordSolver = new WordSolver(board,
                        wordSearchTrie, rack);
                wordSolver.generateInitialHighestScoringMove();

                // Used to refresh the tileBag since tiles are removed from
                // tileBag when they're placed on the board (will run out
                // with multiple inputs)
                tileBag = new TileBag(
                        "resources/scrabble_tiles.txt");
            }
        }
    }
}

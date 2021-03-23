package wordSolver;

import constants.InputChoice;
import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.TileBag;
import players.ComputerPlayer;
import players.Player;
import wordSearch.WordSearchTrie;

import java.util.Scanner;

public class MainWordSolver {
    public static boolean DEBUG = true;

    public static void main(String[] args) {
        TileBag tileBag = new TileBag(InputChoice.FILE);
        Board board = new Board(InputChoice.CONSOLE, tileBag);
//        Board board = new Board("resources/testCaseBoard1.txt",
//                tileBag);

        // FIXME: only one scanner...

        if (DEBUG) {
            try (Scanner scanner =
                         new Scanner(System.in)) {
                String something = scanner.nextLine();
                System.out.println("Something: " + something);
            }
        }

        Rack rack = new Rack(tileBag);
        // Player computerPlayer = new ComputerPlayer(board, rack);
        WordSearchTrie wordSearchTrie =
                new WordSearchTrie("resources/sowpods.txt");

        WordSolver wordSolver = new WordSolver(board,
                wordSearchTrie, rack);
        wordSolver.generateInitialHighestScoringMove();
    }
}

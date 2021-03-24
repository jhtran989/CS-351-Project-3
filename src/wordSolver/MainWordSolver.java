package wordSolver;

import constants.InputChoice;
import gamePieces.Board;
import gamePieces.Rack;
import gamePieces.TileBag;
import wordSearch.WordSearchTrie;

import java.util.Scanner;

public class MainWordSolver {
    public static boolean DEBUG = true;

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
                    new WordSearchTrie("resources/sowpods.txt", tileBag);

            WordSolver wordSolver = new WordSolver(board,
                    wordSearchTrie, rack);
            wordSolver.generateInitialHighestScoringMove();
        }
    }
}

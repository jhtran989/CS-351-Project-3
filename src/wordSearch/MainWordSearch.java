package wordSearch;

import constants.InputChoice;
import gamePieces.TileBag;
import gamePieces.WordInPlay;

import java.util.*;

public class MainWordSearch {
    public static final boolean DEBUG = false;

    public static void main(String[] args) {
        String dictionaryFilePath = "resources/sowpods.txt";
        TileBag tileBag = new TileBag(InputChoice.FILE, null);
        WordSearchTrie wordSearchTrie = new WordSearchTrie(
                dictionaryFilePath, tileBag);

        if (DEBUG) {
            wordSearchTrie.printRoot();
        }

        List<String> wordList = new ArrayList<>();
        wordList.add("kljasdfkljksdfa");
        wordList.add("apple");
        wordList.add("banana");
        wordList.add("bannannannna");
        wordList.add("b");

        for (String s : wordList) {
            System.out.println(s + " " + wordSearchTrie.searchWord(s));
        }

//        Set<Character> characterSet = new TreeSet<>();
//        Set<Character> newCharacterSet = new TreeSet<>();
//
//        for (int i = 'a'; i < 'z'; i++) {
//            characterSet.add((char) i);
//        }
//
//        newCharacterSet.addAll(characterSet);
//
//        characterSet.remove('c');
//        characterSet.remove('a');
//
//        System.out.println(characterSet);
//
//        newCharacterSet.remove('b');
//
//        System.out.println(newCharacterSet);
    }
}

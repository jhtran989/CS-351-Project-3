package wordSearch;

import java.util.ArrayList;
import java.util.List;

public class MainWordSearch {
    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        String dictionaryFilePath = "resources/sowpods.txt";
        WordSearchTrie wordSearchTrie = new WordSearchTrie(
                dictionaryFilePath);

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
    }
}

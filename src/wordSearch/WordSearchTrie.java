package wordSearch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

public class WordSearchTrie {
    private CharacterNode root;
    private CharacterNode currentNode;

    public WordSearchTrie() {
        initializeRoot();

        String dictionaryFilePath = "resources/sowpods.txt";
        setupWordSearchTree(dictionaryFilePath);
        if (MainWordSearch.DEBUG) {
            printTree();
        }
    }

    private void initializeRoot() {
        root = new CharacterNode();
    }

    private void addWord(String word, CharacterNode currentNode) {
        if (word.length() > 0) {
            CharacterNode childNode = new CharacterNode(word.charAt(0));
            if (MainWordSearch.DEBUG) {
                System.out.println("current char: " + word.charAt(0));
            }
            childNode = currentNode.addChild(childNode);
            addWord(word.substring(1), childNode);
        } else {
            currentNode.activateTerminalNode();
        }
    }

    // Wrapper method for the one above (so root stays internal to the class)
    private void addWord(String word) {
        addWord(word, root);
    }

    private void setupWordSearchTree(String filePath) {
        System.out.println("Setting up the word search tree...");

        try (Scanner scanner = new Scanner(
                new FileReader(filePath))) {
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (MainWordSearch.DEBUG) {
                    System.out.println("word: " + word);
                }
                addWord(word);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }

        System.out.println("Finished...");
    }

//    // Mostly for debugging purposes (couldn't possibly print the entire tree
//    // out for over 9,000 words...)
//    private void printTree(CharacterNode node) {
//        if (node.hasChildren()) {
//            node.printChildren();
//            for (CharacterNode childNode: node.getChildren()) {
//                printTree(childNode);
//            }
//        }
//    }

    // Mostly for debugging purposes (couldn't possibly print the entire tree
    // out for over 9,000 words...)
    private void printTree(CharacterNode node) {
        if (node.hasChildren()) {
            node.printChildren();
            for (Map.Entry<Character, CharacterNode>
                    characterCharacterNodeEntry :
                    node.getChildrenMap().entrySet()) {
                CharacterNode childNode =
                        characterCharacterNodeEntry.getValue();
                //childNode.printChildren();
                printTree(childNode);
            }
        }
    }

    private void printTree() {
        if (MainWordSearch.DEBUG) {
            System.out.println("Tree: ");
        }
        printTree(root);
    }
}

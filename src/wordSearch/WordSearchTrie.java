package wordSearch;

import gamePieces.TileBag;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class WordSearchTrie {
    private CharacterNode root;
    private TileBag tileBag;
    private Set<Character> fullLetterSet;

    public WordSearchTrie(String dictionaryFilePath, TileBag tileBag) {
        this.tileBag = tileBag;
        fullLetterSet = tileBag.getFullLetterSet();

        initializeRoot();

        setupWordSearchTree(dictionaryFilePath);

//        if (MainWordSearch.DEBUG) {
//            printTree();
//        }
    }

    /**
     * The root node is "empty" - the formation of character nodes start
     * after the root node
     */
    private void initializeRoot() {
        root = new CharacterNode();
    }

    public CharacterNode getRoot() {
        return root;
    }

    public CharacterNode getYoungestChild(String leftPart) {
        return getYoungestChild(leftPart, root);
    }

    /**
     * Precondition: leftPart is a valid prefix in the given dictionary...
     * except that there's nontrivial checks for the left part, so we return
     * null early if at any point in the leftPart, there's no node that can
     * be extended
     *
     * @param leftPart
     * @param currentNode
     * @return
     */
    private CharacterNode getYoungestChild(String leftPart,
                                          CharacterNode currentNode) {
        if (leftPart.length() > 0) {
            char currentChar = Character.toLowerCase(leftPart.charAt(0));

            if (MainWordSearch.DEBUG) {
                if (currentNode == root) {
                    System.out.print("Root: ");
                }

                System.out.println(currentNode);
            }

            CharacterNode searchNode;

//            // FIXME: add something for a blank tile...
//            if (currentChar == '*') {
//                System.out.println("Asterisk found...");
//
//                // Will search using every character given in the tile bag
//                // (set of available tiles -- for the usual game, just the 26
//                // letters of the alphabet) and return the earliest found
//                // word in the given dictionary
//                for (Character specialCharacter : fullLetterSet) {
//                    if (MainWordSearch.DEBUG) {
//                        System.out.println("Current char: " + specialCharacter +
//                                " (level " + currentNode.getLevel() + ")");
//                    }
//
//                    searchNode = currentNode.getChildNode(
//                            specialCharacter);
//
//                    if (searchNode != null) {
//                        boolean foundSpecialWord = searchWord(
//                                word.substring(1),
//                                searchNode);
//
//                        if (foundSpecialWord) {
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }

            if (currentNode != null) {
                searchNode =
                        currentNode.getChildNode(currentChar);

                if (searchNode != null) {
                    return getYoungestChild(leftPart.substring(1),
                            searchNode);
                } else {
                    return null; // shouldn't happen if the precondition was met...
                }
            } else {
                return null;
            }
        } else {
            return currentNode;
        }
    }

    private boolean searchLeftPart(String leftPart, CharacterNode currentNode) {
        if (leftPart.length() > 0) {
            char currentChar = Character.toLowerCase(leftPart.charAt(0));

            if (MainWordSearch.DEBUG) {
                if (currentNode == root) {
                    System.out.print("Root: ");
                }

                System.out.println(currentNode);
            }

            CharacterNode searchNode;

//            // FIXME: add something for a blank tile...
//            if (currentChar == '*') {
//                System.out.println("Asterisk found...");
//
//                // Will search using every character given in the tile bag
//                // (set of available tiles -- for the usual game, just the 26
//                // letters of the alphabet) and return the earliest found
//                // word in the given dictionary
//                for (Character specialCharacter : fullLetterSet) {
//                    if (MainWordSearch.DEBUG) {
//                        System.out.println("Current char: " + specialCharacter +
//                                " (level " + currentNode.getLevel() + ")");
//                    }
//
//                    searchNode = currentNode.getChildNode(
//                            specialCharacter);
//
//                    if (searchNode != null) {
//                        boolean foundSpecialWord = searchWord(
//                                word.substring(1),
//                                searchNode);
//
//                        if (foundSpecialWord) {
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }

            searchNode =
                    currentNode.getChildNode(currentChar);

            if (searchNode != null) {
                return searchLeftPart(leftPart.substring(1),
                        searchNode);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    public boolean searchLeftPart(String leftPart) {
        return searchWord(leftPart, root);
    }

    // assuming the characters of the word can be lowercase or uppercase (and
    // any dictionary given will have lowercase words) where uppercase is
    // from the blank tiles...
    private boolean searchWord(String word, CharacterNode currentNode) {
        if (word.length() > 0) {
            char currentChar = Character.toLowerCase(word.charAt(0));

            if (MainWordSearch.DEBUG) {
                if (currentNode == root) {
                    System.out.print("Root: ");
                }

                System.out.println(currentNode);
            }

            CharacterNode searchNode;

//            // FIXME: add something for a blank tile...
//            if (currentChar == '*') {
//                System.out.println("Asterisk found...");
//
//                // Will search using every character given in the tile bag
//                // (set of available tiles -- for the usual game, just the 26
//                // letters of the alphabet) and return the earliest found
//                // word in the given dictionary
//                for (Character specialCharacter : fullLetterSet) {
//                    if (MainWordSearch.DEBUG) {
//                        System.out.println("Current char: " + specialCharacter +
//                                " (level " + currentNode.getLevel() + ")");
//                    }
//
//                    searchNode = currentNode.getChildNode(
//                            specialCharacter);
//
//                    if (searchNode != null) {
//                        boolean foundSpecialWord = searchWord(
//                                word.substring(1),
//                                searchNode);
//
//                        if (foundSpecialWord) {
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }

            searchNode =
                    currentNode.getChildNode(currentChar);

            if (searchNode != null) {
                return searchWord(word.substring(1),
                        searchNode);
            } else {
                return false;
            }
        } else {
            return currentNode.isTerminalNode();
        }
    }

    public boolean searchWord(String word) {
        return searchWord(word, root);
    }

    private void addWord(String word, CharacterNode currentNode, int level) {
        if (word.length() > 0) {
            level++;
            CharacterNode childNode = new CharacterNode(word.charAt(0));

//            if (MainWordSearch.DEBUG) {
//                System.out.println(currentNode);
//            }

            childNode = currentNode.addChild(childNode, level);
            addWord(word.substring(1), childNode,
                    level);
        } else {
            currentNode.activateTerminalNode();
        }
    }

    // Wrapper method for the one above (so root stays internal to the class)
    private void addWord(String word) {
        int level = 0;
        addWord(word, root, level);
    }

    private void setupWordSearchTree(String filePath) {
        if (MainWordSearch.DEBUG) {
            System.out.println("Setting up the word search trie...");
        }

        try (Scanner scanner = new Scanner(
                new FileReader(filePath))) {
            while (scanner.hasNext()) {
                String word = scanner.next();

//                if (MainWordSearch.DEBUG) {
//                    System.out.println("word: " + word);
//                }

                addWord(word);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }

        if (MainWordSearch.DEBUG) {
            System.out.println("Finished setting up word trie...");
        }
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

    public void printRoot() {
        if (MainWordSearch.DEBUG) {
            root.printChildren();
        }
    }

    private void printTree() {
        if (MainWordSearch.DEBUG) {
            System.out.println("Tree: ");
        }
        printTree(root);
    }
}

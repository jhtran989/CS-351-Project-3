package wordSearch;

import java.util.*;

public class CharacterNode {
    private char character;
    private Map<Character, CharacterNode> childrenMap;
    private boolean terminalNode;
    private int level;

    public CharacterNode(char character) {
        this.character = character;
        terminalNode = false;
        childrenMap = new TreeMap<>();
    }

    // Only used for the construction of the root node in the tree
    public CharacterNode() {
        character = ' ';
        terminalNode = false;
        childrenMap = new TreeMap<>();
        level = 0;
    }

    public void activateTerminalNode() {
        terminalNode = true;
    }

    public boolean isTerminalNode() {
        return terminalNode;
    }

    public int getLevel() {
        return level;
    }

    public CharacterNode getChildNode(char currentChar) {
        return childrenMap.get(currentChar);
    }

    public CharacterNode addChild(CharacterNode childNode, int level) {
        char childCharacter = childNode.getCharacter();
        CharacterNode currentChildNode = childrenMap.get(childCharacter);

        if (currentChildNode == null) {
//            if (MainWordSearch.DEBUG) {
//                System.out.println("Creating..." + childCharacter);
//            }
            currentChildNode = childNode;
            currentChildNode.level = level;

            childrenMap.put(childCharacter, currentChildNode);
        }

        //childrenMap.put(childCharacter, currentChildNode);

        return currentChildNode;
    }

    public char getCharacter() {
        return character;
    }

    public boolean hasChildren() {
        return childrenMap != null;
    }

    public void printChildren() {
        System.out.println(childrenMap.values());
    }

    public Map<Character, CharacterNode> getChildrenMap() {
        return childrenMap;
    }

    @Override
    public String toString() {
        return "" + character + " (level " + level + ")";
    }
}

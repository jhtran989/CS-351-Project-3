package wordSearch;

import java.util.*;

public class CharacterNode {
    private char character;
    private Map<Character, CharacterNode> childrenMap;
    private boolean terminalNode;

    public CharacterNode(char character) {
        this.character = character;
        terminalNode = false;
    }

    // Only used for the construction of the root node in the tree
    public CharacterNode() {
    }

    public void activateTerminalNode() {
        terminalNode = true;
    }

    public CharacterNode addChild(CharacterNode childNode) {
        if (childrenMap == null) {
            childrenMap = new TreeMap<>();
        }

        char childCharacter = childNode.getCharacter();
        CharacterNode currentChildNode = childrenMap.get(childCharacter);

        if (currentChildNode == null) {
            if (MainWordSearch.DEBUG) {
                System.out.println("Creating..." + childCharacter);
            }
            currentChildNode = childNode;
        }

        childrenMap.put(childCharacter, currentChildNode);

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
        return "" + character;
    }
}

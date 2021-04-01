package comparators;

import wordSearch.CharacterNode;

import java.util.Comparator;

/**
 * Comparator for the CharacterNode class
 */
public class CharacterNodeComparator implements Comparator<CharacterNode> {
    @Override
    public int compare(CharacterNode characterNode1,
                       CharacterNode characterNode2) {
        return characterNode1.getCharacter() - characterNode2.getCharacter();
    }
}

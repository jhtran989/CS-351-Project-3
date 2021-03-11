package wordSearch;

import java.util.Comparator;

public class CharacterNodeComparator implements Comparator<CharacterNode> {
    @Override
    public int compare(CharacterNode characterNode1,
                       CharacterNode characterNode2) {
        return characterNode1.getCharacter() - characterNode2.getCharacter();
    }
}

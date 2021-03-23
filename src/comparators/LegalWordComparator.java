package comparators;

import gamePieces.WordInPlay;

import java.util.Comparator;
import java.util.Map;

public class LegalWordComparator implements
        Comparator<Map.Entry<WordInPlay, Integer>> {

    @Override
    public int compare(Map.Entry<WordInPlay, Integer> o1,
                       Map.Entry<WordInPlay, Integer> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}

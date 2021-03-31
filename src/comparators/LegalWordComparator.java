package comparators;

import gamePieces.WordInPlay;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class LegalWordComparator implements
        Comparator<Map.Entry<WordInPlay, Integer>> {

    @Override
    public int compare(Map.Entry<WordInPlay, Integer> o1,
                       Map.Entry<WordInPlay, Integer> o2) {
        int initialCompare = o1.getValue().compareTo(o2.getValue());

        if (initialCompare != 0) {
            return initialCompare;
        }

        String word1 = o1.getKey().getWord().toLowerCase(Locale.ROOT);
        String word2 = o2.getKey().getWord().toLowerCase(Locale.ROOT);

        int nextCompare =
                word1.compareTo(word2);
        return nextCompare;
    }
}

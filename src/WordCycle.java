import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

final class WordCycle {
    private final List<String> words;
    private final Random random;
    private List<String> remaining = new ArrayList<>();
    private String previousWord;

    WordCycle(String[] source, Random random) {
        Set<String> seen = new HashSet<>();
        this.words = new ArrayList<>();
        for (String word : source) {
            if (seen.add(word.toLowerCase(Locale.ROOT))) words.add(word);
        }
        if (words.isEmpty()) throw new IllegalArgumentException("Word list cannot be empty");
        this.random = random;
    }

    String next() {
        if (remaining.isEmpty()) {
            remaining = new ArrayList<>(words);
            Collections.shuffle(remaining, random);
            if (remaining.size() > 1 && remaining.get(0).equals(previousWord)) {
                Collections.swap(remaining, 0, 1);
            }
        }
        previousWord = remaining.remove(0);
        return previousWord;
    }
}

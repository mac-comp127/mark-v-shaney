package mvs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * A Markov chain text generator.
 */
@SuppressWarnings("WeakerAccess")
public class MarkVShaney {
    private final int contextSize;
    private final Map<List<String>, WordChoice> chain = new HashMap<>();

    /**
     * Starts the generator with an empty Markov chain.
     */
    public MarkVShaney(int contextSize) {
        this.contextSize = contextSize;
    }

    /**
     * The number of previous words the chain will use for context.
     */
    public int getContextSize() {
        return contextSize;
    }

    /**
     * Registers nextWord as a possible choice for the given context.
     */
    public void addChoice(List<String> context, String nextWord) {
        WordChoice choices = chain.get(context);
        if (choices == null) {
            choices = new WordChoice();
            chain.put(List.copyOf(context), choices);
        }
        choices.addChoice(nextWord);
    }

    /**
     * Chooses a next word at random for the given preceding words. Returns null if the Markov chain
     * contains no options for the given context.
     */
    public String chooseNextWord(List<String> context) {
        WordChoice choices = chain.get(context);
        if (choices == null) {
            return null;
        } else {
            return choices.getRandomChoice();
        }
    }

    /**
     * Reads all words from the given text and adds them to the Markov chain.
     */
    public void readText(String text) {
        readText(
            new ByteArrayInputStream(
                text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Reads all words from the given text and adds them to the Markov chain.
     */
    public void readText(InputStream text) {
        ChainWalker walker = new ChainWalker(this);
        new Scanner(text, StandardCharsets.UTF_8).tokens()
            .forEach(walker::addNext);
    }

    /**
     * Randomly generates text using the Markov chain.
     */
    public Stream<String> generate() {
        ChainWalker walker = new ChainWalker(this);
        return Stream.generate(walker::chooseNext)
            .takeWhile(Objects::nonNull);
    }

    @Override
    public String toString() {
        return "MarkVShaney{"
            + "contextSize=" + contextSize
            + ", chain=" + chain
            + '}';
    }

    public static void main(String[] args) throws IOException {
        MarkVShaney mvs = new MarkVShaney(2);
        for (String bookName : List.of(
            "metamorphasis",
            "moby-dick",
            "prince-and-pauper",
            "sense-and-sensibility"
        )) {
            System.out.println("Reading " + bookName);
            mvs.readText(MarkVShaney.class.getResourceAsStream("/" + bookName + ".txt"));
        }
        mvs.generate()
            .map(word -> word + " ")
            .limit(1000000)
            .forEach(System.out::print);
        System.out.println();
    }
}

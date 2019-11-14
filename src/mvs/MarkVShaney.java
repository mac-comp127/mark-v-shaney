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
    // TODO: Declare an instance variable chain that is:
    //       - a Map
    //       - with List<String> keys
    //       - and WordChoice values
    //       - intiailized to a new HashMap.

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
        // TODO: Find the existing WordChoice for the given context.
        //       If there is not one already:
        //       - create it and
        //       - add it to the chain, using List.copyOf() to make
        //         an unmodifiable defensive copy of the context.
        //       Now add nextWord to the WordChoice object you found/created.
    }

    /**
     * Chooses a next word at random for the given preceding words. Returns null if the Markov chain
     * contains no options for the given context.
     */
    public String chooseNextWord(List<String> context) {
        // TODO: Look up the WordChoice for the given context, and return a random choice from that
        //       object (check the methods of WordChoice!), or return null if there was no matching
        //       context.
        throw new UnsupportedOperationException("not implemented yet");
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
        // TODO: Create a ChainWalker attached to this MarkVShaney.
        // TODO: Create a scanner using:  new Scanner(text, StandardCharsets.UTF_8)
        //       ...and call its tokens() method to get a stream of words
        //       ...and use the forEach() method of stream to call walker.addNext() on each word
    }

    /**
     * Randomly generates text using the Markov chain.
     */
    public Stream<String> generate() {
        // TODO: Create a ChainWalker attached to this MarkVShaney.
        // TODO: Use the Stream.generate() method to stream the results of calling
        //       walker.chooseNext() over and over, then use takeWhile() to stop
        //       when it returns null.
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public String toString() {
        return "MarkVShaney{"
            + "contextSize=" + contextSize
//            + ", chain=" + chain  //TODO: uncomment when you've create the chain instance variable
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

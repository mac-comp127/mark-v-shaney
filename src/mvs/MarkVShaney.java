package mvs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * A Markov chain text generator. Holds a transition chain whose entries each say, “For these n
 * words, the following words might appear next in the text.” You can populate the transition
 * table with one of more calls to readText(), then use a call to generateText() to randomly
 * create new text by choosing random options from the transition chain.
 */
@SuppressWarnings({"WeakerAccess","resource"})
public class MarkVShaney {
    private final int contextSize;
    private final boolean includeWhitespace;
    // TODO: Declare an instance variable “transitions” that is:
    //       - a Map
    //       - with List<String> keys
    //       - and WordChoice values
    //       - intiailized to a new HashMap.

    /**
     * Starts the generator with an empty Markov chain.
     *
     * @param contextSize
     *      The number of preceding words to keep as context when looking up the next word.
     * @param includeWhitespace
     *      If true, include surrounding whitespace in the words so that the Markov chain generates
     *      paragraph breaks. If false, the chain strips all whitespace and includes only bare words.
     */
    public MarkVShaney(int contextSize, boolean includeWhitespace) {
        this.contextSize = contextSize;
        this.includeWhitespace = includeWhitespace;
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
        //       - add it to the transitions, using List.copyOf() to make
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
     * Populates the transition table with the standard body of texts in the res/ directory.
     */
    public void readDefaultCorpus() {
        for (File bookFile : new File("res").listFiles((dir, name) -> name.endsWith(".txt"))) {
            System.out.println("Reading " + bookFile);
            try {
                readText(new FileInputStream(bookFile), true);
            } catch (FileNotFoundException e) {
                System.err.print("Cannot read " + bookFile + ": ");
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads all words from the given text and adds them to the Markov chain.
     */
    public void readText(String text) {
        readText(
            new ByteArrayInputStream(
                text.getBytes(StandardCharsets.UTF_8)),
            false);
    }

    /**
     * Reads all words from the given text and adds them to the Markov chain.
     */
    public void readText(InputStream text, boolean fullBookFormat) {
        // TODO: Create a ChainWalker attached to this MarkVShaney.

        Stream<String> stream = new Scanner(text, StandardCharsets.UTF_8)
            // Split into words
            .useDelimiter(includeWhitespace ? "(?<=\\s)(?!\\s)" : "\\s+").tokens();

        if (fullBookFormat) {
            stream = stream
                // Skip header & footer
                .dropWhile(word -> !word.startsWith("––––––START––––––")).skip(1)
                .takeWhile(word -> !word.startsWith("––––––END––––––"))

                // Convert line endings and remove hard word wraps
                .map(word -> word.replaceAll("\\r(\\n)?", "\n"))
                .map(word -> word.replaceFirst(" *\\n *$", " "));
        }

        // TODO: For each string in stream, call walker.addNext()
    }

    /**
     * Randomly generates text using the Markov chain.
     */
    public Stream<String> generateText() {
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
//            + ", transitions=" + transitions  //TODO: uncomment when you've create the transitions instance variable
            + '}';
    }

    public static void main(String[] args) throws IOException {
        MarkVShaney mvs = new MarkVShaney(2, true);
        mvs.readDefaultCorpus();
        mvs.generateText()
            .limit(1000)
            .forEach(System.out::print);
        System.out.println();
    }
}

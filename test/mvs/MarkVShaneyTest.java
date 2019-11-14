package mvs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class MarkVShaneyTest {
    private MarkVShaney mvs = new MarkVShaney(2);

    @Test
    void handlesUnrecognizedContext() {
        mvs.readText("Hello!");
        assertNull(mvs.chooseNextWord(List.of("Goodbye!")));
    }

    @Test
    void handleInitialContext() {
        mvs.readText("Hello there!");
        assertEquals("Hello", mvs.chooseNextWord(List.of()));
        assertEquals("there!", mvs.chooseNextWord(List.of("Hello")));
    }

    @Test
    void handlesMultiwordContext() {
        mvs.readText("one two three");
        assertEquals("three", mvs.chooseNextWord(List.of("one", "two")));
    }

    @Test
    void shiftsWordsOutOfContext() {
        mvs.readText("one two three four");
        assertEquals("four", mvs.chooseNextWord(List.of("two", "three")));
        assertNull(mvs.chooseNextWord(List.of("one", "two", "three")));
    }

    @Test
    void respectsContextSize() {
        mvs = new MarkVShaney(3);
        mvs.readText("one two three four");
        assertNull(mvs.chooseNextWord(List.of("two", "three")));
        assertEquals("four", mvs.chooseNextWord(List.of("one", "two", "three")));
    }

    @Test
    void handlesMultipleChoices() {
        mvs.readText("Jack be nimble, Jack be quick");
        Set<String> results = IntStream.range(0, 100)
            .mapToObj(i -> mvs.chooseNextWord(List.of("Jack", "be")))
            .collect(Collectors.toSet());
        assertEquals(Set.of("nimble,", "quick"), results);
    }

    @Test
    void generatesText() {
        mvs.readText("Coding up a storm!");
        assertEquals(
            List.of("Coding", "up", "a", "storm!"),
            mvs.generate().collect(toList()));
    }
}

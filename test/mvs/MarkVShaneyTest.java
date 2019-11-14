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

//    @Test
//    void handlesInitialContext() {
//        mvs.readText("Hello there!");
//        assertEquals("Hello", mvs.chooseNextWord(List.of()));
//        assertEquals("there!", mvs.chooseNextWord(List.of("Hello")));
//    }

}

import day7.Day7;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Day7Test {
    @Test
    void testPart1() throws IOException {
        assertEquals(Day7.part1("src/main/resources/tests/7.1.txt"), 43210);
        assertEquals(Day7.part1("src/main/resources/tests/7.2.txt"), 54321);
        assertEquals(Day7.part1("src/main/resources/tests/7.3.txt"), 65210);
    }

    @Test
    void testPart2() throws IOException {
        assertEquals(Day7.part2("src/main/resources/tests/7.4.txt"), 139629729);
        assertEquals(Day7.part2("src/main/resources/tests/7.5.txt"), 18216);
    }
}

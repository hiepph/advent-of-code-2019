import day8.Day8;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day8Test {
    @Test
    void testPart1() throws IOException {
        assertEquals(Day8.part1("src/main/resources/tests/8.1.txt", 3, 2), 1);
    }
}
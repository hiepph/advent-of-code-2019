import day9.Day9;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day9Test {
    @Test
    void testPart1() throws IOException {
        // make sure it also works as an Intcode computer
        assertEquals(5821753, Day9.part1("src/main/resources/inputs/5.txt", 1));
        assertEquals(11956381, Day9.part1("src/main/resources/inputs/5.txt", 5));

        // opcode 9
        assertEquals(99, Day9.part1("src/main/resources/tests/9.1.txt", 1));

        // able to return large numbers
        assertEquals(16, Long.toString(Day9.part1("src/main/resources/tests/9.2.txt", 1)).length());
        assertEquals(1125899906842624L, Day9.part1("src/main/resources/tests/9.3.txt", 1));
    }
}

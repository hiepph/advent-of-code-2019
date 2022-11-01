import org.junit.jupiter.api.Test;

import static day14.Day14.part1;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day14Test {
    @Test
    void testPart1() {
//        assertEquals(31, part1("src/main/resources/tests/14.1.txt"));
//        assertEquals(165, part1("src/main/resources/tests/14.2.txt"));
        assertEquals(13312, part1("src/main/resources/tests/14.3.txt"));
        assertEquals(180697, part1("src/main/resources/tests/14.4.txt"));
    }
}

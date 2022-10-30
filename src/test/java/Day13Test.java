import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Day13Test {
    @Test
    void testPart1() throws IOException {
        assertEquals(255, day13.Day13.part1("src/main/resources/inputs/13.txt"));
    }
}

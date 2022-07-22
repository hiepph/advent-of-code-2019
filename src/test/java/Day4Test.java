import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day4Test {
    Day4 sol = new Day4();

    @Test
    void testPart1() {
        assertTrue(Day4.isValid(111111));
        assertFalse(Day4.isValid(223450));
        assertFalse(Day4.isValid(123789));
    }

    @Test
    void testPart2() {
        assertTrue(Day4.isValidPart2(112233));
        assertFalse(Day4.isValidPart2(123444));
        assertTrue(Day4.isValidPart2(111122));
    }
}

import day10.Day10;
import day10.Tuple;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10Test {
    private Tuple<Tuple<Integer, Integer>, Integer> result;

    @Test
    void testPart1() throws IOException {
        assertEquals(new Tuple<>(new Tuple<>(3, 4), 8), Day10.part1("src/main/resources/tests/10.1.txt"));
        assertEquals(new Tuple<>(new Tuple<>(5, 8), 33), Day10.part1("src/main/resources/tests/10.2.txt"));
        assertEquals(new Tuple<>(new Tuple<>(1, 2), 35), Day10.part1("src/main/resources/tests/10.3.txt"));
        assertEquals(new Tuple<>(new Tuple<>(11, 13), 210), Day10.part1("src/main/resources/tests/10.4.txt"));
    }
}
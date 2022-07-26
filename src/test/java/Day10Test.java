import day10.Day10;
import day10.Tuple;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10Test {
    @Test
    void testPart1() throws IOException {
        assertEquals(new Tuple<>(new Tuple<>(3, 4), 8), Day10.part1("src/main/resources/tests/10.1.txt"));
        assertEquals(new Tuple<>(new Tuple<>(5, 8), 33), Day10.part1("src/main/resources/tests/10.2.txt"));
        assertEquals(new Tuple<>(new Tuple<>(1, 2), 35), Day10.part1("src/main/resources/tests/10.3.txt"));
        assertEquals(new Tuple<>(new Tuple<>(11, 13), 210), Day10.part1("src/main/resources/tests/10.4.txt"));

        // result
        assertEquals(286, Day10.part1("src/main/resources/inputs/10.txt").second());
    }

    @Test
    void testPart2() throws IOException {
        Tuple<Integer, Integer> location;

        location = Day10.part1("src/main/resources/tests/10.4.txt").first();
        List<Tuple<Integer, Integer>> asteroids = Day10.part2("src/main/resources/tests/10.4.txt", location);
        assertEquals(new Tuple<>(11, 12), asteroids.get(0));
        assertEquals(new Tuple<>(12, 1), asteroids.get(1));
        assertEquals(new Tuple<>(12, 2), asteroids.get(2));
        assertEquals(new Tuple<>(12, 8), asteroids.get(9));
        assertEquals(new Tuple<>(16, 0), asteroids.get(19));
        assertEquals(new Tuple<>(16, 9), asteroids.get(49));
        assertEquals(new Tuple<>(10, 16), asteroids.get(99));
        assertEquals(new Tuple<>(9, 6), asteroids.get(198));
        assertEquals(new Tuple<>(8, 2), asteroids.get(199));
        assertEquals(new Tuple<>(10, 9), asteroids.get(200));
        assertEquals(new Tuple<>(11, 1), asteroids.get(298));
    }
}
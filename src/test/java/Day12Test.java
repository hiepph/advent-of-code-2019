import day12.Experiment;
import day12.Moon;
import day12.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12Test {
    @Test
    void testPart1() {
        Experiment experiment = new Experiment("src/main/resources/tests/12.1.txt");
        assertArrayEquals(new Moon[]{
                new Moon(new Tuple(-1, 0, 2)),
                new Moon(new Tuple(2, -10, -7)),
                new Moon(new Tuple(4, -8, 8)),
                new Moon(new Tuple(3, 5, -1)),
        }, experiment.getMoons());

         experiment.simulate();
         // after 1 step
        assertArrayEquals(new Moon[]{
                new Moon(new Tuple(2, -1, 1), new Tuple(3, -1, -1)),
                new Moon(new Tuple(3, -7, -4), new Tuple(1, 3, 3)),
                new Moon(new Tuple(1, -7, 5), new Tuple(-3, 1, -3)),
                new Moon(new Tuple(2, 2, 0), new Tuple(-1, -3, 1)),
        }, experiment.getMoons());
    }
}

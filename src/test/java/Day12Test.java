import day12.Experiment;
import day12.Moon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day12Test {
    @Test
    void testPart1() {
        Experiment experiment = new Experiment("src/main/resources/tests/12.1.txt");
        assertArrayEquals(new Moon[]{
                new Moon(new int[]{-1, 0, 2}),
                new Moon(new int[]{2, -10, -7}),
                new Moon(new int[]{4, -8, 8}),
                new Moon(new int[]{3, 5, -1}),
        }, experiment.getMoons());

         experiment.simulate();
        // after 1 step
        assertArrayEquals(new Moon[]{
                new Moon(new int[]{2, -1, 1}, new int[]{3, -1, -1}),
                new Moon(new int[]{3, -7, -4}, new int[]{1, 3, 3}),
                new Moon(new int[]{1, -7, 5}, new int[]{-3, 1, -3}),
                new Moon(new int[]{2, 2, 0}, new int[]{-1, -3, 1}),
        }, experiment.getMoons());

        experiment.simulate();
        // after 2 steps
        assertArrayEquals(new Moon[]{
                new Moon(new int[]{5, -3, -1}, new int[]{3, -2, -2}),
                new Moon(new int[]{1, -2, 2}, new int[]{-2, 5, 6}),
                new Moon(new int[]{1, -4, -1}, new int[]{0, 3, -6}),
                new Moon(new int[]{1, -4, 2}, new int[]{-1, -6, 2}),
        }, experiment.getMoons());

        for (int i = 0; i < 8; i++) {
            experiment.simulate();
        }

        // after 10 steps
        assertArrayEquals(new Moon[]{
                new Moon(new int[]{2, 1, -3}, new int[]{-3, -2, 1}),
                new Moon(new int[]{1, -8, 0}, new int[]{-1, 1, 3}),
                new Moon(new int[]{3, -6, 1}, new int[]{3, 2, -3}),
                new Moon(new int[]{2, 0, 4}, new int[]{1, -1, -1}),
        }, experiment.getMoons());
    }
}

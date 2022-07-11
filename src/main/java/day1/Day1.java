package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/1.txt")
        );

        // part 1
        long sum = 0;
        for (String line : lines) {
            int mass = Integer.parseInt(line);
            sum += mass / 3 - 2;
        };
        System.out.println(sum);

        // part 2
        sum = 0;
        for (String line : lines) {
            int mass = Integer.parseInt(line);
            sum += calculateRecursiveFuel(mass);
        };
        System.out.println(sum);
    }

    private static long calculateRecursiveFuel(int mass) {
        int fuel = mass / 3 - 2;
        if (fuel <= 0) {
            return 0;
        }

        return fuel + calculateRecursiveFuel(fuel);
    }
}

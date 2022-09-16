package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {
    public static void main(String[] args) throws IOException {
        // part 1
        Tuple<Tuple<Integer, Integer>, Integer> result1 = part1("src/main/resources/inputs/10.txt");
        System.out.println(result1.second());

        // part 2
        List<Tuple<Integer, Integer>> asteroids = part2("src/main/resources/inputs/10.txt", result1.first());
        System.out.println(asteroids.get(199).first() * 100 + asteroids.get(199).second());
    }

    public static Tuple<Tuple<Integer, Integer>, Integer> part1(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Tuple<Integer, Integer>> coordinates = getAllCoordinates(lines);
        return calculateBestLocation(coordinates);
    }

    public static List<Tuple<Integer, Integer>> part2(
            String inputFilename,
            Tuple<Integer, Integer> location) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );

    }

    private static List<Tuple<Integer, Integer>> getAllCoordinates(List<String> lines) {
        List<Tuple<Integer, Integer>> coordinates = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) == '#')
                    coordinates.add(new Tuple<>(x, y));
            }
        }

        return coordinates;
    }

    private static Tuple<Tuple<Integer, Integer>, Integer> calculateBestLocation(List<Tuple<Integer, Integer>> coordinates) {
        Tuple<Integer, Integer> bestLocation = coordinates.get(0);
        int maxNumAsteroids = 0;

        for (int i = 0; i < coordinates.size(); i++) {
            Tuple<Integer, Integer> current = coordinates.get(i);
            Set<Tuple<Integer, Float>> set = new HashSet<>();

            for (int j = 0; j < coordinates.size(); j++) {
                if (i == j) continue;

                Tuple<Integer, Integer> other = coordinates.get(j);
                int x = current.first() - other.first();
                float y = current.second() - other.second();
                Tuple<Integer, Float> unitVector = x == 0 ?
                        new Tuple<>(0, y / Math.abs(y)) :
                        new Tuple<>(x / Math.abs(x), y / Math.abs(x));

                set.add(unitVector);
            }

            if (set.size() > maxNumAsteroids) {
                maxNumAsteroids = set.size();
                bestLocation = current;
            }
        }

        return new Tuple<>(bestLocation, maxNumAsteroids);
    }
}


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.abs;

public class Day3 {
    private static Set<Map.Entry<Integer, Integer>> buildWire(String line) {
        Set<Map.Entry<Integer, Integer>> set = new HashSet<>();

        int x = 0, y = 0;
        Map.Entry<Integer, Integer> coord;

        for (String move : line.split(","))  {
            char direction = move.charAt(0);
            int step = Integer.parseInt(move.substring(1));

            switch (direction) {
                case 'U':
                    for (int i = 0; i < step; i++) {
                        y += 1;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        set.add(coord);
                    }
                    break;
                case 'D':
                    for (int i = 0; i < step; i++) {
                        y -= 1;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        set.add(coord);
                    }
                    break;
                case 'L':
                    for (int i = 0; i < step; i++) {
                        x -= 1;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        set.add(coord);
                    }
                    break;
                case 'R':
                    for (int i = 0; i < step; i++) {
                        x += 1;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        set.add(coord);
                    }
                    break;
            }
        }

        return set;
    }

    private static int getClosestDistance(Set<Map.Entry<Integer, Integer>> intersections) {
        int minDistance = MAX_VALUE;

        for (Map.Entry<Integer, Integer> point : intersections) {
            int x = point.getKey();
            int y = point.getValue();
            int distance = Math.abs(x) + Math.abs(y);

            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    private static void part1(List<String> lines) {
        Set<Map.Entry<Integer, Integer>> firstWire = buildWire(lines.get(0));
        Set<Map.Entry<Integer, Integer>> secondWire = buildWire(lines.get(1));

        // intersection of two wires
        firstWire.retainAll(secondWire);

        int closestDistance = getClosestDistance(firstWire);
        System.out.println(closestDistance);
    }

    private static Map<Map.Entry<Integer, Integer>, Integer> buildWireWithSteps(String line) {
        Map<Map.Entry<Integer, Integer>, Integer> map = new HashMap<>();

        int x = 0, y = 0;
        Map.Entry<Integer, Integer> coord;

        int totalStep = 0;
        for (String move : line.split(","))  {
            char direction = move.charAt(0);
            int step = Integer.parseInt(move.substring(1));

            switch (direction) {
                case 'U':
                    for (int i = 0; i < step; i++) {
                        y++; totalStep++;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        map.put(coord, totalStep);
                    }
                    break;
                case 'D':
                    for (int i = 0; i < step; i++) {
                        y--; totalStep++;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        map.put(coord, totalStep);
                    }
                    break;
                case 'L':
                    for (int i = 0; i < step; i++) {
                        x--; totalStep++;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        map.put(coord, totalStep);
                    }
                    break;
                case 'R':
                    for (int i = 0; i < step; i++) {
                        x++; totalStep++;
                        coord = new AbstractMap.SimpleEntry<>(x, y);
                        map.put(coord, totalStep);
                    }
                    break;
            }
        }

        return map;
    }

    private static int getMinimumStep(Set<Map.Entry<Integer, Integer>> intersections,
                                      Map<Map.Entry<Integer, Integer>, Integer> firstWireMap,
                                      Map<Map.Entry<Integer, Integer>, Integer> secondWireMap) {
        int minStep = MAX_VALUE;

        for (Map.Entry<Integer, Integer> point : intersections) {
            int step = firstWireMap.get(point) + secondWireMap.get(point);
            if (step < minStep) {
                minStep = step;
            }
        }

        return minStep;
    }

    private static void part2(List<String> lines) {
        Map<Map.Entry<Integer, Integer>, Integer> firstWire = buildWireWithSteps(lines.get(0));
        Map<Map.Entry<Integer, Integer>, Integer> secondWire = buildWireWithSteps(lines.get(1));

        Set<Map.Entry<Integer, Integer>> firstWireSet = firstWire.keySet();
        Set<Map.Entry<Integer, Integer>> secondWireSet = secondWire.keySet();

        // intersection
        firstWireSet.retainAll(secondWireSet);

        int minimumStep = getMinimumStep(firstWireSet, firstWire, secondWire);
        System.out.println(minimumStep);
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/3.txt")
        );

        // part1(lines);
        part2(lines);
    }
}

package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
            Tuple<Integer, Integer> source) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );

        List<Point> points = getAllPoints(lines, source);
        SortedMap<Float, List<Point>> asteroids = arrangePoints(points);
        return vaporise(asteroids);
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

    private static List<Point> getAllPoints(List<String> lines, Tuple<Integer, Integer> source) {
        List<Point> points = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) == '#' &&
                        !(x == source.first() && y == source.second()))
                    points.add(new Point(x, y, source));
            }
        }

        return points;
    }

    // Build strategy for vaporising the asteroids.
    // Create a sorted hash map with:
    //   + Keys are angles, sorted incrementally
    //   + Values are sorted list, elements are points which are sorted by distance incrementally
    private static SortedMap<Float, List<Point>> arrangePoints(List<Point> points) {
        SortedMap<Float, List<Point>> asteroids = new TreeMap<>();

        for (Point point : points) {
            List<Point> list = asteroids.getOrDefault(point.getAngle(), new ArrayList<>());
            list.add(point);

            asteroids.put(point.getAngle(), list);
        }

        for (List<Point> list : asteroids.values()) {
            Collections.sort(list);
        }

        return asteroids;
    }

    private static List<Tuple<Integer, Integer>> vaporise(SortedMap<Float, List<Point>> asteroids) {
        List<Tuple<Integer, Integer>> result = new ArrayList<>();
        int level = 0;
        boolean halt = true;
        while (true) {
            for (List<Point> list : asteroids.values()) {
                if (list.size() > level) {
                    halt = false; // there is still a level to go
                    Point nearestPoint = list.get(level);
                    result.add(new Tuple<>(nearestPoint.getX(), nearestPoint.getY()));
                }
            }

            level++;
            if (halt)
                break;

            halt = true;
        }

        return result;
    }
}

class Point implements Comparable<Point> {
    private int x;
    private int y;

    private float angle; // in radian
    private int distance;

    Point(int x, int y, Tuple<Integer, Integer> source) {
        this.x = x;
        this.y = y;

        int relative_x = x - source.first();
        int relative_y = source.second() - y;

        // we're moving clockwise, hence calculate the angle using atan2(x, y)
        angle = (float) Math.atan2(relative_x, relative_y);
        // map angle to 0..2PI instead of -PI..PI
        angle = (angle >= 0) ? angle : (float) (2 * Math.PI + angle);

        distance =  relative_y * relative_y + relative_x * relative_x;
    }

    public float getAngle() {
        return angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Point other) {
        if (angle < other.angle || (angle == other.angle && distance < other.distance))
            return -1;

        return (angle == other.angle && distance == other.distance) ? 0 : 1;
    }
}
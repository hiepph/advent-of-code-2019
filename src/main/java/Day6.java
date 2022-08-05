import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/6.txt")
        );


        // part1
        // BFS, sum all vertex levels
        Graph g = makeGraph(lines);
        g.BFS("COM");
        System.out.println("part 1: " + g.countOrbits());

        // part 2
        // BFS
        System.out.println("part 2: " + g.getShortestDistance("YOU", "SAN"));
    }

    private static Graph makeGraph(List<String> lines) {
        Graph g = new Graph();
        for (String line : lines) {
            String[] vertices = line.split("\\)");
            g.addEdge(vertices[0], vertices[1]);
        }

        return g;
    }
}

// adjacent list graph
class Graph {
    private final Map<String, Integer> levels;
    private final Map<String, LinkedList<String>> adj;

    Graph() {
        adj = new HashMap<>();
        levels = new HashMap<>();
    }

    void addEdge(String u, String v) {
        if (!adj.containsKey(u)) {
            adj.put(u, new LinkedList<>());
        }
        if (!adj.containsKey(v)) {
            adj.put(v, new LinkedList<>());
        }

        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    void BFS(String s) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(s);
        queue.add(s);

        for (String key : adj.keySet()) {
            levels.put(key, 0);
        }

        while (queue.size() != 0) {
            s = queue.poll();

            for (String n : adj.get(s)) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    queue.add(n);
                    levels.put(n, levels.get(s) + 1);
                }
            }
        }
    }

     public int countOrbits() {
        int result = 0;

        for (Integer level : levels.values()) {
            result += level;
        }

        return result;
    }

    public int getShortestDistance(String s, String d) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(s);
        queue.add(s);

        for (String key : adj.keySet()) {
            levels.put(key, 0);
        }

        while (queue.size() != 0) {
            s = queue.poll();

            for (String n : adj.get(s)) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    queue.add(n);
                    levels.put(n, levels.get(s) + 1);

                    if (n.equals(d)) {
                        // minus source and destination vertices
                        return levels.get(n) - 2;
                    }
                }
            }
        }

        return -1;
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// BFS, sum all node levelss
public class Day6 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/6.txt")
        );

        Graph g = makeGraph(lines);

        // part1
        g.BFS("COM");
        System.out.println("part 1: " + g.countOrbits());
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
    private Map<String, Integer> levels;
    private Map<String, LinkedList<String>> adj;

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

            Iterator<String> i = adj.get(s).listIterator();
            while (i.hasNext()) {
                String n = i.next();
                if (!visited.contains(n)) {
                    visited.add(n);
                    queue.add(n);
                    levels.put(n, levels.get(s) + 1);
                }
            }
        }
    }

    int countOrbits() {
        int result = 0;

        for (Integer level : levels.values()) {
            result += level;
        }

        return result;
    }
}

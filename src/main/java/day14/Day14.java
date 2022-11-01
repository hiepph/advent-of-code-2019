package day14;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    // Returns the minimum number of OREs needed.
    public static long part1(String inputFilename) {
        Map<Chemical, Chemical> oresRequirement = getOresRequirement(inputFilename);
        TreeNode rootNode = makeTree(inputFilename);

        return countOres(rootNode, oresRequirement);
    }

    /*
     * Make a binary tree from the input.
     * Modify the map of ore requirements.
     */
    public static Map<Chemical, Chemical> getOresRequirement(String inputFilename) {
        Map<Chemical, Chemical> oresRequirement = new HashMap<>();

        Scanner sc = null;
        try {
            sc = new Scanner(Paths.get(inputFilename));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.contains("ORE")) {
                // e.g. 10 ORE => 10 A
                Pattern p = Pattern.compile("(\\d+) ORE => (\\d+) (\\w+)");
                Matcher m = p.matcher(line);
                m.find();

                oresRequirement.put(
                        new Chemical(m.group(3), Integer.parseInt(m.group(2))),
                        new Chemical("ORE", Integer.parseInt(m.group(1)))
                );
            }
        }

        return oresRequirement;
    }

    public static TreeNode makeTree(String inputFilename) {
        TreeNode rootNode = null;

        Scanner sc = null;
        try {
            sc = new Scanner(Paths.get(inputFilename));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.contains("ORE")) {

            }
        }

        return rootNode;
    }

    /*
     * Given a root tree node, count the ores needed to make the leaves.
     */
    public static long countOres(TreeNode rootNode, Map<Chemical, Chemical> oresRequirement) {
        return 0;
    }
}

record Chemical(String name, int quantity) { }

class TreeNode {
    private List<TreeNode> children;
    private Chemical val;

    public TreeNode(Chemical val) {
        this.val = val;
    }

    public Chemical getVal() {
        return val;
    }

}
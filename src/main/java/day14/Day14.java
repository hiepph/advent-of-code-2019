package day14;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    public static void main(String[] args) {
        System.out.println(part1("src/main/resources/inputs/14.txt"));
    }

    // Returns the minimum number of OREs needed.
    public static int part1(String inputFilename) {
        return countOres(getReactions(inputFilename));
    }

    public static Map<String, Reaction> getReactions(String inputFilename) {
        Map<String, Reaction> reactions = new HashMap<>();

        Scanner sc = null;
        try {
            sc = new Scanner(Paths.get(inputFilename));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            //
            // Example cases:
            //
            // 10 ORE => 10 A
            // 5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
            // 7 A, 1 E => 1 FUEL
            //
            Pattern p = Pattern.compile("(\\d+) (\\w+)");
            Matcher m = p.matcher(line);

            Deque<Chemical> chemicals = new ArrayDeque<>();
            while (m.find()) {
                chemicals.add(new Chemical(m.group(2), Integer.parseInt(m.group(1))));
            }

            Chemical product = chemicals.removeLast();
            reactions.put(
                    product.name(),
                    new Reaction(product, new ArrayList<>(chemicals))
            );
        }

        return reactions;
    }

    public static int countOres(Map<String, Reaction> reactions) {
        Map<String, Integer> wastes = new HashMap<>();
        Queue<Chemical> needs = new ArrayDeque<>();
        needs.add(new Chemical("FUEL", 1));

        int numOres = 0;

        while (!needs.isEmpty()) {
            Chemical need = needs.remove();

            Chemical product = reactions.get(need.name()).product();
            List<Chemical> reactants = reactions.get(need.name()).reactants();

            for (Chemical reactant : reactants) {
                // calculate the minimum necessary reactants
                int needQuantity = need.quantity();
                int existedQuantity = wastes.getOrDefault(need.name(), 0);
                if (needQuantity <= existedQuantity) {
                    // abundant
                    wastes.put(need.name(), existedQuantity - needQuantity);
                    continue;
                }

                needQuantity -= existedQuantity;
                wastes.remove(need.name());

                int requiredReactantQuantity = (int) Math.ceil(needQuantity * 1.0 / product.quantity())
                        * reactant.quantity();

                if (reactant.name().equals("ORE")) {
                    numOres += requiredReactantQuantity;
                }  else {
                    // need to make some reactions
                    needs.add(new Chemical(reactant.name(), requiredReactantQuantity));
                }

                // reaction might produce wasted product
                int wastedQuantity = requiredReactantQuantity / reactant.quantity() * product.quantity()
                        - needQuantity;
                wastes.put(need.name(), wastedQuantity);

            };
        }

        return numOres;
    }
}

record Chemical(String name, int quantity) { }
record Reaction(Chemical product, List<Chemical> reactants) { }
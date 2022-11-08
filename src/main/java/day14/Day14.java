package day14;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    public static void main(String[] args) {
        System.out.println(part1("src/main/resources/inputs/14.txt"));
        System.out.println(part2("src/main/resources/inputs/14.txt"));
    }

    // Returns the minimum number of OREs needed.
    public static long part1(String inputFilename) {
        var reactions = getReactions(inputFilename);
        return countRequiredOres(reactions, 1);
    }

    // Returns the maximum amount of FUEL you can produce given 10^12 ores.
    // Approach: Binary search for a possible range of number of fuel.
    public static long part2(String inputFilename) {
        var reactions = getReactions(inputFilename);
        long numProvidedOres = (long) 1e12;

        long low =  numProvidedOres / countRequiredOres(reactions, 1);
        long high = low * 10;

        while (countRequiredOres(reactions, high) < numProvidedOres) {
            low = high;
            high *= 10;
        }

        long mid = 0;
        long numRequiredOres = 0;
        while (low < high) {
            mid = (low + high) / 2;
            numRequiredOres = countRequiredOres(reactions, mid);
            if (numRequiredOres < numProvidedOres)
                low = mid;
            else if (numRequiredOres > numProvidedOres)
                high = mid - 1;
            else
                break;
        }

        return mid - 1;
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
                chemicals.add(new Chemical(m.group(2), Long.parseLong(m.group(1))));
            }

            Chemical product = chemicals.removeLast();
            reactions.put(
                    product.name(),
                    new Reaction(product, new ArrayList<>(chemicals))
            );
        }

        return reactions;
    }

    public static long countRequiredOres(Map<String, Reaction> reactions, long numFuel) {
        Map<String, Long> exists = new HashMap<>();
        Map<String, Long> needs = new HashMap<>();
        needs.put("FUEL", numFuel);

        long numOres = 0;
        while (!needs.isEmpty()) {
            String need = needs.keySet().stream().findFirst().get();
            long needQuantity = needs.get(need);
            needs.remove(need);

            long existedQuantity = exists.getOrDefault(need, 0L);
            if (needQuantity <= existedQuantity)  {
                // abundant
                exists.put(need, existedQuantity - needQuantity);
                continue;
            }

            needQuantity -= existedQuantity;
            exists.remove(need);

            long productQuantity = reactions.get(need).product().quantity();
            long numReactions = (long) Math.ceil(needQuantity * 1.0 / productQuantity);

            exists.put(need, exists.getOrDefault(need, 0L) + numReactions * productQuantity - needQuantity);
            for (Chemical reactant : reactions.get(need).reactants()) {
                if (reactant.name().equals("ORE")) {
                    numOres += reactant.quantity() * numReactions;
                } else {
                    needs.put(reactant.name(),
                            needs.getOrDefault(reactant.name(), 0L) + reactant.quantity() * numReactions);
                }
            }
        }

        return numOres;
    }
}

record Chemical(String name, long quantity) { }
record Reaction(Chemical product, List<Chemical> reactants) { }
package day12;

import java.util.BitSet;

public class Day12 {
    public static void main(String[] args) {
        part1();
        System.out.println(part2("src/main/resources/inputs/12.txt"));
    }

    public static void part1() {
        var experiment = new Experiment("src/main/resources/inputs/12.txt");
        for (int i = 0; i < 1000; i++) {
            experiment.simulate();
        }
        System.out.println(experiment.getTotalEnergy());
    }

    public static long part2(String inputFileName) {
        var experiment = new Experiment(inputFileName);

        long[] cycles = new long[3];
        BitSet isRepeated = new BitSet(3);

        Moon[] initialMoons = new Moon[4];
        try {
            for (int i = 0; i < 4; i++) {
                initialMoons[i] = (Moon) experiment.getMoons()[i].clone();
            }
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }

        while (isRepeated.cardinality() < 3) {
            experiment.simulate();

            for (int axis = 0; axis < 3; axis++) {
                // skip axis that already had repeated cycle
                if (isRepeated.get(axis)) continue;

                cycles[axis]++;
                if (experiment.equalsState(initialMoons, axis))  {
                    isRepeated.set(axis);
                }
            }
        }

        return lcm(cycles);
    }

    private static long lcm(long[] arr) {
        return lcm(lcm(arr[0], arr[1]), arr[2]);
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    private static long gcd(long a, long b) {
        return (a % b == 0) ? b : gcd(b, a % b);
    }
}


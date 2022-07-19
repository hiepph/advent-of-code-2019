package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day2 {
    private static List<Integer> readInts(String line) {
        List<Integer> nums = new ArrayList<Integer>();

        for (String numString : line.split(",")) {
            nums.add(Integer.parseInt(numString));
        }

        return nums;
    }

    private static void part1(List<Integer> nums) {
        nums.set(1, 12);
        nums.set(2, 2);

        int i = 0;
        boolean halt = false;
        while (i < nums.size()) {
            int opCode = nums.get(i);

            switch (opCode) {
                case 1:
                    nums.set(nums.get(i + 3), nums.get(nums.get(i + 1)) + nums.get(nums.get(i + 2)));
                    break;
                case 2:
                    nums.set(nums.get(i + 3), nums.get(nums.get(i + 1)) * nums.get(nums.get(i + 2)));
                    break;
                case 99:
                    halt = true;
                    break;
            }

            if (halt) break;
            i += 4;
        }

        System.out.println(nums.get(0));
    }

    private static boolean outputProduced(List<Integer> nums, int noun, int verb) {
        nums.set(1, noun);
        nums.set(2, verb);

        int i = 0;
        boolean halt = false;
        while (i < nums.size()) {
            int opCode = nums.get(i);

            switch (opCode) {
                case 1:
                    nums.set(nums.get(i + 3), nums.get(nums.get(i + 1)) + nums.get(nums.get(i + 2)));
                    break;
                case 2:
                    nums.set(nums.get(i + 3), nums.get(nums.get(i + 1)) * nums.get(nums.get(i + 2)));
                    break;
                case 99:
                    halt = true;
                    break;
            }

            if (halt) break;
            i += 4;
        }

        return nums.get(0) == 19690720;
    }

    private static void part2(List<Integer> nums) {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                if (outputProduced(new ArrayList<>(nums), noun, verb))  {
                    System.out.println(100 * noun + verb);
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/2.txt")
        );

        List<Integer> nums = readInts(lines.get(0));

        // part 1
        part1(new ArrayList<>(nums));

        // part 2
        part2(new ArrayList<>(nums));
    }
}

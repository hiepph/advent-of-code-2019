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

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/2.txt")
        );

        List<Integer> nums = readInts(lines.get(0));

        // part 1
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
}

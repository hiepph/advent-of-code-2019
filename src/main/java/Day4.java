import java.util.*;
import java.util.stream.IntStream;

public class Day4 {
    public static boolean isIncreasing(ArrayList<Integer> digits) {
        for (int i = 0; i < digits.size() - 1; i++)  {
            if (digits.get(i + 1) < digits.get(i)) return false;
        }
        return true;
    }

    public static boolean containsDuplicate(ArrayList<Integer> digits) {
        Set<Integer> s = new HashSet<>(digits);
        return s.size() < digits.size();
    }

    public static boolean isValid(Integer number) {
        ArrayList<Integer> digits = new ArrayList<>();
        for (char c : number.toString().toCharArray()) {
            digits.add(c - '0');
        }

        return isIncreasing(digits) && containsDuplicate(digits);
    }

    public static void part1() {
        long res = IntStream.range(264360, 746325 + 1)
                .filter(n -> isValid(n))
                .count();
        System.out.println(res);
    }

    public static boolean containsGroupOfTwo(ArrayList<Integer> digits) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int d : digits) {
            m.put(d, m.getOrDefault(d, 0) + 1);
        }

        return m.values().contains(2);
    }

    public static boolean isValidPart2(Integer number) {
        ArrayList<Integer> digits = new ArrayList<>();
        for (char c : number.toString().toCharArray()) {
            digits.add(c - '0');
        }

        return isIncreasing(digits) && containsGroupOfTwo(digits);
    }

    public static void part2() {
        long res = IntStream.range(264360, 746325 + 1)
                .filter(n -> isValidPart2(n))
                .count();
        System.out.println(res);
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}
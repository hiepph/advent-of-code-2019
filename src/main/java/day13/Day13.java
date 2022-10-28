package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 {
    public static void main(String[] args) throws IOException {
        System.out.println(part1("src/main/resources/inputs/13.txt"));
        System.out.println(part2("src/main/resources/inputs/13.txt"));
    }

    // Returns the number of block tiles on the screen
    private static long part1(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Long> inputs = readNumbers(lines.get(0));

        Map<Point, Tile> map = new HashMap<>();

        int inputCode = 0;
        IntCodeComputer computer = new IntCodeComputer(inputs);
        try {
            while (!computer.isHalted()) {
                int x = (int) computer.execute(inputCode);
                int y = (int) computer.execute(inputCode);
                Tile tile = Tile.values()[(int) computer.execute(inputCode)];

                map.put(new Point(x, y), tile);
            }
        } catch (NoOpCodeException | NoModeException err) {
            System.out.println(err.getMessage());
        }

        return map.keySet().stream()
                .collect(Collectors.groupingBy(map::get))
                .get(Tile.Block)
                .size();
    }

    // Returns the  score after beating all blocks.
    private static long part2(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Long> data = readNumbers(lines.get(0));

        IntCodeComputer computer = new IntCodeComputer(data);
        var map = makeMap(data, computer);

        // play
        // set memory address
        // set stream of joystick input (by comparing the ball position to the next block position)
        // recompute each time input is set
        // refer: https://github.com/berkgulmus/AdventOfCode2019/blob/master/Day13.2.py
        // refer: https://www.reddit.com/r/adventofcode/comments/e9zgse/2019_day_13_solutions/

        return 0;
    }

    public static Map<Point, Tile> makeMap(List<Long> data, IntCodeComputer computer) {
        Map<Point, Tile> map = new HashMap<>();

        int inputCode = 0;
        try {
            while (!computer.isHalted()) {
                int x = (int) computer.execute(inputCode);
                int y = (int) computer.execute(inputCode);
                Tile tile = Tile.values()[(int) computer.execute(inputCode)];

                map.put(new Point(x, y), tile);
            }
        } catch (NoOpCodeException | NoModeException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }

        return map;
    }

    private static List<Long> readNumbers(String line) {
        List<Long> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Long.parseLong(numString));
        }

        return inputs;
    }
}

enum Tile { Empty, Wall, Block, Paddle, Ball };

class IntCodeComputer {
    private final List<Long> memory;
    private long instructionPointer;
    private String instr;
    private long diagnosticCode;
    private long relativeBase;
    private boolean isHalted;

    public IntCodeComputer(List<Long> inputs) {
        this.memory = new ArrayList<>(Collections.nCopies(9999, 0L));
        for (int i = 0; i < inputs.size(); i++) {
            this.memory.set(i, inputs.get(i));
        }

        this.diagnosticCode = 0;
        this.instructionPointer = 0;
        this.relativeBase = 0;
        this.isHalted = false;
    }

    /*
     * Instruction has the form: ABCDE, in which:
     *   + DE: 2-digit opcode
     *   + C: mode of first parameter
     *   + B: mode of second parameter
     *   + A: mode of third parameter
     *
     * ref:
     *   + https://adventofcode.com/2019/day/2
     *   + https://adventofcode.com/2019/day/5
     */
    public long execute(int inputCode) throws NoOpCodeException, NoModeException {
        // poll mode: runs continuously until outputs a diagnostic code
        while (true) {
            instr = String.format("%05d", getMemory(instructionPointer));
            int opCode = (int) Long.parseLong(instr.substring(3));
            switch (opCode) {
                case 1:
                    setMemory(3, getParameter(1) + getParameter(2));
                    instructionPointer += 4;
                    break;
                case 2:
                    setMemory(3, getParameter(1) * getParameter(2));
                    instructionPointer += 4;
                    break;
                case 3:
                    setMemory(1, inputCode);
                    instructionPointer += 2;
                    break;
                case 4:
                    diagnosticCode = getParameter(1);
                    instructionPointer += 2;
                    return diagnosticCode;
                case 5:
                    if (getParameter(1) != 0) {
                        instructionPointer = getParameter(2);
                    } else {
                        instructionPointer += 3;
                    }
                    break;
                case 6:
                    if (getParameter(1) == 0) {
                        instructionPointer = getParameter(2);
                    } else {
                        instructionPointer += 3;
                    }
                    break;
                case 7:
                    setMemory(3, getParameter(1) < getParameter(2) ? 1 : 0);
                    instructionPointer += 4;
                    break;
                case 8:
                    setMemory(3, getParameter(1) == getParameter(2) ? 1 : 0);
                    instructionPointer += 4;
                    break;
                case 9:
                    relativeBase += getParameter(1);
                    instructionPointer += 2;
                    break;
                case 99:
                    isHalted = true;
                    return 0;
                default:
                    throw new NoOpCodeException(opCode);
            }
        }
    }


    /*
     * Types of mode:
     *   + 0: position mode
     *   + 1: immediate mode
     *   + 2: relative mode
     */
    private long getParameter(int ordinal) throws NoModeException {
        char mode = instr.charAt(3 - ordinal);
        return switch (mode) {
            case '0' -> getMemory(getMemory(instructionPointer + ordinal));
            case '1' -> getMemory(instructionPointer + ordinal);
            case '2' -> getMemory(relativeBase + getMemory(instructionPointer + ordinal));
            default -> throw new NoModeException(mode);
        };
    }

    /*
     * Invalid to try to access the memory at a negative address.
     */
    private long getMemory(long address) {
        return memory.get((int) address);
    }

    /*
     * Parameters that an instruction writes to will *never be in immediate mode*
     */
    private void setMemory(int ordinal, long value) throws NoModeException {
        char mode = instr.charAt(3 - ordinal);
        switch (mode) {
            case '0', '1' -> memory.set((int) getMemory(instructionPointer + ordinal), value);
            case '2' -> memory.set((int) (relativeBase + getMemory(instructionPointer + ordinal)), value);
            default -> throw new NoModeException(mode);
        }
    }

    public long getDiagnosticCode() {
        return diagnosticCode;
    }

    public boolean isHalted() {
        return isHalted;
    }
}

record Point(int x, int y) { }

class NoOpCodeException extends Exception {
    public NoOpCodeException(int opCode) {
        super(String.format("No such opcode: %d", opCode));
    }
}

class NoModeException extends Exception {
    public NoModeException(int mode) {
        super(String.format("No such mode: %d", mode));
    }
}

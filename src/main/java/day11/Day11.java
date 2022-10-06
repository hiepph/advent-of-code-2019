package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    public static void main(String[] args) throws IOException {
        System.out.println(part1("src/main/resources/inputs/11.txt"));
    }

    public static long part1(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Long> inputs = readNumbers(lines.get(0));

        Robot robot = new Robot();
        Map<Point, Integer> map = new HashMap<>();
        // starts with black
        int inputCode = 0;

        IntCodeComputer computer = new IntCodeComputer(inputs);
        try {
            while (!computer.isHalted()) {
                int colour = (int) computer.execute(inputCode);
                int direction  = (int) computer.execute(inputCode);

                robot.paint(colour);
                map.put(robot.getPosition(), colour);
                robot.move(direction);

                inputCode = map.getOrDefault(robot.getPosition(), 0);
            }
        } catch (NoOpCodeException | NoModeException err) {
            System.out.println(err.getMessage());
            return -1;
        }

        return map.size();
    }

    private static List<Long> readNumbers(String line) {
        List<Long> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Long.parseLong(numString));
        }

        return inputs;
    }
}

class Robot {
    private Point position;
    private Point face;
    private static final Point UP = new Point(0, -1);
    private static final Point DOWN = new Point(0, 1);
    private static final Point LEFT = new Point(-1, 0);
    private static final Point RIGHT = new Point(1, 0);

    public Robot() {
        // starts at the origin
        position = new Point(0, 0);
        // start with face ^
        face = UP;
    }

    public void paint(int colour) { }

    public void move(int direction) {
        if (direction == 0) {
            // turn left
            if (face.equals(UP)) {
                face = LEFT;
            } else if (face.equals(LEFT)) {
                face = DOWN;
            } else if (face.equals(DOWN)) {
                face = RIGHT;
            } else {
                face = UP;
            }
        } else {
            // turn right
            if (face.equals(UP)) {
                face = RIGHT;
            } else if (face.equals(RIGHT)) {
                face = DOWN;
            } else if (face.equals(DOWN)) {
                face = LEFT;
            } else {
                face = UP;
            }
        }

        position = new Point(position.x() + face.x(), position.y() + face.y());
    }

    public Point getPosition() {
        return position;
    }
}

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

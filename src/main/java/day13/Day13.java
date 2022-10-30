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

    /*
     * Returns the number of block tiles on the screen
     */
    public static int part1(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Integer> instructions = readNumbers(lines.get(0));

        IntCodeComputer computer = new IntCodeComputer(instructions);
        try {
            computer.execute();
        } catch (NoOpCodeException | NoModeException err) {
            System.err.println(err.getMessage());
            System.exit(1);
        }

        Map<Point, Tile> map = new HashMap<>();
        Queue<Integer> outputs = computer.getOutputs();
        while (!outputs.isEmpty()) {
            int x = outputs.remove();
            int y = outputs.remove();
            Tile tile = Tile.values()[outputs.remove()];

            if (tile != Tile.Empty)
                map.put(new Point(x, y), tile);
        }

        return map.keySet().stream()
                .collect(Collectors.groupingBy(map::get))
                .get(Tile.Block)
                .size();
    }

    // Returns the  score after beating all blocks.
    private static int part2(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Integer> instructions = readNumbers(lines.get(0));

        IntCodeComputer computer = new IntCodeComputer(instructions);
        computer.setMemoryDirectly(0, 2);

        Point ball = null, paddle = null;
        Map<Point, Tile> map = new HashMap<>();
        int score = -1;

        try {
            while (!computer.isHalted()) {
                computer.execute();

                Queue<Integer> outputs = computer.getOutputs();
                while (!outputs.isEmpty()) {
                    int x = outputs.remove();
                    int y = outputs.remove();
                    if (x == -1 && y == 0) {
                        score = outputs.remove();
                        continue;
                    }

                    Tile tile = Tile.values()[outputs.remove()];

                    if (tile != Tile.Empty)
                        map.put(new Point(x, y), tile);

                    if (tile == Tile.Ball)  ball = new Point(x, y);
                    if (tile == Tile.Paddle) paddle = new Point(x, y);
                }

                // add input code depends on the positions of the ball and the paddle.
                int inputCode;
                if (ball.x() < paddle.x()) inputCode = -1; // move left
                else if (ball.x() > paddle.x()) inputCode = 1; // move right
                else inputCode = 0;
                computer.addInput(inputCode);
            }
        } catch (NoOpCodeException | NoModeException err) {
            System.err.println(err.getMessage());
            System.exit(1);
        }

        return score;
    }

    private static List<Integer> readNumbers(String line) {
        List<Integer> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Integer.parseInt(numString));
        }

        return inputs;
    }
}

enum Tile { Empty, Wall, Block, Paddle, Ball };

class IntCodeComputer {
    private final List<Integer> memory;
    private int instructionPointer;
    private int diagnosticCode;
    private int relativeBase;
    // queue of input code
    private Queue<Integer> inputs;
    // queue of diagnostic code
    private Queue<Integer> outputs;
    private boolean isHalted;

    public IntCodeComputer(List<Integer> instructions) {
        this.memory = new ArrayList<>(Collections.nCopies(9999, 0));
        for (int i = 0; i < instructions.size(); i++) {
            this.memory.set(i, instructions.get(i));
        }

        this.diagnosticCode = 0;
        this.instructionPointer = 0;
        this.relativeBase = 0;

        this.inputs = new ArrayDeque<>();
        this.outputs = new ArrayDeque<>();

        this.isHalted = false;
    }

    /*
     * Execute continuously until it needs an input.
     */
    public void execute() throws NoOpCodeException, NoModeException {
        boolean isPolling = false;
        while (!isHalted && !isPolling) {
            //
            //  Instruction has the form: ABCDE in which,
            //    + DE: 2-digit opcode
            //    + C: mode of first parameter
            //    + B: mode of second parameter
            //    + A: mode of third parameter
            //
            String literalInstruction = String.format("%05d", getMemory(instructionPointer));
            int opCode = Integer.parseInt(literalInstruction.substring(3));
            String parameters = literalInstruction.substring(0, 3);

            switch (opCode) {
                case 1:
                    setMemory(parameters, 3,
                            getParameter(parameters, 1) + getParameter(parameters, 2));
                    instructionPointer += 4;
                    break;
                case 2:
                    setMemory(parameters, 3,
                            getParameter(parameters, 1) * getParameter(parameters, 2));
                    instructionPointer += 4;
                    break;
                case 3:
                    if (inputs.isEmpty()) {
                        isPolling = true;
                        break;
                    }

                    // consume one input
                    setMemory(parameters, 1,
                            inputs.remove());
                    instructionPointer += 2;
                    break;
                case 4:
                    diagnosticCode = getParameter(parameters, 1);
                    instructionPointer += 2;
                    outputs.add(diagnosticCode);
                    break;
                case 5:
                    if (getParameter(parameters, 1) != 0) {
                        instructionPointer = getParameter(parameters, 2);
                    } else {
                        instructionPointer += 3;
                    }
                    break;
                case 6:
                    if (getParameter(parameters, 1) == 0) {
                        instructionPointer = getParameter(parameters, 2);
                    } else {
                        instructionPointer += 3;
                    }
                    break;
                case 7:
                    setMemory(parameters, 3,
                            getParameter(parameters, 1) < getParameter(parameters, 2) ? 1 : 0);
                    instructionPointer += 4;
                    break;
                case 8:
                    setMemory(parameters, 3,
                            getParameter(parameters, 1) == getParameter(parameters, 2) ? 1 : 0);
                    instructionPointer += 4;
                    break;
                case 9:
                    relativeBase += getParameter(parameters, 1);
                    instructionPointer += 2;
                    break;
                case 99:
                    isHalted = true;
                    break;
                default:
                    throw new NoOpCodeException(opCode);
            }
        }
    }


    /*
     * @param ordinal: the nth parameters.
     */
    private int getParameter(String parameters, int ordinal) throws NoModeException {
        char mode = parameters.charAt(3 - ordinal);
        return switch (mode) {
            // position mode
            case '0' -> getMemory(getMemory(instructionPointer + ordinal));
            // immediate mode
            case '1' -> getMemory(instructionPointer + ordinal);
            // relative mode
            case '2' -> getMemory(relativeBase + getMemory(instructionPointer + ordinal));
            default -> throw new NoModeException(mode);
        };
    }

    /*
     * Invalid to try to access the memory at a negative address.
     */
    private int getMemory(int address) {
        return memory.get(address);
    }

    /*
     * @param ordinal: the nth parameters.
     */
    private void setMemory(String parameters, int ordinal, int value) throws NoModeException {
        char mode = parameters.charAt(3 - ordinal);
        switch (mode) {
            // position mode
            // Parameters that an instruction writes to will *never be in immediate mode*.
            case '0', '1' -> memory.set(getMemory(instructionPointer + ordinal), value);
            // relative mode
            case '2' -> memory.set(relativeBase + getMemory(instructionPointer + ordinal), value);
            default -> throw new NoModeException(mode);
        }
    }

    public void setMemoryDirectly(int index, int value) {
        memory.set(index, value);
    }

    public Queue<Integer> getOutputs() {
        return outputs;
    }

    public void addInput(int inputCode) {
        inputs.add(inputCode);
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

package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day9 {
    public static void main(String[] args) throws IOException {
        // part 1
        System.out.println(run("src/main/resources/inputs/9.txt", 1));
        // part 2
        System.out.println(run("src/main/resources/inputs/9.txt", 2));
    }

    public static long run(String inputFilename, int inputCode) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Long> inputs = readNumbers(lines.get(0));

        IntCodeComputer computer = new IntCodeComputer(inputs);
        try {
            while (!computer.isHalted()) {
                computer.execute(inputCode);
            }
        } catch (NoOpCodeException | NoModeException err) {
            System.out.println(err.getMessage());
            return -1;
        }

        return computer.getDiagnosticCode();
    }

    private static List<Long> readNumbers(String line) {
        List<Long> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Long.parseLong(numString));
        }

        return inputs;
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
    public void execute(int inputCode) throws day9.NoOpCodeException, day9.NoModeException {
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
                break;
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
                return;
            default:
                throw new NoOpCodeException(opCode);
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

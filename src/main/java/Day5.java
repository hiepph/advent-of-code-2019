import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/5.txt")
        );

        System.out.println(part1(lines.get(0)));
        System.out.println(part2(lines.get(0)));
    }

    private static int part1(String line) {
        Instruction instr = new Instruction(readInts(line), 1);

        try {
            instr.execute();
        } catch (NoOpCodeException err) {
            System.out.println(err.getMessage());
            return -1;
        }

        return instr.getDiagnosticCode();
    }


    private static int part2(String line) {
        Instruction instr = new Instruction(readInts(line), 5);
        try {
            instr.execute();
        } catch (NoOpCodeException err) {
            System.out.println(err.getMessage());
            return -1;
        }

        return instr.getDiagnosticCode();
    }

    private static List<Integer> readInts(String line) {
        List<Integer> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Integer.parseInt(numString));
        }

        return inputs;
    }
}

class Instruction {
    private List<Integer> inputs;
    private int instructionPointer;
    private String instr;
    private int inputCode;
    private int diagnosticCode;


    public Instruction(List<Integer> inputs, int inputCode) {
        this.inputs = inputs;
        this.inputCode = inputCode;

        this.instructionPointer = 0;
    }

    public void execute() throws NoOpCodeException {
        this.instr = String.format("%05d", inputs.get(instructionPointer));
        int opCode = Integer.parseInt(this.instr.substring(3));
        switch (opCode) {
            case 1:
                inputs.set(inputs.get(instructionPointer + 3),
                        firstParameter() + secondParameter());
                instructionPointer += 4;
                break;
            case 2:
                inputs.set(inputs.get(instructionPointer + 3),
                        firstParameter() * secondParameter());
                instructionPointer += 4;
                break;
            case 3:
                inputs.set(inputs.get(instructionPointer + 1), inputCode);
                instructionPointer += 2;
                break;
            case 4:
                diagnosticCode = inputs.get(inputs.get(instructionPointer + 1));
                instructionPointer += 2;
                break;
            case 5:
                if (firstParameter() != 0) {
                    instructionPointer = secondParameter();
                } else {
                    instructionPointer += 3;
                }
                break;
            case 6:
                if (firstParameter() == 0) {
                    instructionPointer = secondParameter();
                } else {
                    instructionPointer += 3;
                }
                break;
            case 7:
                inputs.set(inputs.get(instructionPointer + 3),
                        firstParameter() < secondParameter() ? 1 : 0);
                instructionPointer += 4;
                break;
            case 8:
                inputs.set(inputs.get(instructionPointer + 3),
                        firstParameter() == secondParameter() ? 1 : 0);
                instructionPointer += 4;
                break;
            case 99:
                return;
            default:
                throw new NoOpCodeException(opCode);
        }

        execute();
    }

    private int firstParameter() {
        return instr.charAt(2) == '0' ?
                inputs.get(inputs.get(instructionPointer + 1)) :
                inputs.get(instructionPointer + 1);
    }

    private int secondParameter() {
        return instr.charAt(1) == '0' ?
                inputs.get(inputs.get(instructionPointer + 2)) :
                inputs.get(instructionPointer + 2);
    }

    public int getDiagnosticCode() {
        return diagnosticCode;
    }
}

class NoOpCodeException extends Exception {
    public NoOpCodeException(int opCode) {
        super(String.format("No such opcode: %d", opCode));
    }
}
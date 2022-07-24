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

//        System.out.println(part1(lines.get(0)));
        System.out.println(part2(lines.get(0)));
    }

    private static int part1(String line) {
        List<String> inputs = Arrays.asList(line.split(","));

        int i = 0;
        int diagnosticCode = 0;

        while (i < inputs.size()) {
            Instruction instruction = new Instruction(inputs, i);

            int tempDiagnosticCode = instruction.execute();
            switch (instruction.getOpCode()) {
                case 1:
                case 2:
                    i += 4;
                    break;
                case 3:
                    i += 2;
                    break;
                case 4:
                    // get last output provided by opCode 4
                    diagnosticCode = tempDiagnosticCode;
                    i += 2;
                    break;
                case 99:
                    // halt
                    return diagnosticCode;
            }
        }

        return diagnosticCode;
    }

    private static int part2(String line) {
        List<Integer> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Integer.parseInt(numString));
        }

        Instruction2 instr = new Instruction2(inputs);
        try {
            instr.execute();
        } catch (NoOpCodeException err) {
            System.out.println(err.getMessage());
            return -1;
        }

        return instr.getDiagnosticCode();
    }
}

class Instruction {
    private int opCode;
    private List<String> inputs;
    private String instr;
    private int currentIndex;

    private int firstParameter() {
        return Integer.parseInt(
                instr.charAt(2) == '0' ?
                        inputs.get(Integer.parseInt(inputs.get(currentIndex + 1))) :
                        inputs.get(currentIndex + 1)
        );
    }

    private int secondParameter() {
        return Integer.parseInt(
                instr.charAt(1) == '0' ?
                        inputs.get(Integer.parseInt(inputs.get(currentIndex + 2))) :
                        inputs.get(currentIndex + 2)
        );
    }

    private int outputParameter(int index) {
        return Integer.parseInt(inputs.get(index)); // always position mode
    }

    public Instruction(List<String> inputs, int currentIndex) {
        this.inputs = inputs;
        this.currentIndex = currentIndex;

        this.instr = String.format("%05d", Integer.parseInt(inputs.get(currentIndex)));

        opCode = Integer.parseInt(instr.substring(3));
    }

    public int execute() {
        Integer output = 0;
        int outputPosition = 0;

        switch (opCode) {
            case 1:
                output = firstParameter() + secondParameter();
                outputPosition = outputParameter(currentIndex + 3);
                break;
            case 2:
                output = firstParameter() * secondParameter();
                outputPosition = outputParameter(currentIndex + 3);
                break;
            case 3:
                output = 1;
                outputPosition = outputParameter(currentIndex + 1);
                break;
            case 4:
                outputPosition = outputParameter(currentIndex + 1);
                return Integer.parseInt(inputs.get(outputPosition));
            case 99:
                return 0;
        }

        inputs.set(outputPosition, output.toString());
        return 0;
    }

    public int getOpCode() {
        return opCode;
    }
}

class Instruction2 {
    private List<Integer> inputs;
    private int instructionPointer;
    private String instr;
    private int inputCode = 5;
    private int diagnosticCode;


    public Instruction2(List<Integer> inputs) {
        this.inputs = inputs;
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
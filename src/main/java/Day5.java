import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

public class Day5 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/inputs/5.txt")
        );

        int diagosticCode = part1(lines.get(0));
        System.out.print(diagosticCode);
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
}

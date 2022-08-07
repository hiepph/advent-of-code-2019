package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.MIN_VALUE;

public class Day7 {
    public static void main(String[] args) throws IOException {
        System.out.println(part1("src/main/resources/inputs/7.txt"));
//        System.out.println(part2(readInts(lines.get(0))));
    }

    public static int part1(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Integer> inputs = readInts(lines.get(0));

        int maxThrusterSignal = MIN_VALUE;
        List<Integer> sequence = IntStream.range(0, 5).boxed().collect(Collectors.toList());
        Set<List<Integer>> permutations = generatePermutations(sequence);

        for (List<Integer> phaseSettingSequence : permutations) {
            Series series = new Series(phaseSettingSequence);

            int thrusterSignal = series.execute(inputs, false);
            if (thrusterSignal > maxThrusterSignal) {
                maxThrusterSignal = thrusterSignal;
            }
        }

        return maxThrusterSignal;
    }

    private static int part2(List<Integer> inputs) {
        int maxThrusterSignal = MIN_VALUE;
        List<Integer> sequence = IntStream.range(5, 10).boxed().collect(Collectors.toList());
        Set<List<Integer>> permutations = generatePermutations(sequence);

        for (List<Integer> phaseSettingSequence : permutations) {
            Series series = new Series(phaseSettingSequence);

            int thrusterSignal = series.execute(inputs, true);
            if (thrusterSignal > maxThrusterSignal) {
                maxThrusterSignal = thrusterSignal;
            }
        }

        return maxThrusterSignal;
    }

    /**
     * Generates a set of all permutations of a sequence.
     */
    private static Set<List<Integer>> generatePermutations(List<Integer> sequence) {
        Set<List<Integer>> permutations = new HashSet<>();
        if (sequence.size() == 0) {
            permutations.add(sequence);
            return permutations;
        }

        int first = sequence.get(0);
        Set<List<Integer>> restPermutation = generatePermutations(sequence.subList(1, sequence.size()));

        for (List<Integer> rest : restPermutation) {
            for (int i = 0; i <= rest.size(); i++)  {
                List<Integer> builder = new ArrayList<>(rest.subList(0, i));
                builder.add(first);
                builder.addAll(rest.subList(i, rest.size()));

                permutations.add(builder);
            }
        }

        return permutations;
    }

    private static List<Integer> readInts(String line) {
        List<Integer> inputs = new ArrayList<>();

        for (String numString : line.split(",")) {
            inputs.add(Integer.parseInt(numString));
        }

        return inputs;
    }
}

class Series {
    private final List<Integer> phaseSettingSequence;

    Series(List<Integer> phaseSettingSequence ) {
        this.phaseSettingSequence = phaseSettingSequence;
    }

    /**
     * Returns the thruster signal as the final output signal after a series of amplifiers.
     *
     * Feedback loop is enabled: If an amplifier meets an opcode 4 to output a signal, it will
     * continue from this position in the next loop.
     */
    public int execute(List<Integer> inputs, boolean feedbackLoopEnabled) {
        int outputSignal = 0;

        for (int phaseSetting : phaseSettingSequence) {
            Amplifier amp = new Amplifier(new ArrayList<>(inputs), Arrays.asList(phaseSetting, outputSignal));

            try {
                amp.execute();
            } catch (NoOpCodeException err) {
                System.out.println(err.getMessage());
                return -1;
            }

            outputSignal = amp.getDiagnosticCode();
        }

        return outputSignal;
    }
}

class Amplifier {
    private final List<Integer> inputs;
    private int instructionPointer;
    private String instr;
    private final List<Integer> inputCodes;
    private int inputCodePointer;
    private int diagnosticCode;


    public Amplifier(List<Integer> inputs, List<Integer> inputCodes) {
        this.inputs = inputs;
        this.instructionPointer = 0;

        this.inputCodes = inputCodes;
        this.inputCodePointer = 0;
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
                // input
                inputs.set(inputs.get(instructionPointer + 1), inputCodes.get(inputCodePointer));
                inputCodePointer++;
                instructionPointer += 2;
                break;
            case 4:
                // output
                // also stops after sending the output signal
                diagnosticCode = inputs.get(inputs.get(instructionPointer + 1));
                instructionPointer += 2;
                return;
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

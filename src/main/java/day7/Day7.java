package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.max;

public class Day7 {
    public static void main(String[] args) throws IOException {
        System.out.println(part1("src/main/resources/inputs/7.txt"));
        System.out.println(part2("src/main/resources/inputs/7.txt"));
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

    public static int part2(String inputFilename) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Integer> inputs = readInts(lines.get(0));

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
    private int thrusterSignal;

    Series(List<Integer> phaseSettingSequence) {
        this.phaseSettingSequence = phaseSettingSequence;
        this.thrusterSignal = -1;
    }

    /**
     * Returns the thruster signal as the final output signal after a series of amplifiers.
     *
     * Feedback loop is enabled: If an amplifier meets an opcode 4 to output a signal, it will
     * continue from this position in the next loop. The loop will halt if any of the amplifiers halts.
     */
    public int execute(List<Integer> inputs, boolean feedbackLoopEnabled) {
        int outputSignal = 0;

        // Amplifiers have memories
        List<Amplifier> amplifiers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            amplifiers.add(new Amplifier(new ArrayList<>(inputs), phaseSettingSequence.get(i)));
        }

        while (true) {
            for (int i = 0; i < 5; i++) {
                Amplifier amplifier = amplifiers.get(i);

                try {
                    amplifier.execute(outputSignal);
                } catch (NoOpCodeException err) {
                    System.out.println(err.getMessage());
                    return -1;
                }

                outputSignal = amplifier.getDiagnosticCode();
                if (amplifier.isHalted()) {
                    return thrusterSignal;
                }
            }

            // thrusterSignal is considered only as the last output signal from amplifier E
            thrusterSignal = outputSignal;
            if (!feedbackLoopEnabled) break;
        }

        return thrusterSignal;
    }
}

class Amplifier {
    private final List<Integer> inputs;
    private int instructionPointer;
    private String instr;
    private List<Integer> inputCodes;
    private final int phaseSetting;
    private boolean acceptPhaseSetting;
    private int diagnosticCode;
    private boolean isHalted;


    public Amplifier(List<Integer> inputs, int phaseSetting) {
        this.inputs = inputs;
        this.instructionPointer = 0;

        this.acceptPhaseSetting = true;
        this.phaseSetting = phaseSetting;

        this.isHalted = false;
    }

    public void execute(int inputCode) throws NoOpCodeException {
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
                if (acceptPhaseSetting) {
                    inputs.set(inputs.get(instructionPointer + 1), phaseSetting);
                    acceptPhaseSetting = false;
                } else {
                    inputs.set(inputs.get(instructionPointer + 1), inputCode);
                }
                instructionPointer += 2;
                break;
            case 4:
                // output
                // also pauses immediately after sending the output signal
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
                isHalted = true;
                return;
            default:
                throw new NoOpCodeException(opCode);
        }

        execute(inputCode);
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

    public boolean isHalted() {
        return isHalted;
    }
}

class NoOpCodeException extends Exception {
    public NoOpCodeException(int opCode) {
        super(String.format("No such opcode: %d", opCode));
    }
}

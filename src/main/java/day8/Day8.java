package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {
    public static void main(String[] args) throws IOException {
        System.out.println(part1("src/main/resources/inputs/8.txt", 25, 6));
        printImage(part2("src/main/resources/inputs/8.txt", 25, 6));
    }

    public static int part1(String inputFilename, int width, int height) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Layer> layers = breakIntoLayers(lines.get(0), width, height);
        Layer layer = getFewestZeroDigitsLayer(layers);
        return layer.getDigitFrequency('1') * layer.getDigitFrequency('2');
    }

    public static String part2(String inputFilename, int width, int height) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Layer> layers = breakIntoLayers(lines.get(0), width, height);
        return stackLayers(layers, width, height);
    }

    public static List<Layer> breakIntoLayers(String line, int width, int height) {
        List<Layer> layers = new ArrayList<>();
        int layerLength = width * height;

        for (int i = 0; i < line.length(); i += layerLength) {
            layers.add(new Layer(line.substring(i, i + layerLength), width, height));
        }

        return layers;
    }

    private static Layer getFewestZeroDigitsLayer(List<Layer> layers) {
        Layer layer = layers.get(0);

        for (int i = 1; i < layers.size(); i++) {
            if (layer.getDigitFrequency('0') > layers.get(i).getDigitFrequency('0')) {
                layer = layers.get(i);
            }
        }

        return layer;
    }

    private static String stackLayers(List<Layer> layers, int width, int height) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < width * height; i++) {
            char pixel = '2';
            for (Layer layer : layers) {
                char currentCharacter = layer.getCharAt(i);
                if (currentCharacter == '0' || currentCharacter == '1') {
                    pixel = currentCharacter;
                    break;
                }
            }
            stringBuilder.append(pixel);

            if ((i + 1) % width == 0) {
                stringBuilder.append('\n');
            }
        }

        return stringBuilder.toString();
    }

    private static void printImage(String image) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < image.length(); i++)  {
            char currentChar = image.charAt(i);
            switch (currentChar) {
                case '0':
                    stringBuilder.append(' ');
                    break;
                case '1':
                    stringBuilder.append('#');
                    break;
                default:
                    stringBuilder.append(currentChar);
            }
        }
        System.out.println(stringBuilder);
    }
}

class Layer {
    private final int width;
    private final int height;
    private String input;
    private Map<Character, Integer> counter;

    Layer(String input, int width, int height) {
        this.width = width;
        this.height = height;
        this.input = input;

        counter = new HashMap<>();
        for (char digit : input.toCharArray()) {
            counter.put(digit, counter.getOrDefault(digit, 0) + 1);
        }
    }

    public int getDigitFrequency(char digit) {
        return counter.getOrDefault(digit, 0);
    }

    public char getCharAt(int index) {
        return input.charAt(index);
    }
}
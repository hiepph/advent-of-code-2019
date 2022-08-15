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
    }

    public static int part1(String inputFilename, int width, int height) throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get(inputFilename)
        );
        List<Layer> layers = breakIntoLayers(lines.get(0), width, height);
        Layer layer = getFewestZeroDigitsLayer(layers);
        return layer.getDigitFrequency('1') * layer.getDigitFrequency('2');
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
}

class Layer {
    private final int width;
    private final int height;
    private Map<Character, Integer> counter;

    Layer(String input, int width, int height) {
        this.width = width;
        this.height = height;

        counter = new HashMap<>();
        for (char digit : input.toCharArray()) {
            counter.put(digit, counter.getOrDefault(digit, 0) + 1);
        }
    }

    public int getDigitFrequency(char digit) {
        return counter.get(digit);
    }
}
package day12;

public class Day12 {
    public static void main(String[] args) {
        var experiment = new Experiment("src/main/resources/inputs/12.txt");
        for (int i = 0; i < 1000; i++) {
            experiment.simulate();
        }
        System.out.println(experiment.getTotalEnergy());
    }
}


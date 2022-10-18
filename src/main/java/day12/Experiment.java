package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Experiment {
    private Moon[] moons;

    public Experiment(String inputFileName) {
        this.moons = readMoons(inputFileName);
    }

    public void simulate() {
        for (int i = 0; i < moons.length; i++) {
            for (int j = 0; j < moons.length; j++)  {
                if (j != i) {
                    moons[i].applyGravity(moons[j]);
                }
            }
        }

        for (int i = 0; i < moons.length; i++) {
            moons[i].applyVelocity();
        }
    }

    public Moon[] getMoons() {
        return moons;
    }

    public int getTotalEnergy() {
        int energy = 0;
        for (int i = 0; i < moons.length; i++) {
            energy += moons[i] .getTotalEnergy();
        }
        return energy;
    }

    private Moon[] readMoons(String inputFileName) {
        moons = new Moon[4];

        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(
                    Paths.get(inputFileName)
            );
        } catch (IOException e) {
            System.out.println("Cannot read input file.");
            System.exit(1);
        }

        for (int i = 0; i < moons.length; i++) {
            // Match 3 positive/negative numbers.
            // Sample line:
            // <x=-1, y=0, z=2>
            Pattern p = Pattern.compile("(-?\\d+)");
            Matcher m = p.matcher(lines.get(i));

            Integer[] nums = new Integer[3];
            for (int j = 0; j < 3; j++) {
                m.find();
                nums[j] = Integer.parseInt(m.group(1));
            }

            moons[i] = new Moon(new int[]{nums[0], nums[1], nums[2]});
        }

        return moons;
    }
}

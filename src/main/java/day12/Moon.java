package day12;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class Moon {
    private int[] position;
    private int[] velocity;

    public Moon(int[] position) {
        this.position = position;
        this.velocity = new int[]{0, 0, 0};
    }

    public Moon(int[] position, int[] velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public void applyGravity(Moon other) {
        for (int i = 0; i < 3; i++) {
            if (position[i] < other.position[i]) {
                velocity[i]++;
            } else if (position[i] > other.position[i]) {
                velocity[i]--;
            }
        }
    }

    public void applyVelocity() {
        for (int i = 0; i < 3; i++) {
            position[i] += velocity[i];
        }
    }

    @Override
    public String toString() {
        return String.format("[x=%d, y=%d, z=%d], [vx=%d, vy=%d, vz=%d]",
                position[0], position[1], position[2],
                velocity[0], velocity[1], velocity[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Moon that = (Moon) o;
        return Arrays.equals(position, that.position) && Arrays.equals(velocity, that.velocity);
    }
}

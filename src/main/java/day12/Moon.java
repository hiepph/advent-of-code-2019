package day12;

import java.util.Objects;

public record Moon(Tuple position, Tuple velocity) {
    public Moon(Tuple position) {
        this(position, new Tuple(0, 0, 0));
    }
}
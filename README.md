# Advent of Code 2019  (*TBD*)

This year is entertaining since I get to build a [computer](https://adventofcode.com/2019/day/9) that interprets integer codes.
Kudos to the people who involved to make it educational.

I use Java for this year with OOP style to encapsulate layers of abstractions and functionalities. It fits perfectly.

Code for the first days are terrible since I wanted to finish the challenges quickly, but I refined a lot along the way.
I had to refactor and add unit tests since the latter challenges require the previous computer module.


# Day 9

+ I keep getting this annoying `203` diagnostic code error:
  - Refer: [reddit](https://www.reddit.com/r/adventofcode/comments/e8aw9j/2019_day_9_part_1_how_to_fix_203_error/).
  - Parameters that an instruction writes to will *never be in immediate mode*.

+ `StackOverflow` error:
  + Switched to iterative mode instead of recursion mode:

```java
// bad 
computer.execute(inputCode);

void execute() { 
    // ...
    execute();
}

// fixed
while (!computer.isHalted()) {
  computer.execute(inputCode);
}
```

# Day 12

x, y, and z coordinates are independent of each other -> find the cycles in each of three axis. 
The answer is the least common multiple (LCM) of three cycles.

+ Reference:
  + https://0xdf.gitlab.io/adventofcode2019/12

# Day 13

+ Visualisation to understand part 2:  [reddit](https://www.reddit.com/r/adventofcode/comments/ea6htk/2019_d13_part_2_cl_xrender_solve/)

+ Refactor the code to have 2 streams of inputs and outputs.
+ Instead of external control -> internal control when run `execute()`.
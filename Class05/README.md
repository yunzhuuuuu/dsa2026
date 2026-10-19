# Assignment 5: Divide and Conquer and Dynamic Programming

## Files

- `Main.kt`: all production code, separated by labeled comments. It contains the
  matrix class, both multiplication algorithms, Needleman-Wunsch alignment,
  DNA sequences, program output, and benchmark code.
- `MainTest.kt`: unit tests for the matrix and sequence-alignment code.
- `BENCHMARK.md`: measured timings and hybrid analysis.
- `ALIGNMENT_RESULT.md`: the required alignment from the supplied sequences.

The main algorithms contain comments explaining the purpose of each important
step. Matrix indices start at zero.

## Run

Run all tests:

```text
mvn clean test
```

Print the supplied DNA alignment:

```text
mvn exec:java
```

Print the alignment and rerun matrix benchmarks:

```text
mvn exec:java -Dexec.args=benchmark
```

# Matrix Multiplication Benchmark

The benchmark uses deterministic random `Double` matrices. Each reported number
is the average of three multiplications after one JVM warm-up multiplication.
Times were measured on this computer, so another machine may give different
absolute values.

| Size | Conventional (ms) | Strassen cutoff 1 | Cutoff 8 | Cutoff 16 | Cutoff 32 | Cutoff 64 |
|---:|---:|---:|---:|---:|---:|---:|
| 32 | 0.143 | 14.513 | 0.486 | 0.139 | 0.058 | — |
| 64 | 0.092 | 41.219 | 1.356 | 0.666 | 0.497 | 0.268 |
| 128 | 0.574 | 103.996 | 7.257 | 2.997 | 1.766 | 1.200 |
| 256 | 4.841 | 639.854 | 28.031 | 13.431 | 8.195 | 6.397 |

## Analysis

Pure Strassen (cutoff 1) is much slower at these sizes because it creates many
small matrices and performs a lot of addition and subtraction. Increasing the
cutoff helps: among the Strassen variants tested, cutoff 64 was best for the
larger matrices. However, conventional multiplication was still faster through
size 256, so this benchmark did not find a size where Strassen reliably wins.
For this implementation, 64 is a reasonable hybrid cutoff, but conventional
multiplication should still be preferred for the tested sizes.

Run the same benchmark again with:

```text
mvn exec:java -Dexec.args=benchmark
```

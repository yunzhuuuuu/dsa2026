# Sorting Algorithms

## Complexity analysis

| Algorithm | Best time | Average time | Worst time | Extra space in this implementation |
|---|---:|---:|---:|---:|
| Heap sort | Theta(n log n) | Theta(n log n) | Theta(n log n) | Theta(n) |
| Selection sort | Theta(n^2) | Theta(n^2) | Theta(n^2) | Theta(n) |
| Merge sort | Theta(n log n) | Theta(n log n) | Theta(n log n) | Theta(n) |
| Quick sort | Theta(n log n) | Theta(n log n) | Theta(n^2) | Theta(n), plus the recursion stack |

Heap sort builds a max heap. It repeatedly moves the largest value to the end and repairs the heap. Building the heap takes Theta(n), and each removal takes Theta(log n).

Selection sort searches the unsorted part of the list for the next smallest value. It does this even when the input is already sorted, so it always takes Theta(n^2) time.

Merge sort splits the list in half and merges the sorted halves. Quick sort divides the values around a pivot. Quick sort usually takes Theta(n log n), but bad pivots can make it take Theta(n^2).

The Theta(n) space entries include the copied output list because every function in this project returns a new list and leaves its input unchanged.

## Benchmark method

The benchmark in `Main.kt` generates random integers from 0 through 99,999. It uses a fixed random seed, so the experiment can be repeated. Every algorithm receives the same input for each size.

I ran each algorithm once on 1,000 values as a JVM warmup. I then ran five timed trials and used the median result. The output of every algorithm was also checked against Kotlin's built-in sort.

Selection sort was stopped after 10,000 values because its quadratic growth makes the larger trials impractical. Heap, merge, and quick sort were also tested at 100,000 and 1,000,000 values.

## Results

Run `Main.kt` to regenerate the table on the current computer.

Each value is the median of five trials, in milliseconds.

| Input size | Heap sort | Selection sort | Merge sort | Quick sort |
|---:|---:|---:|---:|---:|
| 10 | 0.010 | 0.003 | 0.007 | 0.002 |
| 100 | 0.114 | 0.135 | 0.087 | 0.025 |
| 1,000 | 1.487 | 0.934 | 1.090 | 0.387 |
| 10,000 | 10.666 | 142.280 | 4.184 | 2.648 |
| 100,000 | 112.084 | - | 64.449 | 24.284 |
| 1,000,000 | 2,459.487 | - | 785.005 | 377.353 |

## Conclusions

Selection sort became much slower as the input grew. It was slightly faster than heap sort at 1,000 values. At 10,000 values, however, it took about 142 ms while heap sort took about 11 ms. This matches their expected Theta(n^2) and Theta(n log n) growth.

Quick sort was the fastest on the larger random inputs. For one million values, quick sort took about 377 ms, merge sort took 785 ms, and heap sort took 2,459 ms. Heap sort has Theta(n log n) time, but it does many swaps and heap repairs. Merge sort also has Theta(n log n) time, but it creates extra lists. Quick sort worked well because the random input usually gave it balanced partitions.

The very small tests were close to zero milliseconds, so the larger tests give a clearer comparison.

## New Frontiers in Sorting: AlphaDev

I learned about AlphaDev from an [official Google DeepMind article](https://deepmind.google/blog/alphadev-discovers-faster-sorting-algorithms/) and the [research paper published in Nature](https://www.nature.com/articles/s41586-023-06004-9). I also used ChatGPT to help me understand the main ideas in simpler language. 

AlphaDev uses reinforcement learning to find faster sorting code. It treats the process of choosing assembly instructions like a game. The AI tries different instructions and receives a reward when the result is correct and fast. Over time, it learns which choices produce better programs.

This is different from the algorithms we studied in class. We wrote steps such as splitting a list, choosing a pivot, or building a heap. AlphaDev searches directly for low-level instruction sequences. It focused on small sorting routines, such as sorting three, four, or five values. These small routines are also used as parts of larger sorting algorithms.

AlphaDev found new ways to move and copy values while using fewer instructions. The discovered routines were added to the LLVM C++ standard library. The paper reports improvements of up to 70% for sequences of five values and about 1.7% for sequences larger than 250,000 values.

AlphaDev did not improve the Big-O complexity of sorting. Instead, it made the actual implementation faster. This is still important because standard sorting libraries are used very often. A small improvement can save a large amount of total computing time when the code runs many times.

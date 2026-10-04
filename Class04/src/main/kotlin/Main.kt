import kotlin.random.Random
import kotlin.system.measureNanoTime

private data class Algorithm(
    val name: String,
    val largestInput: Int,
    val sort: (List<Int>) -> List<Int>,
)

/** Run the benchmark and print a Markdown table of median runtimes. */
fun main() {
    val algorithms = listOf(
        Algorithm("Heap sort", 1_000_000, ::heapSort),
        Algorithm("Selection sort", 10_000, ::selectionSort),
        Algorithm("Merge sort", 1_000_000, ::mergeSort),
        Algorithm("Quick sort", 1_000_000, ::quickSort),
    )
    val sizes = listOf(10, 100, 1_000, 10_000, 100_000, 1_000_000)
    val trials = 5
    val random = Random(202604)

    // Run each implementation once before measuring so JVM startup and class
    // loading have less influence on the recorded trials.
    val warmupInput = List(1_000) { random.nextInt(100_000) }
    algorithms.forEach { it.sort(warmupInput) }

    println("Each value is the median of $trials trials, in milliseconds.")
    println("| Input size | ${algorithms.joinToString(" | ") { it.name }} |")
    println("|---:|${algorithms.joinToString("|") { "---:" }}|")

    for (size in sizes) {
        val input = List(size) { random.nextInt(100_000) }
        val expected = input.sorted()
        val cells = algorithms.map { algorithm ->
            if (size > algorithm.largestInput) {
                "-"
            } else {
                val runtimes = MutableList(trials) {
                    var result: List<Int>
                    val elapsed = measureNanoTime {
                        result = algorithm.sort(input)
                    }
                    check(result == expected) { "${algorithm.name} returned an incorrect result" }
                    elapsed / 1_000_000.0
                }.sorted()
                "%.3f".format(runtimes[trials / 2])
            }
        }
        println("| ${"%,d".format(size)} | ${cells.joinToString(" | ")} |")
    }
}

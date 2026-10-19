import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

// Part 1: Creating a matrix class

/**
 * Stores a square matrix containing [size] rows and [size] columns.
 */
class Matrix private constructor(
    val size: Int,
    private val data: Array<DoubleArray>
) {
    /**
     * Creates a square matrix and fills every position with [initialValue].
     */
    constructor(size: Int, initialValue: Double = 0.0) : this(
        size,
        Array(size) { DoubleArray(size) { initialValue } }
    ) {
        require(size > 0) { "Matrix size must be positive." }
    }

    /**
     * Returns the value stored at [row], [column].
     */
    operator fun get(row: Int, column: Int): Double {
        checkIndex(row, column)
        return data[row][column]
    }

    /**
     * Stores [value] at [row], [column].
     */
    operator fun set(row: Int, column: Int, value: Double) {
        checkIndex(row, column)
        data[row][column] = value
    }

    /**
     * Adds [other] to this matrix entry by entry.
     */
    operator fun plus(other: Matrix): Matrix {
        requireSameSize(other)
        return Matrix(size) { row, column -> this[row, column] + other[row, column] }
    }

    /**
     * Subtracts [other] from this matrix entry by entry.
     */
    operator fun minus(other: Matrix): Matrix {
        requireSameSize(other)
        return Matrix(size) { row, column -> this[row, column] - other[row, column] }
    }

    /**
     * Splits this matrix into four equally sized matrices.
     */
    fun quadrants(): List<Matrix> {
        require(size % 2 == 0) { "Only even-sized matrices can be split evenly." }
        val half = size / 2
        val topLeft = Matrix(half)
        val topRight = Matrix(half)
        val bottomLeft = Matrix(half)
        val bottomRight = Matrix(half)

        for (row in 0 until half) {
            for (column in 0 until half) {
                topLeft[row, column] = this[row, column]
                topRight[row, column] = this[row, column + half]
                bottomLeft[row, column] = this[row + half, column]
                bottomRight[row, column] = this[row + half, column + half]
            }
        }
        return listOf(topLeft, topRight, bottomLeft, bottomRight)
    }

    /** Returns an independent copy of this matrix and all its stored values. */
    fun copy(): Matrix = Matrix(size) { row, column -> data[row][column] }

    /**
     * Checks whether corresponding entries differ by no more than [tolerance].
     * This is more reliable than exact equality after floating-point arithmetic.
     */
    fun approximatelyEquals(other: Matrix, tolerance: Double = 1e-9): Boolean {
        if (size != other.size) return false
        return data.indices.all { row ->
            data[row].indices.all { column ->
                kotlin.math.abs(data[row][column] - other.data[row][column]) <= tolerance
            }
        }
    }


    // Part 2: Strassen’s Algorithm for Matrix Multiplication

    /**
     * Multiplies this matrix by [other] using conventional O(n^3) multiplication.
     */
    fun multiply(other: Matrix): Matrix? {
        if (size != other.size) return null

        val answer = Matrix(size)
        for (row in 0 until size) {
            for (shared in 0 until size) {
                // Reusing this value avoids looking it up for every output column.
                val leftValue = data[row][shared]
                for (column in 0 until size) {
                    answer.data[row][column] += leftValue * other.data[shared][column]
                }
            }
        }
        return answer
    }

    /**
     * Multiplies this matrix by [other] using Strassen's divide-and-conquer method.
     */
    fun strassenMultiply(other: Matrix, cutoff: Int = DEFAULT_STRASSEN_CUTOFF): Matrix? {
        if (size != other.size) return null
        require(cutoff >= 1) { "Cutoff must be at least 1." }

        val paddedSize = nextPowerOfTwo(size)
        val left = if (paddedSize == size) copy() else paddedTo(paddedSize)
        val right = if (paddedSize == size) other.copy() else other.paddedTo(paddedSize)
        return strassenRecursive(left, right, cutoff).trimTo(size)
    }

    override fun equals(other: Any?): Boolean = other is Matrix && approximatelyEquals(other)

    override fun hashCode(): Int = 31 * size + data.contentDeepHashCode()

    override fun toString(): String = data.joinToString(separator = "\n") { row ->
        row.joinToString(prefix = "[", postfix = "]", separator = ", ")
    }

    private fun checkIndex(row: Int, column: Int) {
        require(row in 0 until size && column in 0 until size) {
            "Index ($row, $column) is outside a $size x $size matrix."
        }
    }

    private fun requireSameSize(other: Matrix) {
        require(size == other.size) { "Matrices must have the same size." }
    }

    private fun paddedTo(newSize: Int): Matrix = Matrix(newSize) { row, column ->
        if (row < size && column < size) data[row][column] else 0.0
    }

    private fun trimTo(newSize: Int): Matrix = Matrix(newSize) { row, column -> data[row][column] }

    companion object {
        /**
         * Default size at which Strassen recursion switches to conventional
         * multiplication. The benchmark in `BENCHMARK.md` motivated this value.
         */
        const val DEFAULT_STRASSEN_CUTOFF = 64

        /**
         * Creates a matrix by calling [initializer] once for every position.
         */
        operator fun invoke(size: Int, initializer: (Int, Int) -> Double): Matrix {
            require(size > 0) { "Matrix size must be positive." }
            return Matrix(size, Array(size) { row ->
                DoubleArray(size) { column -> initializer(row, column) }
            })
        }

        /**
         * Builds a matrix from a list of rows.
         */
        fun fromRows(rows: List<List<Number>>): Matrix {
            require(rows.isNotEmpty()) { "A matrix needs at least one row." }
            require(rows.all { it.size == rows.size }) { "The input must be square." }
            return Matrix(rows.size) { row, column -> rows[row][column].toDouble() }
        }

        /** Returns the smallest power of two greater than or equal to [value]. */
        private fun nextPowerOfTwo(value: Int): Int {
            if (value == 1) return 1
            return 2.0.pow(ceil(log2(value.toDouble()))).toInt()
        }

        /**
         * Performs the recursive part of Strassen multiplication.
         */
        private fun strassenRecursive(left: Matrix, right: Matrix, cutoff: Int): Matrix {
            if (left.size <= cutoff) return left.multiply(right)!!

            val (a11, a12, a21, a22) = left.quadrants()
            val (b11, b12, b21, b22) = right.quadrants()

            // Strassen needs seven recursive products instead of the usual eight.
            val m1 = strassenRecursive(a11 + a22, b11 + b22, cutoff)
            val m2 = strassenRecursive(a21 + a22, b11, cutoff)
            val m3 = strassenRecursive(a11, b12 - b22, cutoff)
            val m4 = strassenRecursive(a22, b21 - b11, cutoff)
            val m5 = strassenRecursive(a11 + a12, b22, cutoff)
            val m6 = strassenRecursive(a21 - a11, b11 + b12, cutoff)
            val m7 = strassenRecursive(a12 - a22, b21 + b22, cutoff)

            val c11 = m1 + m4 - m5 + m7
            val c12 = m3 + m5
            val c21 = m2 + m4
            val c22 = m1 - m2 + m3 + m6
            return combine(c11, c12, c21, c22)
        }

        /** Reassembles four equal quadrants into one matrix twice their size. */
        private fun combine(
            topLeft: Matrix,
            topRight: Matrix,
            bottomLeft: Matrix,
            bottomRight: Matrix
        ): Matrix {
            val half = topLeft.size
            return Matrix(half * 2) { row, column ->
                when {
                    row < half && column < half -> topLeft[row, column]
                    row < half -> topRight[row, column - half]
                    column < half -> bottomLeft[row - half, column]
                    else -> bottomRight[row - half, column - half]
                }
            }
        }
    }
}

// Part 3: Dynamic Programming for Aligning Protein Sequences

/**
 * Holds one optimal alignment reconstructed by backtracking.
 */
data class AlignmentResult(
    val score: Int,
    val alignedFirst: String,
    val alignedSecond: String
) {
    init {
        require(alignedFirst.length == alignedSecond.length)
    }

    /** Creates a guide where `|` is a match, `.` a mismatch, and space a gap. */
    val matchLine: String
        get() = alignedFirst.indices.joinToString("") { index ->
            when {
                alignedFirst[index] == '-' || alignedSecond[index] == '-' -> " "
                alignedFirst[index] == alignedSecond[index] -> "|"
                else -> "."
            }
        }

    /** Formats both aligned sequences, their match guide, and the final score. */
    fun display(): String = "$alignedFirst\n$matchLine\n$alignedSecond\nScore: $score"
}

/**
 * Globally aligns [first] and [second] with Needleman-Wunsch dynamic programming.
 */
fun needlemanWunsch(
    first: String,
    second: String,
    matchScore: Int = 1,
    mismatchScore: Int = -1,
    gapScore: Int = -2
): AlignmentResult {
    val scores = Array(first.length + 1) { IntArray(second.length + 1) }

    // Aligning a nonempty prefix with an empty string requires only gaps.
    for (row in 1..first.length) scores[row][0] = row * gapScore
    for (column in 1..second.length) scores[0][column] = column * gapScore

    for (row in 1..first.length) {
        for (column in 1..second.length) {
            val diagonal = scores[row - 1][column - 1] +
                if (first[row - 1] == second[column - 1]) matchScore else mismatchScore
            val deleteFromSecond = scores[row - 1][column] + gapScore
            val insertIntoSecond = scores[row][column - 1] + gapScore
            scores[row][column] = maxOf(diagonal, deleteFromSecond, insertIntoSecond)
        }
    }

    val alignedFirst = StringBuilder()
    val alignedSecond = StringBuilder()
    var row = first.length
    var column = second.length

    // Walk backward through choices that could have produced each optimal score.
    while (row > 0 || column > 0) {
        val canMoveDiagonally = row > 0 && column > 0
        val diagonalScore = if (canMoveDiagonally) {
            scores[row - 1][column - 1] +
                if (first[row - 1] == second[column - 1]) matchScore else mismatchScore
        } else Int.MIN_VALUE

        when {
            canMoveDiagonally && scores[row][column] == diagonalScore -> {
                alignedFirst.append(first[row - 1])
                alignedSecond.append(second[column - 1])
                row--
                column--
            }
            row > 0 && scores[row][column] == scores[row - 1][column] + gapScore -> {
                alignedFirst.append(first[row - 1])
                alignedSecond.append('-')
                row--
            }
            else -> {
                alignedFirst.append('-')
                alignedSecond.append(second[column - 1])
                column--
            }
        }
    }

    // Backtracking builds the strings from end to start, so reverse them once.
    return AlignmentResult(
        score = scores[first.length][second.length],
        alignedFirst = alignedFirst.reverse().toString(),
        alignedSecond = alignedSecond.reverse().toString()
    )
}


// PROVIDED DNA SEQUENCES

val genomeSnippet = "TGGCGACAACCGTAGCGGAATATTTTCGCGACCAGGGAAAACGGGTCGTGCTTTTTATCGATTCCATGACCCGTTATGCGCGTGCTTTGCGAGACGTGGCACTGGCGTCGGGAGAGCGTCCGGCTCGTCGAGGTTATCCCGCCTCCGTATTCGATAATTTGCCCCGCTTGCTGGAACGCCCAGGGGCGACCAGCGAGGGAAGCATTACTGCCTTTTATACGGTACTGCTGGAAAGCGAGGAAGAGGCGGACCCGATGGCGGATGAAATTCGCTCTATCCTTGACGGTCACCTGTATCTGAGCAGAAAGCTGGCCGGGCAGGGACATTACCCGGCAATCGATGTACTGAAAAGCGTAAGCCGCGTTTTT"

val testAgainst = "TGGCCACCACGATAGCAGAATTTTTTCGCGATAATGGAAAGCGAGTCGTCTTGCTTGCCGACTCACTGACGCGTTATGCCAGGGCCGCACGGGAAATCGCTCTGGCCGCCGGAGAGACCGCGGTTTCTGGAGAATATCCGCCAGGCGTATTTAGTGCATTGCCACGACTTTTAGAACGTACGGGAATGGGAGAAAAAGGCAGTATTACCGCATTTTATACGGTACTGGTGGAAGGCGATGATATGAATGAGCCGTTGGCGGATGAAGTCCGTTCACTGCTTGATGGACATATTGTACTATCCCGACGGCTTGCAGAGAGGGGGCATTATCCTGCCATTGACGTGTTGGCAACGCTCAGCCGCGTTTTT"


// OUTPUT AND BENCHMARK

/** Runs the required DNA example. Pass "benchmark" to run matrix timing too. */
fun main(args: Array<String>) {
    println("Needleman-Wunsch alignment of genomeSnippet and testAgainst:\n")
    println(needlemanWunsch(genomeSnippet, testAgainst).display())

    if (args.contains("benchmark")) runMatrixBenchmarks()
}

/**
 * A small reproducible benchmark for comparing conventional multiplication with
 * Strassen and several hybrid cutoffs. Results vary by machine and JVM run.
 */
fun runMatrixBenchmarks() {
    val random = Random(5)
    val sizes = listOf(32, 64, 128, 256)
    val cutoffs = listOf(1, 8, 16, 32, 64)
    println("\nsize,algorithm,cutoff,milliseconds")

    for (size in sizes) {
        val left = Matrix(size) { _, _ -> random.nextDouble(-10.0, 10.0) }
        val right = Matrix(size) { _, _ -> random.nextDouble(-10.0, 10.0) }

        // Warm up the JVM so one-time compilation affects the timings less.
        left.multiply(right)
        left.strassenMultiply(right)

        printTiming(size, "conventional", null) { left.multiply(right) }
        for (cutoff in cutoffs.filter { it <= size }) {
            printTiming(size, "strassen", cutoff) {
                left.strassenMultiply(right, cutoff)
            }
        }
    }
}

private fun printTiming(size: Int, algorithm: String, cutoff: Int?, operation: () -> Unit) {
    val trials = 3
    val elapsed = measureNanoTime { repeat(trials) { operation() } }
    val milliseconds = elapsed / trials / 1_000_000.0
    println("$size,$algorithm,${cutoff ?: "-"},${"%.3f".format(milliseconds)}")
}

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class SortingAlgorithmsTest {
    private val algorithms = listOf(
        ::heapSort,
        ::selectionSort,
        ::mergeSort,
        ::quickSort,
    )

    @Test
    fun sortsEmptyList() {
        checkAll(emptyList())
    }

    @Test
    fun sortsOneValue() {
        checkAll(listOf(7))
    }

    @Test
    fun sortsDuplicatesAndNegatives() {
        checkAll(listOf(4, -2, 4, 0, -2, 9))
    }

    @Test
    fun sortsAlreadySortedValues() {
        checkAll(listOf(-3, -1, 0, 5, 8))
    }

    @Test
    fun sortsReverseSortedValues() {
        checkAll(listOf(9, 7, 5, 3, 1))
    }

    @Test
    fun randomValues() {
        val random = Random(42)
        checkAll(List(200) { random.nextInt(-1_000, 1_001) })
    }

    private fun checkAll(input: List<Int>) {
        val expected = input.sorted()
        val original = input.toList()
        for (algorithm in algorithms) {
            assertEquals(expected, algorithm(input))
            assertEquals(original, input)
        }
    }
}

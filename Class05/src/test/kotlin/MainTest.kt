import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MainTest {
    @Test
    fun getSet() {
        val matrix = Matrix(2)
        matrix[0, 1] = 7.5
        assertEquals(7.5, matrix[0, 1])
    }

    @Test
    fun invalidMatrix() {
        assertFailsWith<IllegalArgumentException> { Matrix(0) }
        assertFailsWith<IllegalArgumentException> {
            Matrix.fromRows(listOf(listOf(1, 2), listOf(3)))
        }
    }

    @Test
    fun operations() {
        val matrix = Matrix.fromRows(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(5, 6, 7, 8),
                listOf(9, 10, 11, 12),
                listOf(13, 14, 15, 16)
            )
        )
        val (topLeft, topRight, bottomLeft, bottomRight) = matrix.quadrants()

        assertEquals(Matrix.fromRows(listOf(listOf(1, 2), listOf(5, 6))), topLeft)
        assertEquals(Matrix.fromRows(listOf(listOf(3, 4), listOf(7, 8))), topRight)
        assertEquals(Matrix.fromRows(listOf(listOf(9, 10), listOf(13, 14))), bottomLeft)
        assertEquals(Matrix.fromRows(listOf(listOf(11, 12), listOf(15, 16))), bottomRight)
        assertEquals(matrix, matrix + Matrix(4) - Matrix(4))
    }

    @Test
    fun multiply() {
        val left = Matrix.fromRows(listOf(listOf(1, 2), listOf(3, 4)))
        val right = Matrix.fromRows(listOf(listOf(5, 6), listOf(7, 8)))
        val expected = Matrix.fromRows(listOf(listOf(19, 22), listOf(43, 50)))

        assertEquals(expected, left.multiply(right))
    }

    @Test
    fun strassen() {
        val left = Matrix.fromRows(listOf(listOf(1, 2), listOf(3, 4)))
        val right = Matrix.fromRows(listOf(listOf(5, 6), listOf(7, 8)))
        val expected = Matrix.fromRows(listOf(listOf(19, 22), listOf(43, 50)))

        // A cutoff of 1 makes this test enter the Strassen recursion.
        assertEquals(expected, left.strassenMultiply(right, cutoff = 1))
    }

    @Test
    fun strassenPadding() {
        val left = Matrix(3) { row, column -> (row * 3 + column + 1).toDouble() }
        val right = Matrix(3) { row, column -> if (row == column) 1.0 else 0.0 }

        assertEquals(left, left.strassenMultiply(right, cutoff = 1))
        assertTrue(left.multiply(right)!!.approximatelyEquals(left.strassenMultiply(right, 1)!!))
    }

    @Test
    fun differentSizes() {
        assertNull(Matrix(2).multiply(Matrix(3)))
        assertNull(Matrix(2).strassenMultiply(Matrix(3)))
    }

    @Test
    fun identicalAlignment() {
        val result = needlemanWunsch("GATT", "GATT")

        assertEquals(4, result.score)
        assertEquals("GATT", result.alignedFirst)
        assertEquals("GATT", result.alignedSecond)
    }

    @Test
    fun gapAlignment() {
        val result = needlemanWunsch("GATT", "GAT")

        assertEquals(1, result.score)
        assertEquals("GATT", result.alignedFirst.replace("-", ""))
        assertEquals("GAT", result.alignedSecond.replace("-", ""))
        assertEquals(1, result.alignedSecond.count { it == '-' })
    }

    @Test
    fun backtrace() {
        val first = "GATTACA"
        val second = "GCATGCU"
        val result = needlemanWunsch(first, second)

        assertEquals(first, result.alignedFirst.replace("-", ""))
        assertEquals(second, result.alignedSecond.replace("-", ""))
        assertEquals(result.alignedFirst.length, result.alignedSecond.length)
        assertTrue(result.display().contains("Score: ${result.score}"))
    }
}

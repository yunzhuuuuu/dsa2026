import org.junit.jupiter.api.Test //to use @Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TicTacToeTest {

    //Board

    @Test
    fun markPlacesCorrectly() {
        val board = TicTacToeBoard()

        board.mark(1, 2)

        assertEquals(
            TicTacToeBoard.PLAYER_1_MARK,
            board.getSquare(1, 2)
        )
    }

    @Test
    fun changePlayerEachMove() {
        val board = TicTacToeBoard()

        board.mark(0, 0)
        assertEquals(TicTacToeBoard.PLAYER_2_MARK, board.nextMove())

        board.mark(1, 1)
        assertEquals(TicTacToeBoard.PLAYER_1_MARK, board.nextMove())
    }

    @Test
    fun detectsRowWin() {
        val board = TicTacToeBoard()

        board.mark(0, 0) // X
        board.mark(1, 0) // O
        board.mark(0, 1) // X
        board.mark(1, 1) // O
        board.mark(0, 2) // X

        assertTrue(board.checkWin("X"))
    }

    @Test
    fun detectsColumnWin() {
        val board = TicTacToeBoard()

        board.mark(0, 0) // X
        board.mark(0, 1) // O
        board.mark(1, 0) // X
        board.mark(1, 1) // O
        board.mark(2, 0) // X

        assertTrue(board.checkWin("X"))
    }

    @Test
    fun detectsDiagonalWin() {
        val board = TicTacToeBoard()

        board.mark(0, 0) // X
        board.mark(0, 1) // O
        board.mark(1, 1) // X
        board.mark(0, 2) // O
        board.mark(2, 2) // X

        assertTrue(board.checkWin("X"))
    }

    @Test
    fun detectsNoWin() {
        val board = TicTacToeBoard()

        board.mark(0, 0) // X
        board.mark(0, 1) // O
        board.mark(1, 1) // X
        board.mark(1, 0) // O

        assertFalse(board.checkWin("X"))
        assertFalse(board.checkWin("O"))
    }


    //View

    @Test
    fun boardToStringShowsPlayerMark() {
        val board = TicTacToeBoard()

        board.mark(0, 0)

        val boardText = board.toString()

        assertTrue(boardText.contains("|X|"))
    }

}
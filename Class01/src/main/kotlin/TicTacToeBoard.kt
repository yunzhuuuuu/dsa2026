/**
 * Tic-tac-toe board implementation.
 */

class TicTacToeBoard {

    companion object {
        const val PLAYER_1_MARK = "X"
        const val PLAYER_2_MARK = "O"
        const val BLANK_MARK = " "
    }

    private val board: Array<Array<String>> =
        Array(3) { Array(3) { BLANK_MARK } }

    private var nextMove: String = PLAYER_1_MARK

    /**
     * Returns the mark of the player whose turn it is.
     */
    fun nextMove(): String {
        return nextMove
    }

    /**
     * Marks a square with the current player's mark.
     */
    fun mark(row: Int, col: Int) {
        if (getSquare(row, col) != BLANK_MARK) {
            throw IllegalArgumentException("Square is already occupied.")
        }
        board[row][col] = nextMove
        flipNextMove()
    }

    /**
     * Changes the current player from X to O or from O to X.
     */
    private fun flipNextMove() {
        nextMove = if (nextMove == PLAYER_1_MARK) {
            PLAYER_2_MARK
        } else {
            PLAYER_1_MARK
        }
    }

    /**
     * Checks whether the given player has won along any row.
     */
    private fun checkRowWin(player: String): Boolean {
        for (row in 0..2) {
            if (board[row][0] == player &&
                board[row][1] == player &&
                board[row][2] == player
            ) {
                return true
            }
        }

        return false
    }

    /**
     * Checks whether the given player has won along any column.
     */
    private fun checkColWin(player: String): Boolean {
        for (col in 0..2) {
            if (board[0][col] == player &&
                board[1][col] == player &&
                board[2][col] == player
            ) {
                return true
            }
        }

        return false
    }

    /**
     * Checks whether the given player has won along either diagonal.
     */
    private fun checkDiagWin(player: String): Boolean {
        val traceWin =
            board[0][0] == player &&
                    board[1][1] == player &&
                    board[2][2] == player

        val crossWin =
            board[0][2] == player &&
                    board[1][1] == player &&
                    board[2][0] == player

        return traceWin || crossWin
    }

    /**
     * Checks whether the given player has won anywhere on the board.
     */
    fun checkWin(player: String): Boolean {
        return checkRowWin(player) ||
                checkColWin(player) ||
                checkDiagWin(player)
    }

    /**
     * Returns the mark at a given board position.
     */
    fun getSquare(row: Int, col: Int): String {
        return board[row][col]
    }

    /**
     * Returns a text representation of the board.
     */
    override fun toString(): String {
        val rowDivider = "+-+-+-+"
        val lines = mutableListOf(rowDivider)

        for (row in 0..2) {
            lines.add("|${board[row].joinToString("|")}|")
            lines.add(rowDivider)
        }

        return lines.joinToString("\n")
    }
}
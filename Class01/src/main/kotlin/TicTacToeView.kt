/**
 * View classes for displaying the state of a tic-tac-toe game.
 */

abstract class TicTacToeView(
    protected val board: TicTacToeBoard
) {

    /**
     * Display the current state of the board.
     */
    abstract fun draw()
}

/**
 * A text-based view for displaying a TicTacToeBoard.
 */
class TextView(
    board: TicTacToeBoard
) : TicTacToeView(board) {

    /**
     * Prints the current board and indicates whose turn is next.
     */
    override fun draw() {
        println(board)
        println("It is now ${board.nextMove()}'s turn.")
    }
}
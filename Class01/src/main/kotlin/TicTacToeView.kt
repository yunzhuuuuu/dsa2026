/**
 * View classes for displaying the state of a tic-tac-toe game.
 */

abstract class TicTacToeView(
    val board: TicTacToeBoard
) {
    abstract fun draw()
}

class TextView(
    board: TicTacToeBoard
) : TicTacToeView(board) {

    override fun draw() {
        println(board)
        println("It is now ${board.nextMove()}'s turn.")
    }
}
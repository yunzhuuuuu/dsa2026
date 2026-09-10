/**
 * Controller classes for handling tic-tac-toe user input.
 */

abstract class TicTacToeController(
    val board: TicTacToeBoard
) {
    abstract fun move()
}


class TextController(
    board: TicTacToeBoard
) : TicTacToeController(board) {

    /**
     * Prompts the user for a valid move and updates the board.
     */
    override fun move() {
        while (true) {
            try {
                print("Type your next move (e.g., '0 1'): ")

                val coords = readln().trim().split(Regex("\\s+"))

                if (coords.size != 2) {
                    throw IllegalArgumentException()
                }

                val row = coords[0].toInt()
                val col = coords[1].toInt()

                if (row !in 0..2 || col !in 0..2) {
                    throw IllegalArgumentException()
                }

                board.mark(row, col)
                break

            } catch (e: NumberFormatException) {
                println("Error: invalid input, try again.")
            } catch (e: IllegalArgumentException) {
                println("Error: invalid move, try again.")
            }
        }
    }
}
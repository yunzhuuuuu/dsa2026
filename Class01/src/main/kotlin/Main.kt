/**
 * Main program for running a two-player tic-tac-toe game.
 */
fun main() {
    val board = TicTacToeBoard()
    val view = TextView(board)

    val player1 = TextController(board)
    val player2 = TextController(board)

    var step = 0

    while (step < 9) {
        val player: TextController
        val playerMark: String

        if (step % 2 == 0) {
            player = player1
            playerMark = TicTacToeBoard.PLAYER_1_MARK
        } else {
            player = player2
            playerMark = TicTacToeBoard.PLAYER_2_MARK
        }

        player.move()
        view.draw()

        if (board.checkWin(playerMark)) {
            println("Player $playerMark won!")
            return
        }

        step++
    }

    println("The game was a draw.")
}
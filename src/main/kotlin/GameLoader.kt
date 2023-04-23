import board.BoardBuilder
import character.CharacterBuilder

object GameLoader {
  fun newGame(): Game {
    val player =  CharacterBuilder.build()
    val board = BoardBuilder.build("StartingMap")
    return Game(board, player)
  }
}


object GameLoader {
  fun loadGame(gameId: Int = 0): Game {
    val player =  CharacterBuilder.build()
    val board = BoardBuilder.build("StartingMap")
    return Game(board, player)
  }
}

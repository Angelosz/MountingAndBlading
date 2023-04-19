package board

import Player
import board.piece.CorpsePiece
import board.piece.EmptyPiece
import board.piece.PlayerPiece

class Board(
  private val mapWidth: Int,
  private val boardMap: List<MapSlot>
) {
  init {
    boardMap.getOrNull(calculateListSlot(Player.position))?.piece = PlayerPiece()
  }

  private fun calculateListSlot(position: Pair<Int, Int>): Int {
    val (row, column) = position
    return row * mapWidth + column
  }

  fun display(): String {
    return boardMap.foldIndexed("") { index, display, piece ->
      display + nextPiece(index, piece)
    }.removeRange(0, 1)
  }

  private fun nextPiece(index: Int, piece: MapSlot): String =
    if (index % mapWidth == 0) "\n ${piece.piece.display} "
    else " ${piece.piece.display} "

  fun getMapSlotAt(position: Pair<Int, Int>): MapSlot? {
    return boardMap.getOrNull(calculateListSlot(position))
  }

  fun movePieceFromTo(position: Pair<Int, Int>, nextPosition: Pair<Int, Int>) {
    val fromMapSlot = getMapSlotAt(position)
    if (fromMapSlot != null) {
      getMapSlotAt(nextPosition)?.piece = fromMapSlot.piece
      fromMapSlot.piece = EmptyPiece()
    }
  }

    fun placePlayerCorpse() {
        getMapSlotAt(Player.position)?.piece = CorpsePiece()
    }
}
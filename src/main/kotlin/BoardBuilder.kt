import board.Board
import board.MapSlot
import board.piece.Piece
import board.piece.PieceBuilder

object BoardBuilder {
  fun build(mapFileName: String): Board {
    val map = getStarterMap()
    val mapWidth = getStarterMapWidth()
    return Board(mapWidth, map.split(' ').map { MapSlot(getPieceFor(it)) })
  }

  private fun getPieceFor(pieceName: String): Piece {
    return PieceBuilder.build(pieceName)
  }

  private fun getStarterMapWidth(): Int {
    return 11
  }

  private fun getStarterMap(): String {
    return  "T T <> T T C2 T T T T T " +
            ". T E2 . T . T T . C3 T " +
            "<> . . C0 T . . T . T T " +
            "T T . . T . . T E4 T T " +
            "T E0 . T T . . T . . T " +
            "T . . E1 . . . . E3 . B " +
            "T T T C1 T T T T . T T"
  }
}
package board.piece

import character.LootTable

object PieceBuilder {
  fun build(pieceName: String): Piece
  {
    return when(pieceName.first()){
      'T' -> TreePiece()
      'C' -> ChestPiece(LootTable.chests[pieceName.removePrefix("C").toInt()])
      'E' -> EnemyPiece(LootTable.enemies[pieceName.removePrefix("E").toInt()])
      'B' -> BossPiece()
      '<' -> TownPiece()
      else -> EmptyPiece()
    }
  }
}
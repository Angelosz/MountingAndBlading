import Direction.*
import GameState.*
import board.Board
import board.piece.*
import combat.Combat

enum class Direction(val position: Pair<Int, Int>){
  Up(Pair(-1, 0)), Down(Pair(1, 0)), Left(Pair(0, -1)), Right(Pair(0, 1))
}

val inputToDirection: Map<String, Direction> = mapOf(
        "w" to Up,
        "s" to Down,
        "a" to Left,
        "d" to Right,
)

enum class GameState(){
  OnBoard, InTown, Stopped
}

class Game(private val board: Board, private val player: Player) {
  private val display = Display(board)
  private var gameState = OnBoard

  private val onBoardActions: Map<String, (List<String>?) -> Unit> = mapOf(
    "character" to { player.displayInformation() },
    "w" to { playerMovesTo(Up) }, "a" to { playerMovesTo(Left) },
    "s" to { playerMovesTo(Down) }, "d" to { playerMovesTo(Right) },
    "use" to { prompt ->
      val itemIndex = prompt?.getOrNull(1)?.toIntOrNull()
      if(itemIndex != null) player.useItem(itemIndex)
      else display.message("use what? (use x)")
    },
    "info" to { prompt ->
      inputToDirection[prompt?.get(1)]?.let { direction -> display.infoAt(direction) }
    },
    "exit" to {
      display.exitMessage()
      gameState = Stopped
    }
  )
  private val inTownActions: Map<String, (List<String>?, TownPiece) -> Unit> = mapOf(
    "shop" to { _, town -> display.shopFrom(town) },
    "buy" to { prompt, town ->
      val itemIndex = prompt?.getOrNull(1)?.toIntOrNull()
      if( itemIndex != null ) town.buyItem(itemIndex, player)
      else display.failedToBuyMessage()
    },
    "sell" to { prompt, _ ->
      val itemIndex = prompt?.getOrNull(1)?.toIntOrNull()
      if( itemIndex != null ) player.sellItem(itemIndex)
      else display.failedToSellMessage()
    },
    "potion" to { _, town ->
      town.buyPotion(player)
    },
    "leave" to { _, _ ->
      display.message("Leaving town.")
      gameState = OnBoard
    }
  )

  fun play() {
    display.gameStartMessage()

    while(gameState != Stopped){
      display.boardMap()
      val prompt = readlnOrNull()?.trim()?.split(' ', limit = 2)
      onBoardActions[prompt?.first()]?.run { this(prompt) }
        ?: display.invalidCommandMessage()

      if(player.health < 1) {
        board.placePlayerCorpse()
        display.playerDeath()
        gameState = Stopped
      }
    }
  }

  private fun playerMovesTo(direction: Direction){
    if(gameState != OnBoard){
      display.message("Can't do that right now!")
      return
    }
    val playerNextPosition = Player.calculateNextPosition(direction)
    val mapSlot = board.getMapSlotAt(playerNextPosition)
    if(mapSlot != null){
      when(val piece = mapSlot.piece){
        is ChestPiece -> {
          display.lootedChest(piece)
          player.getLoot(piece.loot)
          mapSlot.piece = EmptyPiece()
        }
        is EmptyPiece -> {
          board.movePieceFromTo(Player.position, playerNextPosition)
          Player.changePosition(playerNextPosition)
        }
        is EnemyPiece -> {
          display.startedCombatWith(piece)
          startCombat(piece)
          if(piece.health < 1) mapSlot.piece = EmptyPiece()
        }
        is TownPiece -> {
          visitTown(piece)
        }
        else -> display.infoOf(piece)
      }
    }
  }

  private fun visitTown(town: TownPiece) {
    display.inTownActions()
    gameState = InTown

    while(gameState == InTown){
      val prompt = readlnOrNull()?.trim()?.split(' ', limit = 2)
      inTownActions[prompt?.first()]?.run { this(prompt, town)}
        ?: onBoardActions[prompt?.first()]?.run { this(prompt) }
        ?: display.invalidCommandMessage()
    }
  }

  private fun startCombat(enemy: EnemyPiece) {
    Combat(enemy, player).start()
  }
}




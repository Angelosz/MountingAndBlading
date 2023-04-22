import Direction.*
import GameState.*
import board.Board
import board.piece.*

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
  private var gameState = OnBoard

  private val onBoardActions: Map<String, (List<String>?) -> Unit> = mapOf(
    "character" to { player.displayInformation() },
    "w" to {playerMovesTo(Up)}, "a" to {playerMovesTo(Left)},
    "s" to {playerMovesTo(Down)}, "d" to {playerMovesTo(Right)},
    "use" to {prompt ->
      val itemIndex = prompt?.getOrNull(1)?.toIntOrNull()
      if(itemIndex != null) player.useItem(itemIndex)
      else println("use what? (use x)")
    },
    "info" to { prompt ->
      inputToDirection[prompt?.get(1)]?.let { lookAt(it) }
    },
    "exit" to {
      println(":(")
      gameState = Stopped
    }
  )
  private val inTownActions: Map<String, (List<String>?, TownPiece) -> Unit> = mapOf(
    "shop" to { _, town ->
      println(town.shop.display())
    },
    "buy" to { prompt, town ->
      val itemIndex = prompt?.getOrNull(1)?.toIntOrNull()
      if( itemIndex != null ) town.buyItem(itemIndex, player)
      else println("buy what? (buy x)")
    },
    "sell" to { prompt, _ ->
      val itemIndex = prompt?.getOrNull(1)?.toIntOrNull()
      if( itemIndex != null ) player.sellItem(itemIndex)
      else println("sell what? (sell x)")
    },
    "potion" to { _, town ->
      town.buyPotion(player)
    },
    "leave" to { _, _ ->
      println("Leaving town.")
      gameState = OnBoard
    }
  )

  fun play() {
    println("*P* = Player; *E* = Enemy; [º] = Chest; <-> = Town; T = Tree")
    displayActions()

    while(gameState != Stopped){
      displayBoard()
      val prompt = readlnOrNull()?.trim()?.split(' ', limit = 2)
      onBoardActions[prompt?.first()]?.run { this(prompt) } ?: println("what?")

      if(player.health < 1) {
        board.placePlayerCorpse()
        displayBoard()
        println("You are dead :(.")
        gameState = Stopped
      }
    }
  }

  private fun displayBoard() {
    println(board.display())
  }

  private fun displayActions() {
    println("To move enter: 'a', 'w', 's' or 'd'.\n" +
            "'info' + 'a', 'w', 's', 'd' to get information about the map piece in that slot." +
            "'character' to see the information of your character.\n" +
            "'use x' where x is the item slot to equip/use that item from the inventory.\n" +
            "'exit' to leave the game.")
  }

  private fun playerMovesTo(direction: Direction){
    if(gameState != OnBoard){
      println("Can't do that right now!")
      return
    }
    val playerNextPosition = Player.calculateNextPosition(direction)
    val mapSlot = board.getMapSlotAt(playerNextPosition)
    if(mapSlot != null){
      when(val piece = mapSlot.piece){
        is ChestPiece -> {
          println("You found some shinies!")
          player.getLoot(piece.loot)
          mapSlot.piece = EmptyPiece()
        }
        is EmptyPiece -> {
          println("You moved $direction")
          board.movePieceFromTo(Player.position, playerNextPosition)
          Player.changePosition(playerNextPosition)
        }
        is EnemyPiece -> {
          println("Combat Starting!")
          startCombat(piece)
          if(piece.health < 1) mapSlot.piece = EmptyPiece()
        }
        is PlayerPiece -> println("how did you do that?")
        is TreePiece -> println("You hit a tree!")
        is TownPiece -> {
          println("You visit a town.")
          visitTown(piece)
        }
        is CorpsePiece -> println("ugh...")
      }
    }
  }

  private fun visitTown(town: TownPiece) {
    town.displayActions()
    gameState = InTown

    while(gameState == InTown){
      val prompt = readlnOrNull()?.trim()?.split(' ', limit = 2)
      inTownActions[prompt?.first()]?.run { this(prompt, town)}
        ?: onBoardActions[prompt?.first()]?.run { this(prompt) }
        ?: println("what?")
    }
  }

  private fun lookAt(direction: Direction){
    println(board.getMapSlotAt(Player.calculateNextPosition(direction))?.piece?.info)
  }

  private fun startCombat(enemy: EnemyPiece) {
    while(player.health > 0){
      enemy.damagedBy(player.calculateDamage())
      if(enemy.health < 1) {
        player.getLoot(enemy.loot)
        break
      }
      player.damagedBy(enemy.damage)
    }
  }
}


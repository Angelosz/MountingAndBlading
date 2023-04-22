import Direction.*
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

class Game(private val board: Board, private val player: Player) {

  fun play() {
    println("*P* = Player; *E* = Enemy; [º] = Chest; <-> = Town; T = Tree")
    displayActions()
    while(true){
      displayBoard()
      val prompt = readlnOrNull()?.trim()?.split(' ')
      when(prompt?.first()){
        "character" -> player.displayInformation()
        "a", "w", "s", "d" -> inputToDirection[prompt.first()]?.let { playerMovesTo(it) }
        "use" -> {
          val itemIndex = prompt.getOrNull(1)?.toIntOrNull()
          if(itemIndex != null) player.useItem(itemIndex)
          else println("use what? (use x)")
        }
        "info" -> {
          inputToDirection[prompt[1]]?.let { lookAt(it) }
        }
        "exit" -> {
          println(":(")
          break
        }
        else -> println("what?")
      }
      if(player.health < 1) {
        println("You are dead :(.")
        board.placePlayerCorpse()
        break
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
          mapSlot.piece = EmptyPiece()
        }
        is PlayerPiece -> println("how did you do that?")
        is TreePiece -> println("You hit a tree!")
        is TownPiece -> {
          println("You visit a town.")
          visitTown(piece)
        }
        is CorpsePiece -> println("ugh, a corpse..")
      }
    }
  }

  private fun visitTown(town: TownPiece) {
    town.displayActions()
    while(true){
      val prompt = readlnOrNull()?.trim()?.split(' ', limit = 2)
      when(prompt?.first()){
        "shop" -> println(town.shop.display())
        "buy" -> {
          val itemIndex = prompt.getOrNull(1)?.toIntOrNull()
          if(itemIndex != null) town.buyItem(itemIndex, player)
          else println("buy what? (buy x)")
        }
        "sell" -> {
          val itemIndex = prompt.getOrNull(1)?.toIntOrNull()
          if(itemIndex != null) player.sellItem(itemIndex)
          else println("sell what? (sell x)")
        }
        "potion" -> town.buyPotion(player)
        "leave" -> {
          println("Leaving town.")
          break
        }
        "character" -> player.displayInformation()
        else -> println("what?")
      }
    }
  }

  private fun lookAt(direction: Direction){
    val playerNextPosition = Player.calculateNextPosition(direction)
    val mapSlot = board.getMapSlotAt(playerNextPosition)
    if(mapSlot != null){
      when(val piece = mapSlot.piece){
        is ChestPiece -> {
          println("A chest! It could be a boat!")
        }
        is EmptyPiece -> {
          println("Empty. You can move there!")
        }
        is EnemyPiece -> {
          println("Danger! Enemy ahead.")
          println(piece.displayInfo())
        }
        is PlayerPiece -> println("how did you do that?")
        is TreePiece -> println("Just a tree...")
        is TownPiece -> println("Visit town?")
        is CorpsePiece -> println("ugh, a corpse..")
      }
    }
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


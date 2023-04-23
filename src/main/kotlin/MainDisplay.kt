import board.Board
import board.piece.ChestPiece
import board.piece.Piece
import board.piece.TownPiece

class MainDisplay(private val board: Board) {

  fun message(message: String){
    println(message)
  }

  fun boardMap() {
    println(board.display())
  }

  fun gameStartMessage() {
    println("*P* = Player; *E* = Enemy; [º] = Chest; <-> = Town; T = Tree")
    playerActions()
  }

  private fun playerActions() {
    println("To move enter: 'a', 'w', 's' or 'd'.\n" +
            "'info' + 'a', 'w', 's', 'd' to get information about the map piece in that slot." +
            "'character' to see the information of your character.\n" +
            "'use x' where x is the item slot to equip/use that item from the inventory.\n" +
            "'exit' to leave the game.")
  }

  fun infoAt(direction: Direction) {
    println(board.getMapSlotAt(Player.nextPosition(direction))?.piece?.info)
  }

  fun inTownActions() {
    println("Welcome to town!")
    println("'shop' Look shop.\n" +
            "'buy x' where x is the slot id.\n" +
            "'sell x' where x is the inventory slot. Sells item at 1/2 cost.\n" +
            "'potion' to buy a potion(25 resources.)\n" +
            "'leave' to leave town.")
  }

  fun shopFrom(town: TownPiece) {
    println(town.shop.display())
  }

  fun playerDeath() {
    boardMap()
    println("You are dead :(.")
  }

  fun invalidCommandMessage() {
    println("what?")
  }

  fun exitMessage() {
    println(":(")
  }

    fun lootedChest(chest: ChestPiece) {
    println("You found some shinies!\n ${chest.loot.display()}")  }

    fun infoOf(piece: Piece?) {
    println(piece?.info)
  }

    fun failedToBuyMessage() {
    println("buy what? (buy x)")
  }

    fun failedToSellMessage() {
    println("sell what? (sell x)")
  }
}
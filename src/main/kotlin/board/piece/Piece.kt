package board.piece

import Player
import character.*

sealed interface Piece{
  val display: String
  val info: String
}

class EmptyPiece: Piece {
  override val display: String = " . "
  override val info: String = "Empty. You can move there!"
}

class PlayerPiece: Piece {
  override val display: String = "*P*"
  override val info: String = "How did you do that?!"
}

class TreePiece: Piece {
  override val display: String = " T "
  override val info: String = "Just a tree.."
}

class ChestPiece(val loot: Loot = Loot()): Piece {
  override val display: String = "[º]"
  override val info: String = "A chest! It could be a boat!"
}

open class EnemyPiece(val loot: Loot = Loot()): Piece {
  open val damage = 6
  open var health = 12
  open val defense = 1
  fun damagedBy(incomingDamage: Int) {
    val actualDamage = if ( defense > incomingDamage ) 1 else incomingDamage - defense
    health -= actualDamage
    println("You hit the enemy for $actualDamage. $health health remaining!")
    if(health < 1) println("You have defeated the enemy! Have some loot!")
  }

  fun displayInfo(): String {
    return "Health: $health\n" +
            "Damage: $damage\n" +
            "Defense: $defense"
  }
  override val display: String = "*E*"
  override val info: String = "Danger! Enemy ahead.\n ${displayInfo()}"
}

class BossPiece: EnemyPiece(){
  override val damage = 50
  override var health = 40
}

class CorpsePiece: Piece {
  override val display = "*X*"
  override val info: String = "huh.."
}

class TownPiece: Piece{
  val shop = Inventory()

  init {
    shop.addItems( listOf<Item>(
            Weapon("Shitty sword", 10, 1),
            Weapon("Ragnaros middle finger", 500, 20),
            Armor("Decent armor!", 50, 2),
            Armor("Nefarian's nipple", 500, 10),
    ))
  }

  fun displayTownActions() {
    println("'shop' Look shop.\n" +
            "'buy x' where x is the slot id.\n" +
            "'sell x' where x is the inventory slot. Sells item at 1/2 cost.\n" +
            "'potion' to buy a potion(25 resources.)\n" +
            "'leave' to leave town.")
  }

  fun buyItem(itemId: Int, player: Player) {
    val item = shop.getItem(itemId) ?: return
    if(player.resources >= item.cost){
      player.bought(item)
      shop.removeItem(itemId)
      println("Bought ${item.name}. Resources remaining: ${player.resources}")
    } else {
      println("Not enough Resources!")
    }
  }

  fun buyPotion(player: Player) {
    val potion = HealingPotion("Healing Potion", 25, 5)
    if(player.resources >= potion.cost) {
      player.bought(potion)
      println("You bought a potion for ${potion.cost}. Remaining resources: ${player.resources}")
    }
  }

  override val display = "<->"
  override val info: String = "Visit town?"

}
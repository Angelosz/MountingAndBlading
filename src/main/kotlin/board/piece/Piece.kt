package board.piece

import Player
import character.*

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

sealed interface Piece{
  val display: String
}

class EmptyPiece: Piece {
  override val display: String = " . "
}

class PlayerPiece: Piece {
  override val display: String = "*P*"
}

class TreePiece: Piece {
  override val display: String = " T "
}

class ChestPiece(val loot: Loot = Loot()): Piece {
  override val display: String = "[º]"
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
}

class BossPiece: EnemyPiece(){
  override val damage = 50
  override var health = 40
}

class CorpsePiece: Piece {
  override val display = "*X*"
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

  fun displayActions() {
    println("'shop' Look shop.")
    println("'buy x' where x is the slot id.")
    println("'sell x' where x is the inventory slot. Sells item at 1/2 cost.")
    println("'potion' to buy a potion(25 resources.)")
    println("'leave' to leave town.")
  }

  fun buyItem(itemId: Int, player: Player) {
    val item = shop.getItem(itemId) ?: return
    if(item.cost <= player.resources){
      player.bought(item)
      shop.removeItem(itemId)
      println("Bought ${item.name}. Resources remaining: ${player.resources}")
    } else {
      println("Not enough Resources!")
    }
  }

  fun buyPotion(player: Player) {
    val potion = HealingPotion("Healing Potion", 25, 5)
    if(player.resources >= potion.cost) player.bought(potion)
  }

  override val display = "<->"

}
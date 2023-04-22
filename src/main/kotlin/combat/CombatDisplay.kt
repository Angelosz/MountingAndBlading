package combat

import Player
import board.piece.EnemyPiece
import character.Loot

class CombatDisplay(private val enemy: EnemyPiece, private val player: Player) {
    fun playerDamagesEnemyBy(damage: Int) {
    println("You hit the enemy for $damage. They have ${enemy.health} health remaining.")
  }

    fun enemyDefeated(loot: Loot) {
    println("You have defeated the enemy! Have some loot!")
    println(loot.display())
  }

    fun enemyDamagesPlayerBy(damage: Int) {
    println("You are hit for $damage. $player.health health remaining!")  }

}
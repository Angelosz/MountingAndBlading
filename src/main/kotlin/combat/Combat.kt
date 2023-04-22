package combat

import Player
import board.piece.EnemyPiece

class Combat(private val enemy: EnemyPiece, private val player: Player) {

  private val display = CombatDisplay(enemy, player)
  fun start(){
    while(player.health > 0){
      playerAttacks()
      if(enemyDied()) {
        display.enemyDefeated(enemy.loot)
        player.getLoot(enemy.loot)
        break
      }
      enemyAttacks()
    }
  }

  private fun enemyDied() = enemy.health < 1

  private fun enemyAttacks() {
    val damage = calculateDamage(enemy.damage, player.defense)
    player.damagedBy(damage)
    display.enemyDamagesPlayerBy(damage)
  }

  private fun playerAttacks() {
    val damage = calculateDamage(player.damage, enemy.defense)
    enemy.damagedBy(damage)
    display.playerDamagesEnemyBy(damage)
  }

  private fun calculateDamage(damage: Int, defense: Int) = if( defense > damage ) 1 else damage - defense
}
package character

import Player

sealed class Item(val name: String, val cost: Int) {
    abstract fun display(): String
}

class Weapon(name: String, cost: Int, val damage: Int): Item(name, cost) {
    override fun display(): String {
        return "$name, +$damage damage. Value: $cost resources (Weapon)"
    }
}

class Armor(name: String, cost: Int, val defense: Int): Item(name, cost) {
    override fun display(): String {
        return "$name, +$defense defense. Value: $cost resources (Armor)"
    }
}

sealed class Consumable(name: String, cost: Int) : Item(name, cost) {
    abstract fun useOn(player: Player)
}

class HealingPotion(name: String, cost: Int, private val health: Int): Consumable(name, cost) {
    override fun display(): String {
        return "$name, +$health heal. Value: $cost resources (Potion)"
    }

    override fun useOn(player: Player){
        player.modifyHealthBy(health)
    }
}

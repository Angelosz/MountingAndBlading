package character

interface CharacterInterface {
    var resources: Int
    val inventory: Inventory
    var weapon: Weapon
    var armor: Armor
    var health: Int
    val damage: Int
    val defense: Int

    val isDead get() = health < 1
    val isAlive get() = health > 0
    fun calculateDamage() = damage + weapon.damage

    fun calculateDefense()= defense + armor.defense

    fun modifyHealthBy(amount: Int) {
        health += amount
    }

    fun damagedBy(incomingDamage: Int) {
        health -= incomingDamage
    }

    fun bought(item: Item) {
        resources -= item.cost
        inventory.addItem(item)
    }

    fun getLoot(loot: Loot) {
        resources += loot.resources
        inventory.addItems(loot.items)
    }
}
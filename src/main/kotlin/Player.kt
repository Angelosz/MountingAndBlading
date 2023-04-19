import character.*

class Player(
        var resources: Int,
        val inventory: Inventory,
        var weapon: Weapon,
        var armor: Armor,
        var health: Int = 20,
        val damage: Int = 2,
        val defense: Int = 1,) {

    fun displayInformation() {
        println("Your Character:")
        println(displayCharacterStats())
        println(displayResources())
        println(displayEquipment())
        println(displayInventory())
    }

    private fun displayCharacterStats(): String =
            "Health: $health\n" +
            "Damage: ${calculateDamage()}\n" +
            "Defense: ${calculateDefense()}"


    fun displayResources(): String = "Resources: $resources"
    fun displayEquipment(): String = "Weapon: ${weapon.display()}\nArmor: ${armor.display()}"
    fun displayInventory(): String = "Inventory:\n ${inventory.display()}"


    fun calculateDamage(): Int = damage + weapon.damage
    private fun calculateDefense(): Int = defense + armor.defense

    fun getLoot(loot: Loot) {
        println(loot.display())
        resources += loot.resources
        inventory.addItems(loot.items)
    }

    fun useItem(slot: Int) {
        val item = inventory.getItem(slot)
        if(item != null) {
            when(item){
                is Weapon -> {
                    inventory.putItemIn(slot, weapon)
                    weapon = item
                    println("Equipped ${weapon.name}")
                }
                is Armor -> {
                    inventory.putItemIn(slot, armor)
                    armor = item
                    println("Equipped ${armor.name}")
                }
                is Consumable ->{
                    item.useOn(this)
                    println("Used item ${item.name}")
                    inventory.removeItem(slot)
                }
            }
        } else println("Item not found.")
    }

    fun modifyHealthBy(amount: Int) {
        health += amount
    }

    fun damagedBy(incomingDamage: Int) {
        val actualDamage = calculateDamageAfterDefense(defense + armor.defense, incomingDamage)
        health -= actualDamage
        println("You are hit for $actualDamage. $health health remaining!")
    }

    private fun calculateDamageAfterDefense(actualDefense: Int, incomingDamage: Int) =
            if (actualDefense >= incomingDamage) 1 else incomingDamage - actualDefense

    fun bought(item: Item) {
        resources -= item.cost
        inventory.addItem(item)
    }

    fun sellItem(itemIndex: Int) {
        val item = inventory.getItem(itemIndex)
        if(item != null){
            resources += item.cost / 2
            inventory.removeItem(itemIndex)
        }
    }

    companion object CharacterPosition{
    private var row = 0
    private var column = 0
    val position
      get() = Pair(row, column)

    fun changePosition(position: Pair<Int, Int>){
      row = position.first
      column = position.second
    }

    fun calculateNextPosition(direction: Direction): Pair<Int, Int> {
      val (nextRow, nextColumn) = direction.position
      return Pair(row + nextRow, column + nextColumn)
    }
  }
}

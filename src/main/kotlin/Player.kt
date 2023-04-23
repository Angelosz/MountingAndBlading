import character.*


class Player( val character: Character ): CharacterInterface by character {
    val display = CharacterDisplay(character)

    fun getLoot(loot: Loot) {
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
                    display.equipped(item)
                }
                is Armor -> {
                    inventory.putItemIn(slot, armor)
                    armor = item
                    display.equipped(item)
                }
                is Consumable -> {
                    item.useOn(this)
                    display.used(item)
                    inventory.removeItem(slot)
                }
            }
        } else display.itemNotFound()
    }

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

    fun sellItem(itemIndex: Int) {
        val item = inventory.getItem(itemIndex)
        if(item != null){
            resources += item.cost / 2
            display.sold(item)
            inventory.removeItem(itemIndex)
        }
    }

    companion object CharacterPosition{
    private var row = 0
    private var column = 0
    val position
      get() = Pair(row, column)

    fun updatePosition(position: Pair<Int, Int>){
      row = position.first
      column = position.second
    }

    fun calculateNextPosition(direction: Direction): Pair<Int, Int> {
      val (nextRow, nextColumn) = direction.position
      return Pair(row + nextRow, column + nextColumn)
    }
  }
}

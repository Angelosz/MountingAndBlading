package character

class CharacterDisplay(val character: CharacterInterface) {


    val resources get() = "Resources: ${character.resources}"

    val equipment get() = "Weapon ${character.weapon.display()}\nArmor: ${character.armor.display()}"

    val inventory get() = "Inventory:\n${character.inventory.display()}"

    private val characterStats
        get() = "Health: ${character.health}\n" +
                "Damage: ${character.calculateDamage()}\n" +
                "Defense: ${character.calculateDefense()}"

    fun characterInformation() {
        println("Your Character:")
        println(characterStats)
        println(resources)
        println(equipment)
        println(inventory)
    }

    fun sold(item: Item) {
        println("Sold ${item.name} for ${item.cost / 2}. Resources remaining ${character.resources}.")
    }

    fun equippedWeapon() {
        println("Equipped ${character.weapon.name}")
    }

    fun equippedArmor() {
        println("Equipped ${character.armor.name}")
    }

    fun used(item: Item) {
        println("Used item ${item.name}")
    }

    fun itemNotFound() {
        println("Item not found!")
    }


}

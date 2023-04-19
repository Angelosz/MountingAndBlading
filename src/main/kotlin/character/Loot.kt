package character

data class Loot(val resources: Int = 25, val items: List<Item> = listOf()) {
    fun display(): String {
        return "Resources: $resources ${items.fold("") { display, item -> "\n $display" + item.display()}}"
    }
}

object  LootTable{
    val chests: List<Loot> = listOf(
            Loot(50, listOf(Armor("\"Better than Wood\" Armor", 40, 2))),
            Loot(20, listOf(HealingPotion("Healing Potion", 10, 5))),
            Loot(50, listOf(HealingPotion("Super Healing Potion", 10, 15))),
            Loot(50, listOf(Weapon("Did you say... Thunderfury?", 200, 10))),
    )
    val enemies: List<Loot> = listOf(
            Loot(30, listOf(Weapon("Cool sword", 50, 4))),
            Loot(30, listOf(HealingPotion("Healing Potion", 10, 5))),
            Loot(20, listOf(HealingPotion("Healing Potion", 10, 5))),
            Loot(50, listOf(HealingPotion("Super Healing Potion", 10, 15))),
            Loot(20, listOf(Armor("Farting cloud", 1000, 5))),
    )
}

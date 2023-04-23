package character

class Character(
    override var resources: Int,
    override val inventory: Inventory,
    override var weapon: Weapon,
    override var armor: Armor,
    override var health: Int = 20,
    override var damage: Int = 2,
    override var defense: Int = 1
) : CharacterInterface
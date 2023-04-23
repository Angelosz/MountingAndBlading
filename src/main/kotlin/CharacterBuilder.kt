import character.Armor
import character.Inventory
import character.Weapon

object CharacterBuilder {
    fun build(): Player {
      Player.updatePosition(Pair(2, 1))
      return buildStarterCharacter()
    }

    private fun buildStarterCharacter(): Player {
        return Player(
                100,
                Inventory(),
                Weapon("Wooden Sword", 20, 2),
                Armor("Wood?", 20, 1)
        )
    }
}
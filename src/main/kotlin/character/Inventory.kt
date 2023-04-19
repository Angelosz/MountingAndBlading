package character

class Inventory {
    private var itemIdCount = 0
    private val items: MutableMap<Int, Item> = mutableMapOf()

    fun addItems(newItems: List<Item>) {
        for (item in newItems) {
            items[itemIdCount] = item
            itemIdCount++
        }
    }

    fun addItem(item: Item){
        items[itemIdCount] = item
        itemIdCount++
    }

    fun removeItem(itemId: Int){
        items.remove(itemId)
    }

    fun display(): String{
        var textToDisplay = ""
        items.forEach { (id, item) ->
            textToDisplay += "(slot)$id: ${item.display()}\n" }
        return textToDisplay
    }

    fun getItem(slot: Int): Item? {
        return items[slot]
    }

    fun putItemIn(slot: Int, item: Item) {
        items[slot] = item
    }

}

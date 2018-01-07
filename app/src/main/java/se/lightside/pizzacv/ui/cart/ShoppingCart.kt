package se.lightside.pizzacv.ui.cart

import se.lightside.pizzacv.ui.restaurants.menu.PizzaMenuItemEntry

data class ShoppingCart(
        val items: List<PizzaMenuItemEntry> = emptyList()
) {

    val totalCount: Int = items.count()

    val totalPrice: Int by lazy { items.sumBy { it.price } }

    fun withItem(item: PizzaMenuItemEntry): ShoppingCart = copy(items = items + item)

    val itemMap by lazy {
        items.groupingBy { it }.aggregate { _, accumulator: Int?, _, _->  (accumulator ?: 0) + 1}
    }

}
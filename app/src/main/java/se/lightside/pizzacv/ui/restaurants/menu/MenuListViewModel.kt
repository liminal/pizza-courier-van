package se.lightside.pizzacv.ui.restaurants.menu

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pizzacv.common.ui.toLiveData
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.comparePizzaCategory
import se.lightside.pizzacv.ui.cart.ShoppingCart
import javax.inject.Inject

data class RestaurantInfo(val restaurantId: Long,
                          val name: String)

class MenuListViewModel @Inject constructor(
        private val pizzaApi: PizzaApi
) : ViewModel() {

    val menuList = MutableLiveData<List<PizzaMenuEntry>>()

    val shoppingCart = MutableLiveData<ShoppingCart>()

    init {
        shoppingCart.value = ShoppingCart()
    }

    fun loadMenuForRestaurant(restaurantId: Long) {
        pizzaApi.getRestaurantMenu(restaurantId)
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {
                    menuList.postValue(mapToEntries(it))
                })
    }

    fun restaurantInfo(restaurantId: Long) : LiveData<RestaurantInfo> =
            pizzaApi.getSingleRestaurant(restaurantId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { RestaurantInfo(
                            restaurantId = it.id,
                            name = it.name)
                    }.toFlowable()
                    .toLiveData()

    fun sendOrder(restaurantId: Long): Single<PizzaApi.PizzaOrder> =
            sendOrder(restaurantId, shoppingCart.value?.itemMap?.asIterable()?.map {
                PizzaApi.OrderItem(
                        menuItemId = it.key.id,
                        quantity = it.value)
            } ?: emptyList())

    fun sendOrder(restaurantId: Long, orderItems: List<PizzaApi.OrderItem>): Single<PizzaApi.PizzaOrder> =
            pizzaApi.createOrder(PizzaApi.PizzaOrderRequest(
                    restaurantId = restaurantId,
                    cart = orderItems))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    private fun mapToEntries(apiMenuItems: List<PizzaApi.PizzaMenuItem>): List<PizzaMenuEntry> {
        val entries = apiMenuItems.map {
            PizzaMenuItemEntry(
                    id = it.id,
                    category = it.category ?: "unknown",
                    name = it.name ?: "unknown",
                    toppings = it.topping ?: emptyList(),
                    price = it.price,
                    rank = it.rank ?: -1)
        }
        val sections = entries.groupBy { it.category }

        val out = mutableListOf<PizzaMenuEntry>()
        sections.keys.sortedBy { comparePizzaCategory(it) }.forEach { category ->
            sections[category]?.let {
                if (!it.isEmpty()) {
                    out.add(PizzaMenuSectionHeader(title = category))
                    out.addAll(it.sortedBy { it.rank })
                }
            }
        }
        return out
    }

    private fun addItemToShoppingCart(item: PizzaMenuItemEntry) {
        shoppingCart.value = shoppingCart.value?.withItem(item) ?: ShoppingCart(items = listOf(item))
    }

    fun listenForMenuClicks(menuItemClicks: PublishSubject<PizzaMenuItemEntry>) {
        menuItemClicks.subscribe({
            addItemToShoppingCart(it)
        })
    }
}
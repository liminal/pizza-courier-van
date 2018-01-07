package se.lightside.pizzacv.ui.order

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.ui.cart.ShoppingCart
import javax.inject.Inject

class OrderViewModel @Inject constructor(
        private val pizzaApi: PizzaApi
) : ViewModel() {

    fun sendOrder(restaurantId: Long, orderItems: List<PizzaApi.OrderItem>): Single<PizzaApi.PizzaOrder> =
            pizzaApi.createOrder(PizzaApi.PizzaOrderRequest(
                    restaurantId = restaurantId,
                    cart = orderItems))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
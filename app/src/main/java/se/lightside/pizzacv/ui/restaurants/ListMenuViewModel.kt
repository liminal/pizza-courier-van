package se.lightside.pizzacv.ui.restaurants

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import se.lightside.pizza.api.PizzaApi
import javax.inject.Inject

class ListMenuViewModel @Inject constructor(
        val pizzaApi: PizzaApi
): ViewModel() {

    val menuList = MutableLiveData<List<PizzaApi.PizzaMenuItem>>()

    fun loadMenuForRestaurant(restaurantId: Long) {
        pizzaApi.getRestaurantMenu(restaurantId)
                .subscribeOn(Schedulers.io())
                .subscribe( Consumer{
                    menuList.postValue(it)

                })
    }
}
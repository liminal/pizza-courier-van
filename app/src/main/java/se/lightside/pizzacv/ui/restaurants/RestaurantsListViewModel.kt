package se.lightside.pizzacv.ui.restaurants

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import se.lightside.pizza.api.PizzaApi
import javax.inject.Inject

class RestaurantsListViewModel @Inject constructor(
        val pizzaApi: PizzaApi
): ViewModel() {

    val restaurantList = MutableLiveData<List<PizzaApi.PizzaRestaurant>>()

    init {
        pizzaApi.getListOfRestaurants()
                .subscribeOn(Schedulers.io())
                .subscribe( Consumer{
                    restaurantList.postValue(it)

                })
    }
}
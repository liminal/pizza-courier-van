package se.lightside.pizzacv.ui.restaurants

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.geo.GeoPoint
import timber.log.Timber
import javax.inject.Inject

data class UserPosition(
        val geoPoint: GeoPoint)

class RestaurantsListViewModel @Inject constructor(
        val pizzaApi: PizzaApi
) : ViewModel() {

    private val userLocationSubject = BehaviorSubject.createDefault(UserPosition(GeoPoint.ZERO)).toSerialized()

    val restaurantList = MutableLiveData<List<RestaurantListEntry>>()

    init {
        Observables.combineLatest(
                userLocationSubject,
                pizzaApi.getListOfRestaurants()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable())
                .doOnNext({ Timber.v("tick: %s", it) })
                .map { (pos, restaurants) -> orderByDistanceAndGroup(pos, restaurants) }
                .doOnNext({ Timber.v("tock: %s", it) })
                .subscribe({
                    restaurantList.postValue(it)
                })
    }

    fun setLocation(location: Location?) {
        userLocationSubject.onNext(
                UserPosition(
                        GeoPoint.from(lat = location?.latitude, lng = location?.longitude)))
    }

    private fun orderByDistanceAndGroup(pos: UserPosition, restaurants: List<PizzaApi.PizzaRestaurant>): List<RestaurantListEntry> {
        if (restaurants.isEmpty()) {
            return listOf(RestaurantHeaderEntry("No restaurants found"))
        }
        val restaurantList = restaurants.map {
            RestaurantEntry(
                    restaurant = it,
                    distanceInMeters = pos.geoPoint.distanceTo(it.geoPoint()))
        }
        if (pos.geoPoint.isZeroPoint()) {
            return listOf(RestaurantHeaderEntry("Restaurants")) + restaurantList
        } else {
            val sortedList = restaurantList.sortedBy { it.distanceInMeters }
            return listOf(RestaurantHeaderEntry("Closest restaurant")) +
                    sortedList.first().copy(isClosest = true) +
                    RestaurantHeaderEntry("Other restaurants") +
                    sortedList.drop(1)
        }
    }


    private fun PizzaApi.PizzaRestaurant.geoPoint() = GeoPoint.from(latitude, longitude)
}
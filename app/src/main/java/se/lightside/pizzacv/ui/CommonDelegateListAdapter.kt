package se.lightside.pizzacv.ui

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import se.lightside.pizzacv.ui.restaurants.MenuAdapterDelegate
import se.lightside.pizzacv.ui.restaurants.RestaurantAdapterDelegate

class CommonDelegateListAdapter : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(RestaurantAdapterDelegate())
                .addDelegate(MenuAdapterDelegate())
    }
}
package se.lightside.pizzacv.ui.restaurants

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.R
import se.lightside.pizzacv.ui.common.HeaderViewHolder
import se.lightside.pizzacv.ui.restaurants.menu.MenuListActivity

sealed class RestaurantListEntry

data class RestaurantHeaderEntry(val title: String): RestaurantListEntry()

data class RestaurantEntry(
        val restaurant: PizzaApi.PizzaRestaurant,
        val distanceInMeters: Int? = null,
        val isClosest: Boolean = false
) : RestaurantListEntry()

class RestaurantListAdapter : ListDelegationAdapter<List<RestaurantListEntry>>() {

    init {
        delegatesManager.addDelegate(RestaurantHeaderAdapterDelegate())
                .addDelegate(ClosestRestaurantAdapterDelegate())
                .addDelegate(NormalRestaurantAdapterDelegate())
    }

}

class RestaurantHeaderAdapterDelegate : AbsListItemAdapterDelegate<RestaurantHeaderEntry, RestaurantListEntry, HeaderViewHolder>() {
    override fun isForViewType(item: RestaurantListEntry, items: MutableList<RestaurantListEntry>, position: Int): Boolean =
            item is RestaurantHeaderEntry

    override fun onCreateViewHolder(parent: ViewGroup): HeaderViewHolder =
            HeaderViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.header_listitem, parent, false))

    override fun onBindViewHolder(item: RestaurantHeaderEntry, viewHolder: HeaderViewHolder, payloads: MutableList<Any>) {
        viewHolder.title.text = item.title
    }
}

class ClosestRestaurantAdapterDelegate : AbsRestaurantAdapterDelegate() {
    override fun isForViewType(item: RestaurantListEntry, items: MutableList<RestaurantListEntry>, position: Int): Boolean =
            item is RestaurantEntry && item.isClosest

    override fun onCreateViewHolder(parent: ViewGroup): RestaurantViewHolder =
            RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_closest_listitem, parent, false))
}


class NormalRestaurantAdapterDelegate : AbsRestaurantAdapterDelegate() {
    override fun isForViewType(item: RestaurantListEntry, items: MutableList<RestaurantListEntry>, position: Int): Boolean =
            item is RestaurantEntry && !item.isClosest

    override fun onCreateViewHolder(parent: ViewGroup): RestaurantViewHolder =
            RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_listitem, parent, false))
}



abstract class AbsRestaurantAdapterDelegate : AbsListItemAdapterDelegate<RestaurantEntry, RestaurantListEntry, RestaurantViewHolder>() {

    override fun onBindViewHolder(item: RestaurantEntry, viewHolder: RestaurantViewHolder, payloads: MutableList<Any>) {

        val restaurant = item.restaurant

        viewHolder.restaurantName.text = restaurant.name
        viewHolder.restaurantAddress1.text = restaurant.address1
        viewHolder.restaurantAddress2.text = restaurant.address2
        viewHolder.restaurantDistance.text = formatDistanceStr(item.distanceInMeters)
        viewHolder.restaurantCard.setOnClickListener { view ->
            view.context.startActivity(
                    MenuListActivity.Builder.newIntent(view.context, restaurant.id))
        }
    }

    private fun formatDistanceStr(distanceInMeters: Int?): String =
            when {
                distanceInMeters ?: 0 < 1 -> ""
                else -> "${distanceInMeters}m"
            }

}

class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val restaurantCard: CardView = itemView.findViewById<CardView>(R.id.restaurant_card)
    val restaurantName: TextView = itemView.findViewById<TextView>(R.id.restaurant_name)
    val restaurantAddress1: TextView = itemView.findViewById<TextView>(R.id.restaurant_address1)
    val restaurantAddress2: TextView = itemView.findViewById<TextView>(R.id.restaurant_address2)
    val restaurantDistance: TextView = itemView.findViewById<TextView>(R.id.restaurant_distance)
}
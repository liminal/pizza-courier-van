package se.lightside.pizzacv.ui.restaurants

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.R

class RestaurantAdapterDelegate : AbsListItemAdapterDelegate<PizzaApi.PizzaRestaurant, Any, RestaurantViewHolder>() {
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is PizzaApi.PizzaRestaurant
    }

    override fun onCreateViewHolder(parent: ViewGroup): RestaurantViewHolder {
        return RestaurantViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.restaurant_listitem, parent, false))
    }

    override fun onBindViewHolder(item: PizzaApi.PizzaRestaurant, viewHolder: RestaurantViewHolder, payloads: MutableList<Any>) {

        viewHolder.text.text = item.toString()
        viewHolder.text.setOnClickListener { view ->
            view.context.startActivity(
                    ListMenuActivity.Builder.newIntent(view.context, item.id))
        }
    }

}


class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val text: TextView = itemView.findViewById<TextView>(R.id.text)
}
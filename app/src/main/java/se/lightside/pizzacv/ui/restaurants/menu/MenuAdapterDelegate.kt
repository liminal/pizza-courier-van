package se.lightside.pizzacv.ui.restaurants.menu

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.R

class MenuAdapterDelegate : AbsListItemAdapterDelegate<PizzaApi.PizzaMenuItem, Any, PizzaMenuItemViewHolder>() {
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is PizzaApi.PizzaMenuItem

    override fun onCreateViewHolder(parent: ViewGroup): PizzaMenuItemViewHolder =
            PizzaMenuItemViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.pizzamenuitem_listitem, parent, false))

    override fun onBindViewHolder(item: PizzaApi.PizzaMenuItem, viewHolder: PizzaMenuItemViewHolder, payloads: MutableList<Any>) {

        viewHolder.pizzamenuitemName.text = item.toString()
    }

}


class PizzaMenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val pizzamenuitemCard: CardView = itemView.findViewById<CardView>(R.id.pizzamenuitem_card)
    val pizzamenuitemName: TextView = itemView.findViewById<TextView>(R.id.pizzamenuitem_name)
}
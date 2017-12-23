package se.lightside.pizzacv.ui.restaurants

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import se.lightside.pizza.api.PizzaApi
import se.lightside.pizzacv.R

class MenuAdapterDelegate : AbsListItemAdapterDelegate<PizzaApi.PizzaMenuItem, Any, MenuViewHolder>() {
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is PizzaApi.PizzaMenuItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): MenuViewHolder {
        return MenuViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.restaurant_listitem, parent, false))
    }

    override fun onBindViewHolder(item: PizzaApi.PizzaMenuItem, viewHolder: MenuViewHolder, payloads: MutableList<Any>) {

        viewHolder.text.text = item.toString()
    }

}


class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val text: TextView = itemView.findViewById<TextView>(R.id.text)
}
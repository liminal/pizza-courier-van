package se.lightside.pizzacv.ui.restaurants.menu

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import io.reactivex.subjects.PublishSubject
import se.lightside.pizzacv.R

sealed class PizzaMenuEntry

data class PizzaMenuSectionHeader(val title: String) : PizzaMenuEntry()

data class PizzaMenuItemEntry(
        val id: Long,
        val category: String,
        val name: String,
        val toppings: List<String> = emptyList(),
        val price: Int,
        val rank: Int
): PizzaMenuEntry()

class PizzaMenuAdapter : ListDelegationAdapter<List<PizzaMenuEntry>>() {

    val entryDelegate = PizzaMenuItemEntryDelegate()

    init {
        delegatesManager.addDelegate(PizzaMenuSectionHeaderAdapterDelegate())
                .addDelegate(entryDelegate)
    }
}

class PizzaMenuSectionHeaderAdapterDelegate : AbsListItemAdapterDelegate<PizzaMenuSectionHeader, PizzaMenuEntry, PizzaMenuSectionHeaderViewHolder>() {
    override fun isForViewType(item: PizzaMenuEntry, items: MutableList<PizzaMenuEntry>, position: Int): Boolean =
            item is PizzaMenuSectionHeader

    override fun onCreateViewHolder(parent: ViewGroup): PizzaMenuSectionHeaderViewHolder =
            PizzaMenuSectionHeaderViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.pizzamenuitem_header, parent, false))

    override fun onBindViewHolder(item: PizzaMenuSectionHeader, viewHolder: PizzaMenuSectionHeaderViewHolder, payloads: MutableList<Any>) {
        viewHolder.title.text = item.title
    }
}

class PizzaMenuSectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById<TextView>(R.id.pizzamenusection_title)
}

class PizzaMenuItemEntryDelegate : AbsListItemAdapterDelegate<PizzaMenuItemEntry, PizzaMenuEntry, PizzaMenuItemEntryViewHolder>() {

    val menuItemClicks : PublishSubject<PizzaMenuItemEntry> = PublishSubject.create()

    override fun isForViewType(item: PizzaMenuEntry, items: MutableList<PizzaMenuEntry>, position: Int): Boolean =
            item is PizzaMenuItemEntry

    override fun onCreateViewHolder(parent: ViewGroup): PizzaMenuItemEntryViewHolder =
            PizzaMenuItemEntryViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.pizzamenuitem_listitem, parent, false))

    override fun onBindViewHolder(item: PizzaMenuItemEntry, viewHolder: PizzaMenuItemEntryViewHolder, payloads: MutableList<Any>) {
        viewHolder.pizzamenuitemName.text = item.name
        viewHolder.pizzamenuitemPrice.text = "${item.price}:-"
        viewHolder.pizzamenuitemToppings.text = item.toppings.joinToString(separator = ", ")

        viewHolder.pizzamenuitemCard.setOnClickListener {
            menuItemClicks.onNext(item)
        }
    }

}


class PizzaMenuItemEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val pizzamenuitemCard: CardView = itemView.findViewById<CardView>(R.id.pizzamenuitem_card)
    val pizzamenuitemName: TextView = itemView.findViewById<TextView>(R.id.pizzamenuitem_name)
    val pizzamenuitemToppings: TextView = itemView.findViewById<TextView>(R.id.pizzamenuitem_toppings)
    val pizzamenuitemPrice: TextView = itemView.findViewById<TextView>(R.id.pizzamenuitem_price)
}
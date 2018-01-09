package pizzacv.common.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate


object PlaceholderFallbackDelegate : AbsListItemAdapterDelegate<Any, Any, PlaceholderViewHolder>() {
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): PlaceholderViewHolder =
            PlaceholderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.fallback_placeholder, parent, false))

    override fun onBindViewHolder(item: Any, viewHolder: PlaceholderViewHolder, payloads: MutableList<Any>) {
        viewHolder.text.text = item.toString()
    }
}

class PlaceholderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val text : TextView
        get() = itemView.findViewById<TextView>(R.id.text)
}
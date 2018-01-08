package se.lightside.pizzacv.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import se.lightside.pizzacv.R

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById<TextView>(R.id.header_title)
}

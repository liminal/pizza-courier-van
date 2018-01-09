package se.lightside.pizzacv.ui.cart

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotterknife.bindView
import se.lightside.pizzacv.R
import pizzacv.common.ui.bottomSheetBehavior
import se.lightside.pizzacv.ui.restaurants.menu.PizzaMenuItemEntry
import se.lightside.pizzacv.ui.restaurants.menu.PizzaMenuItemEntryViewHolder

class ShoppingCartWidget @JvmOverloads constructor(
        ctx: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : FrameLayout(ctx, attrs, defStyle) {

    val itemCount: TextView by bindView(R.id.shopping_cart_count)
    val itemTotal: TextView by bindView(R.id.shopping_cart_total)
    val cartButton: TextView by bindView(R.id.shopping_cart_btn)
    val orderList: LinearLayout by bindView(R.id.order_list)
    val sendOrderButton: Button by bindView(R.id.btn_send_order)

    var cartIsExpanded = false

    init {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.ShoppingCartWidget, defStyle, 0)

        a.recycle()

        inflate(ctx, R.layout.shopping_cart_widget, this)

        cartButton.setOnClickListener {
            cartIsExpanded = !cartIsExpanded
            cartButton.setText(if (cartIsExpanded) R.string.cart_hide_cart else R.string.cart_show_cart)
            updateViews()
        }
    }

    var shoppingCart: ShoppingCart? = null
        set(value) {
            field = value
            updateViews()
        }

    private fun updateViews() {
        (shoppingCart ?: ShoppingCart()).let {

            renderOrderEntries(it.itemMap)

            val behavior = this.bottomSheetBehavior()

            behavior.state = when {
                cartIsExpanded == true -> BottomSheetBehavior.STATE_EXPANDED
                it.totalPrice < 1 -> BottomSheetBehavior.STATE_HIDDEN
                else -> BottomSheetBehavior.STATE_COLLAPSED
            }

            itemCount.text = context.getString(R.string.cart_total_count, it.totalCount)
            itemTotal.text = context.getString(R.string.cart_total_price, it.totalPrice)
        }

    }

    private fun renderOrderEntries(cartEntries: Map<PizzaMenuItemEntry, Int>) {
        val inflater = LayoutInflater.from(context)
        orderList.removeAllViews()
        cartEntries.entries.sortedWith(Comparator { o1, o2 -> o1.key.compareTo(o2.key) })
                .forEach { (entry, count) ->
                    val itemView = inflater.inflate(R.layout.pizzamenuitem_listitem, orderList, false)
                    PizzaMenuItemEntryViewHolder(itemView).render(entry, count)
                    orderList.addView(itemView)
                }
        requestLayout()
    }


}

package se.lightside.pizzacv.ui.restaurants.menu

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import kotterknife.bindView
import pizzacv.common.ui.ViewModelPizzaCourierVanActivity
import pizzacv.common.ui.viewmodel.observeNotNull
import se.lightside.pizzacv.R
import se.lightside.pizzacv.ui.cart.ShoppingCartWidget
import se.lightside.pizzacv.ui.order.OrderActivity

class MenuListActivity : ViewModelPizzaCourierVanActivity<MenuListViewModel>() {

    object Builder {
        private const val EXTRA_RESTAURANT_ID = "extra_restaurantId"

        fun newIntent(ctx: Context, restaurantId: Long): Intent =
                Intent(ctx, MenuListActivity::class.java).apply {
                    putExtra(EXTRA_RESTAURANT_ID, restaurantId)
                }

        internal fun injectExtras(activity: MenuListActivity) {
            activity.restaurantId = activity.intent.extras.getLong(EXTRA_RESTAURANT_ID)
        }

    }

    var restaurantId: Long = -1L


    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val shoppingCart: ShoppingCartWidget by bindView(R.id.shopping_cart)

    private val adapter = PizzaMenuAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Builder.injectExtras(this)
        model = getViewModel(MenuListViewModel::class.java)

        setContentView(R.layout.restaurant_menu_list_activity)

        recyclerView.adapter = adapter


        // If we were working with larger menus we might want to use DiffUtil but for this api
        // this will do fine.
        model.menuList.observeNotNull(this, {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        model.restaurantInfo(restaurantId).observeNotNull(this, {
            title = it.name
        })

        model.loadMenuForRestaurant(restaurantId)

        model.listenForMenuClicks(adapter.entryDelegate.menuItemClicks)

        model.shoppingCart.observeNotNull(this) {
            shoppingCart.shoppingCart = it
        }

        shoppingCart.sendOrderButton.setOnClickListener {
            var pd: ProgressDialog? = null

            model.sendOrder(restaurantId)
                    .doOnSubscribe({
                        pd = ProgressDialog.show(this,
                                getString(R.string.order_progress_title),
                                getString(R.string.order_progress_message),
                                true)
                    })
                    .doAfterTerminate { pd?.hide() }
                    .subscribe({ orderDetails ->
                        startActivity(OrderActivity.Builder.newIntent(this, orderDetails.orderId))

                    },
                            { Snackbar.make(recyclerView, R.string.order_failure_message, Snackbar.LENGTH_INDEFINITE).show() })
        }


    }

}

package se.lightside.pizzacv.ui.restaurants.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import kotterknife.bindView
import pizzacv.common.ui.ViewModelPizzaCourierVanActivity
import pizzacv.common.ui.viewmodel.observeNotNull
import se.lightside.pizzacv.R

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

    val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    val adapter = PizzaMenuAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Builder.injectExtras(this)
        model = getViewModel(MenuListViewModel::class.java)

        setContentView(R.layout.activity_list_restaurants)

        recyclerView.adapter = adapter


        model.menuList.observeNotNull(this, {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        model.restaurantInfo(restaurantId).observeNotNull(this, {
            title = it.name
        })

        model.loadMenuForRestaurant(restaurantId)

        model.listenForMenuClicks(adapter.entryDelegate.menuItemClicks)

    }
}

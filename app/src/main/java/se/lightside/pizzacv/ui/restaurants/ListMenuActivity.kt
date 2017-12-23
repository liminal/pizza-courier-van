package se.lightside.pizzacv.ui.restaurants

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import se.lightside.pizzacv.R
import se.lightside.pizzacv.ui.CommonDelegateListAdapter
import javax.inject.Inject

class ListMenuActivity : DaggerAppCompatActivity() {

    object Builder {
        private const val EXTRA_RESTAURANT_ID = "extra_restaurantId"

        fun newIntent(ctx: Context, restaurantId: Long) : Intent {
            return Intent(ctx, ListMenuActivity::class.java).apply {
                putExtra(EXTRA_RESTAURANT_ID, restaurantId)
            }
        }

        internal fun injectExtras(activity: ListMenuActivity) {
            activity.restaurantId = activity.intent.extras.getLong(EXTRA_RESTAURANT_ID)
        }

    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var model: ListMenuViewModel

    var restaurantId: Long = -1L

    val recyclerView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    val adapter = CommonDelegateListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory)
                .get(ListMenuViewModel::class.java)

        setContentView(R.layout.activity_list_restaurants)

        recyclerView.adapter = adapter


        model.menuList.observe(this, Observer {
            it?.let {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
        })

        model.loadMenuForRestaurant(restaurantId)

    }
}

package se.lightside.pizzacv.ui.restaurants

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import kotterknife.bindView
import pizzacv.common.ui.ViewModelPizzaCourierVanActivity
import pizzacv.common.ui.viewmodel.observeNotNull
import se.lightside.pizzacv.R
import se.lightside.pizzacv.ui.CommonDelegateListAdapter

class RestaurantsListActivity : ViewModelPizzaCourierVanActivity<RestaurantsListViewModel>() {

    val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    val adapter = CommonDelegateListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = getViewModel(RestaurantsListViewModel::class.java)

        setContentView(R.layout.activity_list_restaurants)

        recyclerView.adapter = adapter

        model.restaurantList.observeNotNull(this, {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

    }
}

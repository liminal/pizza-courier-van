package se.lightside.pizzacv.ui.restaurants

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import se.lightside.pizzacv.R
import se.lightside.pizzacv.ui.CommonDelegateListAdapter
import javax.inject.Inject

class ListRestaurantsActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var model: ListRestaurantsViewModel

    val recyclerView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    val adapter = CommonDelegateListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory)
                .get(ListRestaurantsViewModel::class.java)

        setContentView(R.layout.activity_list_restaurants)

        recyclerView.adapter = adapter


        model.restaurantList.observe(this, Observer {
            it?.let {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
        })

    }
}

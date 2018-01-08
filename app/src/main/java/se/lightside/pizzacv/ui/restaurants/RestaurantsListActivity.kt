package se.lightside.pizzacv.ui.restaurants

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.tbruyelle.rxpermissions2.RxPermissions
import kotterknife.bindView
import pizzacv.common.ui.ViewModelPizzaCourierVanActivity
import pizzacv.common.ui.viewmodel.observeNotNull
import se.lightside.pizzacv.R
import timber.log.Timber
import javax.inject.Inject

class RestaurantsListActivity : ViewModelPizzaCourierVanActivity<RestaurantsListViewModel>() {

    @Inject lateinit var locationClient : FusedLocationProviderClient
    @Inject lateinit var rxPermissions: RxPermissions

    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    private val adapter = RestaurantListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        model = getViewModel(RestaurantsListViewModel::class.java)

        setContentView(R.layout.restaurant_list_activity)
        title = "List of pizza places"

        recyclerView.adapter = adapter

        model.restaurantList.observeNotNull(this, {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({
                    permissionGranted -> if (permissionGranted) { updateModelWithLastLocation() }
                })


    }

    @SuppressLint("MissingPermission")
    private fun updateModelWithLastLocation() {
        locationClient.lastLocation.addOnSuccessListener(this, {
            Timber.v("onSuccess(%s)", it)
            model.setLocation(it)
        }).addOnFailureListener(this, { Timber.e(it, "On Location Failure")})

    }

}

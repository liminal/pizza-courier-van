package se.lightside.pizzacv.geo

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import timber.log.Timber


object BoundLocationManager {

    fun LifecycleOwner.bindLocationListener(context: Context, locationListener: LocationListener) {
        BoundLocationListener(this, locationListener, context)
    }

    internal class BoundLocationListener(
            lifecycleOwner: LifecycleOwner,
            private val locationListener: LocationListener,
            private val context: Context
    ) : LifecycleObserver {
        private var locationManager: LocationManager? = null

        init {
            lifecycleOwner.lifecycle.addObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun addLocationListener() {
            // Note: Use the Fused Location Provider from Google Play Services instead.
            // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            this.locationManager = locationManager
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                Timber.d("Listener added")

                // Force an update with the last location, if available.
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                    locationListener.onLocationChanged(it)
                }
            } catch (error: SecurityException) {
                Timber.v(error, "SecurityException getting location")
            }
        }


        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun removeLocationListener() {
            locationManager?.let {
                locationManager = null
                it.removeUpdates(locationListener)
                Timber.d( "Listener removed")
            }
        }
    }
}

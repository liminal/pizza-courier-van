package se.lightside.pizzacv.geo

import android.location.Location
import kotlin.math.roundToInt

data class GeoPoint(val latitude: Double, val longitude: Double) {

    fun isZeroPoint() = latitude == 0.0 && longitude == 0.0

    fun distanceTo(that: GeoPoint) : Int? {
        if (this.isZeroPoint() || that.isZeroPoint()) return null
        val results = FloatArray(2)
        Location.distanceBetween(this.latitude, this.longitude, that.latitude, that.longitude, results)
        return results.getOrNull(0)?.roundToInt()

    }

    companion object {
        val ZERO = GeoPoint(0.0, 0.0)

        fun from(lat: Double?, lng: Double?) : GeoPoint =
            when {
                (lat == null || lng == null) -> ZERO
                else -> GeoPoint(latitude = lat, longitude = lng)
            }
    }
}
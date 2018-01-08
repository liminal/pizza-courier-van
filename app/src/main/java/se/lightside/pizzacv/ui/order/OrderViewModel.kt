package se.lightside.pizzacv.ui.order

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import pizzacv.common.ui.toLiveData
import se.lightside.pizza.api.PizzaApi
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class OrderState(
        val details: PizzaApi.PizzaOrderDetails,
        val estimatedArrivalTime: String
)

class OrderViewModel @Inject constructor(
        private val pizzaApi: PizzaApi
) : ViewModel() {


    fun orderStatus(orderId: Long) : LiveData<OrderState> {
        return pizzaApi.getOrderDetails(orderId)
                .subscribeOn(Schedulers.io())
                .flatMapObservable {
            order -> Observable.interval(1, TimeUnit.SECONDS)
                .map { OrderState(
                        details = order,
                        estimatedArrivalTime = formatDuration(Duration.between(Instant.now(), Instant.parse(order.esitmatedDelivery))))
                }
        }.observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.LATEST)
                .doOnError({ Timber.e(it, "orderDetailsError")})
                .toLiveData()
    }

    private fun formatDuration(duration: Duration): String {
        val secs = Math.abs(duration.seconds)

        val ss = secs %60
        val mm = (secs / 60) % 60
        val hh = secs / 3600

        val timeStr = String.format(Locale.US, "%02d:%02d:%02d", hh, mm, ss)

        return when {
            duration.isNegative -> "-$timeStr"
            else -> timeStr
        }
    }


}
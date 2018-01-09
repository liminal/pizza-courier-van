package se.lightside.pizzacv.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.TimeUtils.formatDuration
import android.widget.TextView
import kotterknife.bindView
import org.threeten.bp.Duration
import pizzacv.common.ui.ViewModelPizzaCourierVanActivity
import pizzacv.common.ui.viewmodel.observeNotNull
import se.lightside.pizzacv.R

class OrderActivity : ViewModelPizzaCourierVanActivity<OrderViewModel>() {

    object Builder {
        private val EXTRA_ORDER_ID = "extra_orderId"

        fun newIntent(ctx: Context, orderId: Long) : Intent =
                Intent(ctx, OrderActivity::class.java).apply {
                    putExtra(EXTRA_ORDER_ID, orderId)
                }

        internal fun inject(activity: OrderActivity) {
            activity.orderId = activity.intent.getLongExtra(EXTRA_ORDER_ID, -1L)
        }
    }

    var orderId : Long = -1L

    private val arrivalTimer : TextView by bindView(R.id.arrival_time)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Builder.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        model = getViewModel(OrderViewModel::class.java)

        setContentView(R.layout.order_activity)

        model.orderStatus(orderId).observeNotNull(this) { state ->
            arrivalTimer.text = state.estimatedArrivalTime
        }
    }

}

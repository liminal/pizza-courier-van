package se.lightside.pizzacv.ui.order

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pizzacv.common.ui.ViewModelPizzaCourierVanActivity
import se.lightside.pizzacv.R

class OrderActivity : ViewModelPizzaCourierVanActivity<OrderViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_activity)
    }
}

package se.lightside.pizzacv.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import dagger.android.support.DaggerAppCompatActivity
import se.lightside.pizzacv.R
import se.lightside.pizzacv.ui.restaurants.ListRestaurantsActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_list_restaurants).setOnClickListener {
            startActivity(Intent(this, ListRestaurantsActivity::class.java))
        }
    }
}

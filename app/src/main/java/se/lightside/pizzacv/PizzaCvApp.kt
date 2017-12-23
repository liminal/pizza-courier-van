package se.lightside.pizzacv

import android.app.Application
import timber.log.Timber

class PizzaCvApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

}
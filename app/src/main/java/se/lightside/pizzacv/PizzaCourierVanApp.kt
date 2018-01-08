package se.lightside.pizzacv

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import se.lightside.pizzacv.di.DaggerPizzaCourierVanAppComponent
import timber.log.Timber

class PizzaCourierVanApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this);
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerPizzaCourierVanAppComponent.builder().create(this)

}
package se.lightside.pizzacv

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

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerPizzaCourierVanAppComponent.builder().create(this)

}
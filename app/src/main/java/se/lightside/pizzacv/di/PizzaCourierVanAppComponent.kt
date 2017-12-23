package se.lightside.pizzacv.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import okhttp3.logging.HttpLoggingInterceptor
import se.lightside.pizzacv.BuildConfig
import se.lightside.pizzacv.PizzaCvApp
import se.lightside.pizzacv.ui.MainActivity
import se.lightside.pizzacv.ui.restaurants.ListMenuActivity
import se.lightside.pizzacv.ui.restaurants.ListMenuViewModel
import se.lightside.pizzacv.ui.restaurants.ListRestaurantsActivity
import se.lightside.pizzacv.ui.restaurants.ListRestaurantsViewModel
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component(modules = [PizzaCourierVanAppModule::class])
interface PizzaCourierVanAppComponent : AndroidInjector<PizzaCvApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PizzaCvApp>() {
        abstract override fun build(): PizzaCourierVanAppComponent
    }


}

@Module(includes = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ViewModelModule::class,
    NetModule::class,
    PizzaCvActivitiesModule::class
])
object PizzaCourierVanAppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideApplication(app: PizzaCvApp): Application = app

    @Singleton
    @Provides
    @JvmStatic
    fun provideCacheDir(app: Application): CacheDir = CacheDir(app.cacheDir)

    @Singleton
    @Provides
    @JvmStatic
    fun provideHttpLogger(): HttpLoggingInterceptor.Logger =
            HttpLoggingInterceptor.Logger { message -> Timber.v(message) }

    @Singleton
    @Provides
    @JvmStatic
    fun provideHttpLogLevel(): HttpLoggingInterceptor.Level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

}

@Module
abstract class PizzaCvActivitiesModule {

    @ContributesAndroidInjector
    abstract fun provideMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    abstract fun provideListRestaurantsActivityInjector(): ListRestaurantsActivity

    @ContributesAndroidInjector
    abstract fun provideListMenuActivityInjector(): ListMenuActivity

}

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ListRestaurantsViewModel::class)
    abstract fun bindListRestaurantsViewModel(model: ListRestaurantsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListMenuViewModel::class)
    abstract fun bindListMenuViewModel(model: ListMenuViewModel): ViewModel

}
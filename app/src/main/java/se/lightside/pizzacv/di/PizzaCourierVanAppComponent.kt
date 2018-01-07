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
import pizzacv.common.ui.viewmodel.ViewModelFactory
import pizzacv.common.ui.viewmodel.ViewModelKey
import se.lightside.pizzacv.BuildConfig
import se.lightside.pizzacv.PizzaCourierVanApp
import se.lightside.pizzacv.ui.MainActivity
import se.lightside.pizzacv.ui.restaurants.menu.MenuListActivity
import se.lightside.pizzacv.ui.restaurants.menu.MenuListViewModel
import se.lightside.pizzacv.ui.restaurants.RestaurantsListActivity
import se.lightside.pizzacv.ui.restaurants.RestaurantsListViewModel
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component(modules = [PizzaCourierVanAppModule::class])
interface PizzaCourierVanAppComponent : AndroidInjector<PizzaCourierVanApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PizzaCourierVanApp>() {
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
    fun provideApplication(app: PizzaCourierVanApp): Application = app

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
    abstract fun provideListRestaurantsActivityInjector(): RestaurantsListActivity

    @ContributesAndroidInjector
    abstract fun provideListMenuActivityInjector(): MenuListActivity

}

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantsListViewModel::class)
    abstract fun bindListRestaurantsViewModel(model: RestaurantsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MenuListViewModel::class)
    abstract fun bindListMenuViewModel(model: MenuListViewModel): ViewModel

}
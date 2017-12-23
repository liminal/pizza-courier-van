package se.lightside.pizzacv.di

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.logging.HttpLoggingInterceptor
import se.lightside.pizzacv.BuildConfig
import se.lightside.pizzacv.MainActivity
import se.lightside.pizzacv.PizzaCvApp
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

}
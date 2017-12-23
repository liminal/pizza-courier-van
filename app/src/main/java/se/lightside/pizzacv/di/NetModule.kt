package se.lightside.pizzacv.di

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import se.lightside.pizza.api.PizzaApi
import java.io.File
import javax.inject.Singleton

data class CacheDir(val cacheDir: File)

@Module(includes = [
    OkHttpModule::class,
    MoshiModule::class,
    PizzaApiModule::class
])
object NetModule {

    private val DEFAULT_DISK_CACHE_SIZE = 64 * 1024 * 1024

    @Provides
    @JvmStatic
    fun provideCache(cacheDir: CacheDir): Cache =
            Cache(cacheDir.cacheDir, DEFAULT_DISK_CACHE_SIZE.toLong())

    @Provides
    @JvmStatic
    fun provideConverterFactory(moshiFactory: MoshiConverterFactory): Converter.Factory = moshiFactory

}

@Module
object PizzaApiModule {

    @Provides
    @JvmStatic
    fun provideBaseUrl(): HttpUrl = HttpUrl.parse("https://private-anon-a3a7dacf75-pizzaapp.apiary-mock.com")!!

    @Provides
    @JvmStatic
    fun provideRetrofit(
            baseUrl: HttpUrl,
            okHttpClient: OkHttpClient,
            converterFactory: Converter.Factory
    ): Retrofit =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

    @Provides
    @JvmStatic
    fun provideApi(r: Retrofit): PizzaApi = r.create(PizzaApi::class.java)
}

@Module
object OkHttpModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideBaseOkHttpClient(
            cache: Cache,
            logLevel: HttpLoggingInterceptor.Level,
            logger: HttpLoggingInterceptor.Logger
    ): OkHttpClient {

        val client = OkHttpClient.Builder()

        // Set up disk cache
        client.cache(cache)

        if (logLevel != HttpLoggingInterceptor.Level.NONE) {
            client.addInterceptor(
                    HttpLoggingInterceptor(logger).setLevel(logLevel))
        }

        return client.build()
    }


}

@Module
object MoshiModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideMoshi(): Moshi =
            Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

    @Singleton
    @Provides
    @JvmStatic
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
            MoshiConverterFactory.create(moshi)

}


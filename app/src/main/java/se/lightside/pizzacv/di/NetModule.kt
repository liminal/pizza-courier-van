package se.lightside.pizzacv.di

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

data class CacheDir(val cacheDir: File)

@Module(includes = [OkHttpModule::class, MoshiModule::class])
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


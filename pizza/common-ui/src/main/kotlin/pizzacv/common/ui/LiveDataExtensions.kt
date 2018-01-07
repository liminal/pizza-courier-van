package pizzacv.common.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.reactivestreams.Publisher

fun <T> Publisher<T>.toLiveData(): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this)

fun <T> Observable<T>.toLiveData(strategy: BackpressureStrategy) =
        this.toFlowable(strategy).toLiveData()

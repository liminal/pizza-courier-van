package pizzacv.common.ui.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, observer: (T) -> Unit) =
        this.observe(owner, Observer { it?.let(observer) })
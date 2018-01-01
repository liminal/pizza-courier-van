package pizzacv.common.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.PersistableBundle
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class ViewModelPizzaCourierVanActivity<VM : ViewModel> : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var model : VM



    fun <T: ViewModel> getViewModel(clazz: Class<T>) =
            ViewModelProviders.of(this, viewModelFactory).get(clazz)


}
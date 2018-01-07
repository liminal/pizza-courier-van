package se.lightside.pizzacv.ui

import android.support.design.widget.BottomSheetBehavior
import android.view.View

fun <T : View> T.bottomSheetBehavior() = BottomSheetBehavior.from<T>(this)
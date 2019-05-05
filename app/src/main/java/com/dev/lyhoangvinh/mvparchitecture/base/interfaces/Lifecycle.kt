package com.dev.lyhoangvinh.mvparchitecture.base.interfaces

import android.os.Bundle

interface Lifecycle : Destroyable {
    fun onCreate()
    fun onStart()
    fun onStop()
    fun onSaveInstanceState(outState: Bundle)
    fun onRestoreInstanceState(savedInstanceState: Bundle)
}
package com.dev.lyhoangvinh.mvparchitecture.di.component

import android.arch.lifecycle.LifecycleOwner
import com.dev.lyhoangvinh.mvparchitecture.di.module.FragmentModule
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import dagger.Component

@PerFragment
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun lifeCycleOwner(): LifecycleOwner
}
package com.dev.lyhoangvinh.mvparchitecture.di.component

import android.arch.lifecycle.LifecycleOwner
import com.dev.lyhoangvinh.mvparchitecture.di.module.FragmentModule
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category.CategoriesFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection.CollectionFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home.HomeFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos.VideosFragment
import dagger.Component

@PerFragment
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun lifeCycleOwner(): LifecycleOwner

    fun inject(fragment: CategoriesFragment)

    fun inject(fragment: CollectionFragment)

    fun inject(fragment: VideosFragment)

    fun inject(fragment: HomeFragment)
}
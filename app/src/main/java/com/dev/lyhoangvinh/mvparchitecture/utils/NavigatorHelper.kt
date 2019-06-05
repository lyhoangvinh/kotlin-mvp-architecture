package com.dev.lyhoangvinh.mvparchitecture.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection.CollectionFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail.DetailActivity
import lyhoangvinh.com.myutil.navigation.ActivityNavigator
import lyhoangvinh.com.myutil.navigation.FragmentNavigator
import lyhoangvinh.com.myutil.navigation.Navigator

class NavigatorHelper(private var mNavigator: Navigator) {

    fun NavigatorHelper(mNavigator: FragmentNavigator) {
        this.mNavigator = mNavigator
    }

    fun NavigatorHelper(mNavigator: ActivityNavigator) {
        this.mNavigator = mNavigator
    }

    fun NavigatorHelper(mNavigator: Navigator) {
        this.mNavigator = mNavigator
    }

    fun navigateDetailActivity(url: String) {
        mNavigator.startActivity(DetailActivity::class.java) { intent -> intent.putExtra(Constants.EXTRA_DATA, url) }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("CommitTransaction")
    fun navigateCollectionFragment(keyword: String) {
        val collectionFragment = CollectionFragment()
        val bundle = Bundle()
        bundle.putString(Constants.EXTRA_DATA, keyword)
        collectionFragment.arguments = bundle
        mNavigator.replaceFragmentAndAddToBackStack(R.id.container, collectionFragment, bundle, null)
    }
}
package com.dev.lyhoangvinh.mvparchitecture.utils

import android.content.Intent
import android.os.Bundle
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail.DetailActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.comics.IssuesActivity
import lyhoangvinh.com.myutil.navigation.ActivityNavigator
import lyhoangvinh.com.myutil.navigation.FragmentNavigator
import lyhoangvinh.com.myutil.navigation.Navigator
import lyhoangvinh.com.myutil.navigation.PlainConsumer

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

    fun natigateMainActivity() {
        mNavigator.startActivity(IssuesActivity::class.java)
    }

    fun navigateDetailActivity(url: String) {
        mNavigator.startActivity(DetailActivity::class.java, object : PlainConsumer<Intent> {
            override fun accept(intent: Intent) {
                intent.putExtra(Constants.EXTRA_DATA, url)
            }

        })
    }
}
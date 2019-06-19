package com.dev.lyhoangvinh.mvparchitecture.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.AvgleActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection.CollectionFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail.DetailActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search.SearchActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos.VideosFragment
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

    private fun navigateTransitionActivity(ctx: Activity, cls: Class<*>, finishAct: Boolean) {
        val pairs = TransitionUtil.createSafeTransitionParticipants(ctx, true)
        val intent = Intent(ctx, cls)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(ctx, *pairs)
        ctx.startActivity(intent, options.toBundle())
        if (finishAct)
            mNavigator.finishActivity()
    }

    fun navigateDetailActivity(url: String) {
        mNavigator.startActivity(DetailActivity::class.java) { intent -> intent.putExtra(Constants.EXTRA_DATA, url) }
    }

    fun navigateCollectionFragment(category: Category) {
        val collectionFragment = CollectionFragment()
        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_DATA, category)
        collectionFragment.arguments = bundle
        mNavigator.replaceFragmentAndAddToBackStack(R.id.container, collectionFragment, bundle, null)
    }

    fun navigateVideosFragment(category: Category?) {
        val collectionFragment = VideosFragment()
        if (category != null) {
            val bundle = Bundle()
            bundle.putParcelable(Constants.EXTRA_DATA, category)
            collectionFragment.arguments = bundle
            mNavigator.replaceFragmentAndAddToBackStack(R.id.container, collectionFragment, bundle, null)
        } else {
            mNavigator.replaceFragmentAndAddToBackStack(R.id.container, collectionFragment, null, null)
        }
    }

    fun navigateVideosFragment(collection: Collection?) {
        val collectionFragment = VideosFragment()
        if (collection != null) {
            val bundle = Bundle()
            bundle.putParcelable(Constants.EXTRA_DATA, collection)
            collectionFragment.arguments = bundle
            mNavigator.replaceFragmentAndAddToBackStack(R.id.container, collectionFragment, bundle, null)
        } else {
            mNavigator.replaceFragmentAndAddToBackStack(R.id.container, collectionFragment, null, null)
        }
    }

    fun navigateVideosFragment() {
        mNavigator.replaceFragmentAndAddToBackStack(R.id.container, VideosFragment(), null, null)
    }

    fun navigateAvgleActivity(activity: Activity) {
        navigateTransitionActivity(activity, AvgleActivity::class.java, true)
    }

    fun navigateSearchActivity(activity: Activity) {
        navigateTransitionActivity(activity, SearchActivity::class.java, false)
    }
}
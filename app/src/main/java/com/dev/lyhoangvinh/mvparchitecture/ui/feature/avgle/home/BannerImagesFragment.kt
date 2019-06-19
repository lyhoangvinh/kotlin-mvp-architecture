package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.fragment_banner_images.*
import javax.inject.Inject

class BannerImagesFragment : BaseFragment() {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    companion object {

        fun getInstance(collection: Collection): BannerImagesFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.EXTRA_DATA, collection)
            val fragment = BannerImagesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent()?.inject(this)
    }

    override fun getLayoutResource() = R.layout.fragment_banner_images

    override fun initialize(view: View, ctx: Context?) {
        if (arguments != null) {
            val collection: Collection = arguments?.getParcelable(Constants.EXTRA_DATA)!!
            val coverUrl: String = collection.coverUrl.toString()
//            val url :String = collection.collectionUrl.toString()
            imv.loadImage(coverUrl)
            imv.setOnClickListener {
                navigatorHelper.navigateVideosFragment(collection)
            }
        }
    }
}
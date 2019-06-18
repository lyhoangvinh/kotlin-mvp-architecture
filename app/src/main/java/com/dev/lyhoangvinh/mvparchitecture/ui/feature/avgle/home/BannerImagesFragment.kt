package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.fragment_banner_images.*
import javax.inject.Inject

class BannerImagesFragment : BaseFragment() {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    companion object {

        fun getInstance(url: String): BannerImagesFragment {
            val bundle = Bundle()
            bundle.putString(Constants.EXTRA_DATA, url)
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
            val url: String = arguments?.getString(Constants.EXTRA_DATA).toString()
            imv.loadImage(url)
            imv.setOnClickListener {
                navigatorHelper.navigateDetailActivity(url)
            }
        }
    }
}
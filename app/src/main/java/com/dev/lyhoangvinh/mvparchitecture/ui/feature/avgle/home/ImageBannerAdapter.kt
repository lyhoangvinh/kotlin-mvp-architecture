package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video

class ImageBannerAdapter(fm: FragmentManager, private var mBannerList: List<Video>) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return BannerImagesFragment.getInstance(mBannerList[position].previewUrl.toString())
    }

    override fun getCount(): Int {
        return mBannerList.size
    }
}
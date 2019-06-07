package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category

class ImageBannerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    private var mBannerList: List<Category>? = null

    fun setBanner(mBannerList: List<Category>) {
        this.mBannerList = mBannerList
    }

    override fun getItem(position: Int): Fragment? {
        return BannerImagesFragment.getInstance(mBannerList?.get(position)!!.coverUrl.toString())
    }

    override fun getCount(): Int {
        return mBannerList!!.size
    }
}
package com.dev.lyhoangvinh.mvparchitecture.ui.feature.main

import android.support.v7.util.DiffUtil
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues

class MainDiffCallBack(private val current: List<Issues>, private val next: List<Issues>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return current.size
    }

    override fun getNewListSize(): Int {
        return next.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val currentItem = current[oldItemPosition]
        val nextItem = next[newItemPosition]
        return currentItem.id == nextItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val currentItem = current[oldItemPosition]
        val nextItem = next[newItemPosition]
        return currentItem == nextItem
    }
}


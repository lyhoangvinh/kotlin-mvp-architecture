package com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.Item
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.SwapItem

abstract class BaseItemViewHolder<T : Item>(parent: ViewGroup, resId: Int) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(resId, parent, false)), SwapItem<T> {

    override fun setItem(item: T) {
        this.item = item
    }

    private lateinit var item: T

    init {
        itemView.tag = this
    }
}
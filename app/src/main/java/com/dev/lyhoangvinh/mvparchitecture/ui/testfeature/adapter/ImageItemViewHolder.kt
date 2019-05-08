package com.dev.lyhoangvinh.mvparchitecture.ui.testfeature.adapter

import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.base.adapter.BaseItemViewHolder
import com.dev.lyhoangvinh.mvparchitecture.database.item.ImageItem
import kotlinx.android.synthetic.main.item_text.view.*

class ImageItemViewHolder(parent: ViewGroup, resId: Int) : BaseItemViewHolder<ImageItem>(parent, resId) {

    override fun setItem(item: ImageItem) {
        super.setItem(item)
        val tvName = itemView.tvName
        tvName.text = item.text
    }
}
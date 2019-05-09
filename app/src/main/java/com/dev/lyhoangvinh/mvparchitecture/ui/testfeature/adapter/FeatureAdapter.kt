package com.dev.lyhoangvinh.mvparchitecture.ui.testfeature.adapter

import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.adapter.BaseItemAdapter
import com.dev.lyhoangvinh.mvparchitecture.base.adapter.BaseItemViewHolder
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Item
import com.dev.lyhoangvinh.mvparchitecture.database.item.ImageItem
import com.dev.lyhoangvinh.mvparchitecture.database.item.TextItem

class FeatureAdapter : BaseItemAdapter() {

    companion object {
        val ITEM_TEXT = 0

        val IIEM_IMAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItemAt(position)

        if (item is TextItem)
            return ITEM_TEXT

        if (item is ImageItem)
            return IIEM_IMAGE

        throw RuntimeException("Not support item $item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<Item> {
        if (viewType == ITEM_TEXT)
            return TextItemViewHolder(parent, R.layout.item_text) as BaseItemViewHolder<Item>
        if (viewType == IIEM_IMAGE)
            return ImageItemViewHolder(parent, R.layout.item_text) as BaseItemViewHolder<Item>
        throw RuntimeException("Not support type=$viewType")
    }
}
package com.dev.lyhoangvinh.mvparchitecture.ui.testfeature.adapter

import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.base.adapter.BaseItemViewHolder
import com.dev.lyhoangvinh.mvparchitecture.database.item.TextItem
import kotlinx.android.synthetic.main.item_text.view.*

class TextItemViewHolder(parent: ViewGroup, resId: Int) : BaseItemViewHolder<TextItem>(parent, resId) {

    override fun setItem(item: TextItem) {
        super.setItem(item)
        val tvName = itemView.tvName
        tvName.text = item.text
    }
}
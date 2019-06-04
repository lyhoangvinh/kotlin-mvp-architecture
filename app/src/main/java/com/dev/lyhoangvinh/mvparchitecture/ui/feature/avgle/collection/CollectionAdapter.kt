package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_collection.view.*

class CollectionAdapter : BaseAdapter<Collection, CollectionAdapter.CollectionViewHoler>(ArrayList()) {
    override fun itemLayoutResource() = R.layout.item_collection

    override fun createViewHolder(itemView: View) = CollectionViewHoler(itemView)

    override fun onBindViewHolder(vh: CollectionViewHoler, dto: Collection, position: Int) {
        vh.tvName.text = dto.title
        vh.imv.loadImage(dto.coverUrl.toString())
    }

    class CollectionViewHoler(itemView: View) : BaseViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val imv: ImageView = itemView.imv
    }
}
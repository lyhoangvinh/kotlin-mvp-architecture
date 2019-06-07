package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_collection_2.view.*

class Collection2Adapter :
    BaseAdapter<Collection, Collection2Adapter.CollectionViewHoler>(ArrayList()) {

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (String) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun itemLayoutResource() = R.layout.item_collection_2

    override fun createViewHolder(itemView: View) = CollectionViewHoler(itemView)

    override fun onBindViewHolder(vh: CollectionViewHoler, dto: Collection, position: Int) {
        vh.tvName.text = dto.title
        vh.imv.loadImage(dto.coverUrl.toString())
        vh.tvKeyword.text = dto.keyword
        vh.tvTotalViews.text = String.format("View %s", dto.totalViews)
        vh.tvVideoCount.text = String.format("Video count %s", dto.videoCount)
        vh.itemView.setOnClickListener { onItemClickListener?.invoke(dto.collectionUrl!!) }
    }

    class CollectionViewHoler(itemView: View) : BaseViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val imv: ImageView = itemView.imv
        val tvKeyword: TextView = itemView.tvKeyword
        val tvTotalViews: TextView = itemView.tvTotalViews
        val tvVideoCount: TextView = itemView.tvVideoCount
    }

    fun updateCollection(newList: List<Collection>) {
        update(newList, CollectionDiffCallBack(getData(), newList), false)
    }

    class CollectionDiffCallBack(private val current: List<Collection>, private val next: List<Collection>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return current.size
        }

        override fun getNewListSize(): Int {
            return next.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val currentItem = current[oldItemPosition]
            val nextItem = next[newItemPosition]
            return currentItem.idCLS == nextItem.idCLS
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val currentItem = current[oldItemPosition]
            val nextItem = next[newItemPosition]
            return currentItem == nextItem
        }
    }
}

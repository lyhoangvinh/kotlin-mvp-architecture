package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
 import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_collection_home.view.*

class CollectionHomeAdapter :
    BaseAdapter<Collection, CollectionHomeAdapter.CollectionViewHoler>(ArrayList()) {

    private var onItemClickListener: ((Collection) -> Unit)? = null

    private var mWidth = 0

    private var mHeight = 0

    fun setLayoutParams(mWidth: Int, mHeight: Int): CollectionHomeAdapter {
        this.mWidth = mWidth
        this.mHeight = mHeight
        return this
    }

    fun setOnItemClickListener(onItemClickListener: (Collection) -> Unit): CollectionHomeAdapter {
        this.onItemClickListener = onItemClickListener
        return this
    }

    override fun itemLayoutResource() = R.layout.item_collection_home

    override fun createViewHolder(itemView: View) = CollectionViewHoler(itemView)

    override fun onBindViewHolder(vh: CollectionViewHoler, dto: Collection, position: Int) {
        vh.imv.layoutParams.width = mWidth
        vh.imv.layoutParams.height = mHeight
        vh.lnMain.layoutParams = RelativeLayout.LayoutParams(mWidth, RelativeLayout.LayoutParams.WRAP_CONTENT)
        vh.imv.requestLayout()
        vh.tvName.text = dto.title
        vh.imv.loadImage(dto.coverUrl.toString())
        vh.tvKeyword.text = dto.keyword
        vh.tvTotalViews.text = String.format("TotalView: %s", dto.totalViews)
        vh.tvCollectionCount.text = String.format("Video count: %s", dto.videoCount)
        vh.itemView.setOnClickListener { onItemClickListener?.invoke(dto) }
    }

    class CollectionViewHoler(itemView: View) : BaseViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val imv: ImageView = itemView.imv
        val tvKeyword: TextView = itemView.tvKeyword
        val tvTotalViews: TextView = itemView.tvTotalViews
        val tvCollectionCount: TextView = itemView.tvVideoCount
        val lnMain: LinearLayout = itemView.lnlMain
     }

    fun updateCollection(newList: List<Collection>) {
        update(newList, CollectionsDiffCallBack(getData(), newList), false)
    }

    class CollectionsDiffCallBack(private val current: List<Collection>, private val next: List<Collection>) :
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

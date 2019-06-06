package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection.CollectionAdapter
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_collection.view.*

class VideosAdapter :
    BaseAdapter<Video, VideosAdapter.VideoViewHoler>(ArrayList()) {

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (String) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun itemLayoutResource() = R.layout.item_collection

    override fun createViewHolder(itemView: View) = VideoViewHoler(itemView)

    override fun onBindViewHolder(vh: VideoViewHoler, dto: Video, position: Int) {
        vh.tvName.text = dto.title
        vh.imv.loadImage(dto.previewUrl.toString())
        vh.itemView.setOnClickListener { onItemClickListener?.invoke(dto.embeddedUrl!!) }
    }

    class VideoViewHoler(itemView: View) : BaseViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val imv: ImageView = itemView.imv
    }

    fun updateVideos(newList: List<Video>) {
        update(newList, VideoDiffCallBack(getData(), newList), false)
    }

    class VideoDiffCallBack(private val current: List<Video>, private val next: List<Video>) :
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
            return currentItem.vid == nextItem.vid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val currentItem = current[oldItemPosition]
            val nextItem = next[newItemPosition]
            return currentItem == nextItem
        }
    }
}
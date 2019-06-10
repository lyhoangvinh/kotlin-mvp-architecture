package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.utils.formatDate
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_video.view.*


class VideosAdapter :
    BaseAdapter<Video, VideosAdapter.VideoViewHoler>(ArrayList()) {

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (String) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun itemLayoutResource() = R.layout.item_video

    override fun createViewHolder(itemView: View) = VideoViewHoler(itemView)

    override fun onBindViewHolder(vh: VideoViewHoler, dto: Video, position: Int) {
        vh.tvTitle.text = dto.title
        vh.tvKeyword.text = dto.keyword
        vh.tvDate.formatDate(dto.addTime!!)
        vh.tvViews.text = String.format("Views: %s", dto.viewNumber)
        vh.imv.loadImage(dto.previewUrl.toString())
        vh.itemView.setOnClickListener { onItemClickListener?.invoke(dto.videoUrl!!) }
        vh.tvPreview.setOnClickListener { onItemClickListener?.invoke(dto.previewVideoUrl!!) }
    }

    class VideoViewHoler(itemView: View) : BaseViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
        val tvKeyword: TextView = itemView.tvKeyword
        val tvPreview: TextView = itemView.tvPreview
        val tvDate: TextView = itemView.tvDate
        val tvViews: TextView = itemView.tvViews
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
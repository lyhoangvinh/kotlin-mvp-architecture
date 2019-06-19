package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_video_home.view.*

class VideosHomeAdapter :
    BaseAdapter<Video, VideosHomeAdapter.VideoViewHoler>(ArrayList()) {

    private var onItemClickListener: ((String) -> Unit)? = null

    private var mWidth = 0

    private var mHeight = 0

    fun setLayoutParams(mWidth: Int, mHeight: Int): VideosHomeAdapter {
        this.mWidth = mWidth
        this.mHeight = mHeight
        return this
    }

    fun setOnItemClickListener(onItemClickListener: (String) -> Unit): VideosHomeAdapter {
        this.onItemClickListener = onItemClickListener
        return this
    }

    override fun itemLayoutResource() = R.layout.item_video_home

    override fun createViewHolder(itemView: View) = VideoViewHoler(itemView)

    override fun onBindViewHolder(vh: VideoViewHoler, dto: Video, position: Int) {
        vh.imv.layoutParams.width = mWidth
        vh.imv.layoutParams.height = mHeight
        vh.lnMain.layoutParams = RelativeLayout.LayoutParams(mWidth, RelativeLayout.LayoutParams.WRAP_CONTENT)
        vh.imv.requestLayout()

        vh.tvName.text = dto.title
        vh.imv.loadImage(dto.previewUrl.toString())
        vh.tvKeyword.text = dto.keyword
        vh.tvTotalViews.text = String.format("Like: %s", dto.likes)
        vh.tvVideoCount.text = String.format("Views: %s", dto.viewNumber)
        vh.itemView.setOnClickListener { onItemClickListener?.invoke(dto.videoUrl!!) }
        vh.tvPreview.setOnClickListener {  onItemClickListener?.invoke(dto.previewVideoUrl!!) }
    }

    class VideoViewHoler(itemView: View) : BaseViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val imv: ImageView = itemView.imv
        val tvKeyword: TextView = itemView.tvKeyword
        val tvTotalViews: TextView = itemView.tvTotalViews
        val tvVideoCount: TextView = itemView.tvVideoCount
        val lnMain: LinearLayout = itemView.lnlMain
        val tvPreview : TextView = itemView.tvPreview
    }

    fun updateCollection(newList: List<Video>) {
        update(newList, VideosDiffCallBack(getData(), newList), false)
    }

    class VideosDiffCallBack(private val current: List<Video>, private val next: List<Video>) :
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

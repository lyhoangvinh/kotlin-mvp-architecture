package com.dev.lyhoangvinh.mvparchitecture.ui.feature.comics

import android.support.v7.util.DiffUtil
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.Issues
import com.dev.lyhoangvinh.mvparchitecture.utils.getAppDateFormatter
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_comics.view.*

class IssuesAdapter(mData: ArrayList<Issues>) : BaseAdapter<Issues, IssuesAdapter.MainViewHolder>(mData) {

    override fun itemLayoutResource() = R.layout.item_comics

    override fun createViewHolder(itemView: View) = MainViewHolder(itemView)

    override fun onBindViewHolder(vh: MainViewHolder, dto: Issues, position: Int) {
        vh.tvTitle.text = dto.volume.name
        vh.tvTime.text = String.format("Added: %s", getAppDateFormatter(dto.dateAdded!!))
        vh.tvDateLastUpdated.text = String.format("Last updated: %s", getAppDateFormatter(dto.dateLastUpdated!!))
        vh.imv.loadImage(dto.images.medium_url!!)
    }

    class MainViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvTitle = itemView.tvTitle!!
        val tvTime = itemView.tvTime!!
        val tvDateLastUpdated = itemView.tvDateLastUpdated!!
        val imv = itemView.imv!!
    }

    fun updateNotes(newList: List<Issues>) {
        update(newList, IssuesDiffCallBack(getData(), newList), false)
    }

    class IssuesDiffCallBack(private val current: List<Issues>, private val next: List<Issues>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return current.size
        }

        override fun getNewListSize(): Int {
            return next.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val currentItem = current[oldItemPosition]
            val nextItem = next[newItemPosition]
            return currentItem.id == nextItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val currentItem = current[oldItemPosition]
            val nextItem = next[newItemPosition]
            return currentItem == nextItem
        }
    }
}
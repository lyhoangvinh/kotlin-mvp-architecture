package com.dev.lyhoangvinh.mvparchitecture.ui.feature.main

import android.support.v7.util.DiffUtil
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.utils.getAppDateFormatter
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_comics.view.*

class MainAdapter(
    mData: ArrayList<Issues>,
    override val itemLayoutResource: Int = R.layout.item_comics
) : BaseAdapter<Issues, MainAdapter.MainViewHolder>(mData) {

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
        val result = DiffUtil.calculateDiff(MainDiffCallBack(getData(), newList), false)
        getData().clear()
        getData().addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}
package com.dev.lyhoangvinh.mvparchitecture.ui.main

import android.support.v7.util.DiffUtil
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import kotlinx.android.synthetic.main.item_comics.view.*

class MainAdapter(
    mData: ArrayList<Issues>,
    override val itemLayoutResource: Int = R.layout.item_comics
) : BaseAdapter<Issues, MainAdapter.MainViewHolder>(mData) {

    override fun createViewHolder(itemView: View): MainViewHolder =
        MainViewHolder(itemView)

    override fun onBindViewHolder(vh: MainViewHolder, dto: Issues, position: Int) {
        vh.tvName.text = dto.date_added
    }

    class MainViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvName = itemView.tvName!!
    }

    fun updateNotes(newList: List<Issues>) {
        val result = DiffUtil.calculateDiff(
            MainDiffCallBack(
                getData(),
                newList
            ), false)
        getData().clear()
        getData().addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}
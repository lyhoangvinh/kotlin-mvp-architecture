package com.dev.lyhoangvinh.mvparchitecture.base.adapter

import android.support.v7.widget.RecyclerView
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Item

abstract class BaseItemAdapter : RecyclerView.Adapter<BaseItemViewHolder<Item>>(){

    private var mItemList: List<Item>? = null

    fun setItemList(itemList: List<Item>) {
        mItemList = itemList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<Item>, position: Int)  {
        holder.setItem(mItemList!![position])
    }

    override fun getItemCount(): Int {
        return if (mItemList != null) mItemList!!.size else 0
    }

    fun getItemAt(position: Int): Item? {
        return if (mItemList != null) mItemList!![position] else null
    }

}
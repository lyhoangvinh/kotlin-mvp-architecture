package com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter

import android.support.v7.widget.RecyclerView
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.ItemViewModel

abstract class BaseItemAdapter : RecyclerView.Adapter<BaseItemViewHolder<ItemViewModel>>(){

    private var mItemList: List<ItemViewModel>? = null

    fun setItemList(itemList: List<ItemViewModel>) {
        mItemList = itemList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<ItemViewModel>, position: Int)  {
        holder.setItem(mItemList!![position])
    }

    override fun getItemCount(): Int {
        return if (mItemList != null) mItemList!!.size else 0
    }

    fun getItemAt(position: Int): ItemViewModel? {
        return if (mItemList != null) mItemList!![position] else null
    }

}
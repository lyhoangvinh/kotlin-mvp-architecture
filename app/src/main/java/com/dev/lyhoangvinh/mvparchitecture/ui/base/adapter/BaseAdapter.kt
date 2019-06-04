package com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter

import android.support.annotation.NonNull
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.ListData

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(private val data: ArrayList<T>) :
    RecyclerView.Adapter<VH>(), ListData {

    abstract fun itemLayoutResource(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(itemLayoutResource(), parent, false)
        return this.createViewHolder(v)
    }

    abstract fun createViewHolder(itemView: View): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = this.getItem(position)
        if (item != null) {
            this.onBindViewHolder(holder, this.getItem(position)!!, position)
        }
    }

    protected abstract fun onBindViewHolder(vh: VH, dto: T, position: Int)

    fun getItem(adapterPosition: Int): T? {
        return if (adapterPosition >= 0 && adapterPosition < this.data.size) this.data[adapterPosition] else null
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItemAt(position: Int): T? {
        return data[position]
    }

    fun getData(): ArrayList<T> {
        return data
    }

    override fun isDataEmpty(): Boolean {
        return data.size == 0
    }

    fun update(newList: List<T>, @NonNull cb: DiffUtil.Callback, detectMoves: Boolean) {
        val result = DiffUtil.calculateDiff(cb, detectMoves)
        getData().clear()
        getData().addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}
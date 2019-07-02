package com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter

import android.arch.lifecycle.LifecycleOwner
import android.graphics.Color
import android.support.annotation.NonNull
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.ListData
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.attachTextChangeAnimator
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.bindProgressButton
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.cleanUpDrawable
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.showProgress
import kotlinx.android.synthetic.main.item_loading_footer.view.*

abstract class BaseFooterAdapter<T, VH : RecyclerView.ViewHolder>(
    private val data: ArrayList<T>,
    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ListData {

    abstract fun itemLayoutResource(): Int

    abstract fun createViewHolder(itemView: View): VH

    protected abstract fun onBindViewHolder(vh: VH, dto: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemLayoutResource() -> this.createViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    itemLayoutResource(),
                    parent,
                    false
                )
            )
            R.layout.item_loading_footer -> LoadingFooterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading_footer,
                    parent,
                    false
                ), lifecycleOwner
            )
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is LoadingFooterViewHolder) {
            val item = this.getItem(position)
            if (item != null) {
                this.onBindViewHolder(holder as VH, item, position)
            }
        } else {
            holder.bind()
        }
    }

    private fun getItem(adapterPosition: Int): T? {
        return if (adapterPosition >= 0 && adapterPosition < this.data.size) this.data[adapterPosition] else null
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            R.layout.item_loading_footer
        } else {
            itemLayoutResource()
        }
    }

    override fun isDataEmpty() = data.isNullOrEmpty()

    override fun getItemCount(): Int = data.size + 1

    fun getData(): ArrayList<T> {
        return data
    }

    fun update(newList: List<T>, @NonNull cb: DiffUtil.Callback, detectMoves: Boolean) {
        val result = DiffUtil.calculateDiff(cb, detectMoves)
        getData().clear()
        getData().addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    class LoadingFooterViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseViewHolder(itemView) {
        init {
            itemView.apply {
                tvLoading.attachTextChangeAnimator()
                lifecycleOwner.bindProgressButton(tvLoading)
                tvLoading.setOnClickListener {
                    tvLoading.showProgress {
                        progressColor = Color.BLUE
                    }
                }
            }
        }

        fun bind() {
            itemView.apply {
                tvLoading.cleanUpDrawable()
                tvLoading.showProgress {
                    progressColor = Color.BLUE
                }
            }
        }
    }
}

package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category

import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.base.adapter.BaseViewHolder
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter : BaseAdapter<Category, CategoriesAdapter.CategoriesViewHolder>(ArrayList()) {

    override fun itemLayoutResource() = R.layout.item_category

    override fun createViewHolder(itemView: View) = CategoriesViewHolder(itemView)

    private var onClickItemListener: ((Category) -> Unit?)? = null

    fun setOnClickItemListener(onClickItemListener: (Category) -> Unit) {
        this.onClickItemListener = onClickItemListener
    }

    override fun onBindViewHolder(vh: CategoriesViewHolder, dto: Category, position: Int) {
        vh.tvName.text = dto.name
        vh.tvSlug.text = String.format("Slug: %s", dto.slug)
        vh.tvTotalVideos.text = String.format("Total videos: %s", dto.totalVideos.toString())
        vh.imv.loadImage(dto.coverUrl.toString())
        vh.tvClick.setOnClickListener { onClickItemListener?.invoke(dto) }
    }

    class CategoriesViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val tvSlug: TextView = itemView.tvSlug
        val tvTotalVideos: TextView = itemView.tvTotalVideos
        val tvClick: TextView = itemView.tvMake
        val imv: ImageView = itemView.imv
    }

    fun updateCategories(newList: List<Category>) {
        update(newList, CategoriesDiffCallBack(getData(), newList), false)
    }

    class CategoriesDiffCallBack(private val current: List<Category>, private val next: List<Category>) :
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
            return currentItem.CHID == nextItem.CHID
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val currentItem = current[oldItemPosition]
            val nextItem = next[newItemPosition]
            return currentItem == nextItem
        }
    }
}
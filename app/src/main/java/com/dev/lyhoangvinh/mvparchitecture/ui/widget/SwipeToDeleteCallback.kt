package com.dev.lyhoangvinh.mvparchitecture.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.utils.getRandomColorDrawable

class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback {

    private var icon: Drawable? = null

    private val background: ColorDrawable

    private var deleteOrSaveTask: PlainConsumer<Int>? = null

    constructor(ctx: Context, deleteTask: PlainConsumer<Int>) : super(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        this.deleteOrSaveTask = deleteTask
        icon = ContextCompat.getDrawable(ctx, R.drawable.ic_delete_toolbar)
        background = getRandomColorDrawable(ctx)
    }

    constructor(ctx: Context, icon: Drawable, deleteTask: PlainConsumer<Int>) : super(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        this.deleteOrSaveTask = deleteTask
        this.icon = icon
        background = getRandomColorDrawable(ctx)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // used for up and down movements
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (deleteOrSaveTask != null) {
            deleteOrSaveTask!!.accept(position)
        }

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
        val iconBottom = iconTop + icon!!.intrinsicHeight

        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin + icon!!.intrinsicWidth
            val iconRight = itemView.left + iconMargin
            icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - icon!!.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon!!.draw(c)
    }
}

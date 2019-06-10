package com.dev.lyhoangvinh.mvparchitecture.ui.widget.recycleview;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mVerticalSpaceHeight;

    public HorizontalSpaceItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (itemPosition != parent.getAdapter().getItemCount() - 1) {
            outRect.right = mVerticalSpaceHeight;
        }
    }

}

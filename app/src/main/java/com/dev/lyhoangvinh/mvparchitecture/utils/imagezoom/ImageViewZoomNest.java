package com.dev.lyhoangvinh.mvparchitecture.utils.imagezoom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

/**
 * ImageViewZoom nest in another scrollable widget
 */
public class ImageViewZoomNest extends ImageViewZoom {

    public ImageViewZoomNest(Context context) {
        this(context, null, 0);
    }

    public ImageViewZoomNest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public ImageViewZoomNest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener((v, event) -> {

            if (null != getParent()) {
                if (event.getPointerCount() > 1) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            }
            return false;
        });
    }
}

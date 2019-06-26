package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseDialogFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.ScreenUtils
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImage
import com.dev.lyhoangvinh.mvparchitecture.utils.loadImageWithListener
import kotlinx.android.synthetic.main.layout_detail_image_view.*
import kotlin.math.roundToInt

class ZoomImageDialogFragment : BaseDialogFragment() {

    private val url: String = ""

    override fun getLayoutResource() = R.layout.layout_detail_image_view

    override fun onStart() {
        super.onStart()
        setFullscreen()
    }

    override fun initialize(ctx: Context?) {

        loadImageWithListener(url, imvDetail, object : (ImageView, Drawable) -> Unit {
            override fun invoke(imageView: ImageView, drawable: Drawable) {
                val w = ((MyApplication.getInstance().getDeviceWidth() * 0.9).roundToInt())
                val h = w * imageView.drawable.intrinsicHeight / imageView.drawable.intrinsicWidth
                imageView.loadImage(url, w, h)
                val lp = imageView.layoutParams
                lp.height = if (h > ScreenUtils.getInstance(ctx).screenHeight)
                    h
                else
                    ScreenUtils.getInstance(ctx).screenHeight
                -ScreenUtils.getInstance(ctx).titlebarHeight
                -ScreenUtils.getInstance(ctx).getStatusBarHeight(activity)
                lp.width =
                    if (w > ScreenUtils.getInstance(ctx).screenWidth) w else ScreenUtils.getInstance(ctx).screenWidth
                imageView.layoutParams = lp

            }
        })
    }
}
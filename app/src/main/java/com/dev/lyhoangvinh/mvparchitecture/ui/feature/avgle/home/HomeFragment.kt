package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BasePresenterFragment
import com.tmall.ultraviewpager.UltraViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BasePresenterFragment<HomeView, HomePresenter>(), HomeView {

    private var categoriesAdapter: Categories2Adapter? = null
    private var collectionAdapter: Collection2Adapter? = null
    private var imageBannerAdapter: ImageBannerAdapter? = null

    override fun getLayoutResource() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent()?.inject(this)
    }

    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        categoriesAdapter = Categories2Adapter()
        collectionAdapter = Collection2Adapter()
        imageBannerAdapter = ImageBannerAdapter(fragmentManager!!)
        val pixel = (ctx as AppCompatActivity).windowManager.defaultDisplay.width / 2
        viewPage.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixel)
        viewPage.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        viewPage.initIndicator()
        viewPage.indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(ContextCompat.getColor(ctx, android.R.color.white))
            .setNormalColor(ContextCompat.getColor(ctx, R.color.color_disable_date))
            .setMargin(5, 5, 5, 20)
            .setRadius(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    3f,
                    ctx.getResources().getDisplayMetrics()
                ).toInt()
            )
            .setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
            .build()
        viewPage.setAutoScroll(3000)

        rcvCategory.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        rcvCollection.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        rcvCategory.adapter = categoriesAdapter
        rcvCollection.adapter = collectionAdapter
        presenter.getData()
    }

    override fun swapDataSuccess(categories: List<Category>, collections: List<Collection>) {
        tvSeeAll.text = getString(R.string.see_all)
        tvCategory.text = getString(R.string.category)
        tvCollection.text = getString(R.string.collection)
        categoriesAdapter?.updateCategories(categories)
        imageBannerAdapter?.setBanner(categories)
        collectionAdapter?.updateCollection(collections)
        viewPage.adapter = imageBannerAdapter
    }
}
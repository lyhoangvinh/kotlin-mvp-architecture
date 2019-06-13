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
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BasePresenterFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.widget.recycleview.GravitySnapHelper
import com.dev.lyhoangvinh.mvparchitecture.ui.widget.recycleview.HorizontalSpaceItemDecoration
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.tmall.ultraviewpager.UltraViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BasePresenterFragment<HomeView, HomePresenter>(), HomeView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    private var categoriesAdapter: Categories2Adapter? = null
    private var collectionAdapter: VideosHomeAdapter? = null
    private var imageBannerAdapter: ImageBannerAdapter? = null

    override fun getLayoutResource() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent()?.inject(this)
    }

    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        val mWidth =
            (MyApplication.getInstance().getDeviceWidth() - 2 * resources.getDimensionPixelSize(R.dimen.padding_16dp)) / 4 * 3 - resources.getDimensionPixelSize(
                R.dimen.padding_16dp
            ) / 2
        val mHeight = mWidth * 5 / 7
        categoriesAdapter = Categories2Adapter().setOnClickItemListener { navigatorHelper.navigateVideosFragment(it) }
        collectionAdapter = VideosHomeAdapter().setLayoutParams(mWidth, mHeight).setOnItemClickListener { navigatorHelper.navigateDetailActivity(it) }

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
                    ctx.getResources().displayMetrics
                ).toInt()
            )
            .setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
            .build()
        viewPage.setAutoScroll(3000)

        rcvCategory.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        rcvCategory.addItemDecoration(HorizontalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_16dp)))
        rcvCategory.isNestedScrollingEnabled = false

        rcvCollection.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        rcvCollection.addItemDecoration(HorizontalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.padding_16dp)))
        rcvCategory.adapter = categoriesAdapter
        rcvCollection.adapter = collectionAdapter

        //init snap
        GravitySnapHelper(Gravity.START).attachToRecyclerView(rcvCategory)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(rcvCollection)

        tvSeeAll.text = getString(R.string.see_all)
        tvCollection.text = getString(R.string.videos)
        tvSeeAll.setOnClickListener {  navigatorHelper.navigateVideosFragment(null)  }
        presenter.getData()
    }

    override fun swapCategoriesSuccess(categories: List<Category>) {
        categoriesAdapter?.updateCategories(categories)
    }

    override fun swapCollectionsSuccess(collections: List<Collection>) {
        imageBannerAdapter = ImageBannerAdapter(fragmentManager!!, collections)
        viewPage.adapter = imageBannerAdapter
    }

    override fun swapVideos(videos: List<Video>) {
        collectionAdapter?.updateCollection(videos)
    }
}
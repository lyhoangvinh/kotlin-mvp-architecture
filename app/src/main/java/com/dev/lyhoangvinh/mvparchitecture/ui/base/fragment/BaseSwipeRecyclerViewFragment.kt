package com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment

import android.content.Context
import android.support.annotation.CallSuper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.*
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.ListData
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.LoadMoreable
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import kotlinx.android.synthetic.main.view_no_data.*
import kotlinx.android.synthetic.main.view_recyclerview.*
import kotlinx.android.synthetic.main.view_scroll_top.*


/**
 * Created by vinh on 14/06/2017
 * Base Fragment with swipe to refresh and recycler view
 * All child fragments 's layout must have refresh layout with id **srl** and RecyclerView with id **rcv**
 *
 * @param <A> Adapter with list and addable headers and footers
 * @param <V> View
 * @param <P> Presenter
</P></V></A> */

abstract class BaseSwipeRecyclerViewFragment<A : RecyclerView.Adapter<*>, V : BaseView, P : BasePresenter<V>> :
    BaseSwipeToRefreshFragment<V, P>(), SwipeRefreshLayout.OnRefreshListener, LoadMoreable {

    private var mVisibleThreshold: Int = 5

//    var recyclerView: RecyclerView? = null
//
//    private var noDataView: View? = null
//
//    private var scrollTopView: View? = null

    private lateinit var adapter: A

    private var scrollTopPosition = DEFAULT_SCROLL_TOP_POSITION

    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun getLayoutResource() = R.layout.swipe_recycler_view

    /**
     * @return true if our adapter has no data
     */
    fun isDataEmpty(): Boolean {
        return adapter is ListData && (adapter as ListData).isDataEmpty()
    }

    // footer for load more state
    internal var footerView: View? = null

    private fun getPastVisibleItems(): Int {
        return if (layoutManager is LinearLayoutManager) {
            (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else 0
    }

    fun setScrollTopPosition(scrollTopPosition: Int) {
        this.scrollTopPosition = scrollTopPosition
    }

    protected open fun addItemDecoration(): RecyclerView.ItemDecoration? {
        return null
    }

    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        initRecyclerView(view)
//        scrollTopView = view.findViewById(R.id.scrollTop)
//        noDataView = view.findViewById(R.id.noDataView)
        if (scrollTop != null) {
            scrollTop!!.visibility = View.GONE
            scrollTop!!.setOnClickListener { rcv!!.scrollToPosition(0) }
        }
        if (noDataView != null) {
            noDataView!!.visibility = View.GONE
        }
    }

    /**
     * Create recycler view adapter
     *
     * @return adapter
     */
    protected abstract fun createAdapter(): A

    @CallSuper
    protected fun initRecyclerView(view: View) {
//        if (recyclerView == null) {
//            recyclerView = view.findViewById(R.id.rcv)
//        }
        layoutManager = createLayoutManager()
        rcv?.layoutManager = layoutManager
        rcv?.itemAnimator = DefaultItemAnimator()

        if (addItemDecoration() != null) {
            rcv?.addItemDecoration(addItemDecoration()!!)
        }

        rcv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // dy < 0 mean scrolling up
                if (dy < 0) return

                val totalItemCount = layoutManager!!.itemCount

                var lastVisibleItemPosition = 0
                if (layoutManager is StaggeredGridLayoutManager) {
                    val lastVisibleItemPositions =
                        (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                    lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
                    mVisibleThreshold *= (layoutManager as StaggeredGridLayoutManager).spanCount
                } else if (layoutManager is GridLayoutManager) {
                    mVisibleThreshold *= (layoutManager as GridLayoutManager).spanCount
                    lastVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                } else if (layoutManager is LinearLayoutManager) {
                    lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val visibleItemCount = layoutManager!!.childCount
                    val pastVisibleItems = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    updateScrollTop(visibleItemCount, pastVisibleItems)
                }

                if (lastVisibleItemPosition + mVisibleThreshold > totalItemCount) {
                    loadMore()
                }
            }
        })
        adapter = createAdapter()
        //        recyclerView.getRecycledViewPool().clear();
        rcv?.adapter = adapter
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        val lm = LinearLayoutManager(activity)
        lm.orientation = LinearLayoutManager.VERTICAL
        return lm
    }

    /**
     * Load more item after scrolling down to bottom of current list
     */
    override fun loadMore() {
        if (presenter is LoadMoreable && (presenter as LoadMoreable).canLoadMore()) {
            (presenter as LoadMoreable).loadMore()
            isRefresh = true
            //            adapter.addFooter(getFooterView());
        }
    }

    /**
     * refresh list by presenter
     */
    override fun refresh() {
        presenter.refresh()
    }

    /**
     * Called after refreshing success, for both case of success or fail
     * Should be called after [BaseActivity.hideProgress]
     */
    @CallSuper
    override fun doneRefresh() {
//        if (adapter != null) {
        //            adapter.removeFooter(getFooterView());
//        }
        updateNoDataState()
        updateScrollTop()
        android.os.Handler().postDelayed({ super.doneRefresh() }, 300)
    }

    /**
     * Show no data view if current adapter data is empty
     * must be call inside or after [.doneRefresh]
     */
    private fun updateNoDataState() {
        if (noDataView != null) {
            if (isDataEmpty()) {
                noDataView?.visibility = View.VISIBLE
            } else {
                noDataView?.visibility = View.GONE
            }
        }
    }

    /**
     * Show scroll top view (click on it to scroll recycler view to top)
     * if user scroll down more than [.DEFAULT_SCROLL_TOP_POSITION]
     */
    private fun updateScrollTop() {
        if (layoutManager != null) {
            val visibleItemCount = layoutManager!!.childCount
            updateScrollTop(visibleItemCount, getPastVisibleItems())
        }
    }

    override fun canLoadMore(): Boolean = false

    /**
     * Show scroll top view (click on it to scroll recycler view to top)
     * if user scroll down more than [.DEFAULT_SCROLL_TOP_POSITION]
     */
    private fun updateScrollTop(visibleItemCount: Int, pastVisibleItems: Int) {
        if (scrollTop != null) {
            if (scrollTop != null) {
                if (visibleItemCount + pastVisibleItems >= scrollTopPosition) {
                    scrollTop!!.visibility = View.VISIBLE
                } else {
                    scrollTop!!.visibility = View.GONE
                }
            } else {
                scrollTop!!.visibility = View.GONE
            }
        }
    }

    companion object {
        private val DEFAULT_SCROLL_TOP_POSITION = 10
    }
}

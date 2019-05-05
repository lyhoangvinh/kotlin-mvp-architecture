package com.dev.lyhoangvinh.mvparchitecture.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.ListData
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.LoadMoreable
import com.dev.lyhoangvinh.mvparchitecture.base.presenter.BasePresenter


/**
 * Created by vinh on 14/06/2017
 * Base Fragment with swipe to refresh and recycler view
 * All child fragments 's layout must have refresh layout with id **srl** and RecyclerView with id **rcv**
 *
 * @param <A> Adapter with list and addable headers and footers
 * @param <V> View
 * @param <P> Presenter
</P></V></A> */

abstract class BaseSwipeRecyclerViewActivity<A : RecyclerView.Adapter<*>, V : BaseView, P : BasePresenter<V>> :
    BaseSwipeToRefreshActivity<V, P>(), SwipeRefreshLayout.OnRefreshListener, LoadMoreable {

    var recyclerView: RecyclerView? = null

    private var noDataView: View? = null

    private var scrollTopView: View? = null

    private lateinit var adapter: A

    private var scrollTopPosition = DEFAULT_SCROLL_TOP_POSITION

    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun getLayoutResource() = R.layout.swipe_recycler_view

    /**
     * @return true if our adapter has no data
     */
    fun isDataEmpty(): Boolean {
        return adapter != null && adapter is ListData && (adapter as ListData).isDataEmpty
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        scrollTopView = findViewById(R.id.scrollTop)
        noDataView = findViewById(R.id.noDataView)
        if (scrollTopView != null) {
            scrollTopView!!.visibility = View.GONE
            scrollTopView!!.setOnClickListener { view1 -> recyclerView!!.scrollToPosition(0) }
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
    protected fun initRecyclerView() {
        if(recyclerView == null){
            recyclerView = findViewById(R.id.rcv)
        }
        layoutManager = createLayoutManager()
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()

        if (addItemDecoration() != null) {
            recyclerView?.addItemDecoration(addItemDecoration()!!)
        }

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager!!.childCount
                val totalItemCount = layoutManager!!.itemCount
                if (layoutManager is LinearLayoutManager) {
                    val pastVisibleItems = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    updateScrollTop(visibleItemCount, pastVisibleItems)
                }

                if (!isRefresh && visibleItemCount + getPastVisibleItems() >= totalItemCount) {
                    loadMore()
                }
            }
        })
        adapter = createAdapter()
        //        recyclerView.getRecycledViewPool().clear();
        recyclerView?.adapter = adapter
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        val lm = LinearLayoutManager(this)
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
     * Should be called after [BaseFragment.hideProgress]
     */
    @CallSuper
    override fun doneRefresh() {
        if (adapter != null) {
            //            adapter.removeFooter(getFooterView());
        }
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
        if (scrollTopView != null) {
            if (recyclerView != null) {
                if (visibleItemCount + pastVisibleItems >= scrollTopPosition) {
                    scrollTopView!!.visibility = View.VISIBLE
                } else {
                    scrollTopView!!.visibility = View.GONE
                }
            } else {
                scrollTopView!!.visibility = View.GONE
            }
        }
    }

    companion object {
        private val DEFAULT_SCROLL_TOP_POSITION = 10
    }
}

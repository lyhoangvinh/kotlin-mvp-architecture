package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BasePresenterActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.NetworkUtils
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : BasePresenterActivity<DetailView, DetailPresenter>(), DetailView {

    override fun getLayoutResource() = R.layout.activity_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val url = intent.getStringExtra(Constants.EXTRA_DATA)
            webViewDetail.settings.apply {
                javaScriptEnabled = true
                useWideViewPort = true
                displayZoomControls = false
                builtInZoomControls = false
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }

            webViewDetail.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (NetworkUtils.netWorkCheck(this@DetailActivity)) {
                        viewLoading.setVisible(true)
                    }
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    viewLoading.setVisible(false)
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    viewNoConnection.setVisible(true)
                }
            }
            webViewDetail.loadUrl(url)
            presenter.observeConnection(url.toString())
        }
    }

    override fun connection(isConnection: Boolean, url: String, isReload: Boolean) {
        if (isReload) {
            webViewDetail.loadUrl(url)
        }
        viewNoConnection.setVisible(!isConnection)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }
}
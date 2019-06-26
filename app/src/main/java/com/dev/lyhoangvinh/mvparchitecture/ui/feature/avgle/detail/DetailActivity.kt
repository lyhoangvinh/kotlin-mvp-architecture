package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BasePresenterActivity
import kotlinx.android.synthetic.main.activity_detail.*
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.utils.*
import com.dev.lyhoangvinh.mvparchitecture.utils.videoview.VideoEnabledWebChromeClient
import kotlinx.android.synthetic.main.toolbar_back.*
import lyhoangvinh.com.myutil.androidutils.AlertUtils


class DetailActivity : BasePresenterActivity<DetailView, DetailPresenter>(), DetailView {

    override fun getLayoutResource() = R.layout.activity_detail

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        imvBack.setOnClickListener { onBackPressed() }
        if (intent != null) {
            val url = intent.getStringExtra(Constants.EXTRA_DATA)
            webViewDetail.settings.apply {
                javaScriptEnabled = true
                allowFileAccess = true
                loadWithOverviewMode = true
                useWideViewPort = true
                setSupportZoom(true)
                displayZoomControls = true
                builtInZoomControls = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            webViewDetail.webChromeClient = object : VideoEnabledWebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress < 100) {
                        if (NetworkUtils.netWorkCheck(this@DetailActivity)) {
                            viewLoading.setVisible(true)
                        }
                    }
                    if (newProgress == 100) {
                        viewLoading.setVisible(false)
                    }
                }
            }
            webViewDetail.webViewClient = object : WebViewClient() {

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    viewNoConnection.setVisible(true)
                }

                @Suppress("DEPRECATION")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return if (url!!.endsWith(".mp3")) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(Uri.parse(url), "audio/*")
                        startActivity(intent)
                        true
                    } else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(Uri.parse(url), "video/*")
                        startActivity(intent)
                        true
//                    }
//                    else if (url.endsWith(".jpg") || url.endsWith(".png")) {
//                        view?.loadDataWithBaseURL(null,
//                            "<html><head></head><body><table style=\"width:100%; height:100%;\"><tr><td style=\"vertical-align:middle;\"><img src=\"$url\"></td></tr></table></body></html>", "html/css", "utf-8", null)
//                        true
                    } else {
                        super.shouldOverrideUrlLoading(view, url)
                    }
                }
            }
            presenter.observeConnection(url)
            webViewDetail.loadUrl(url)
            tvTitleToolBar.startCollapsingAnimation(url, 500L)
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            imvCopy.setOnClickListener {
                if (!TextUtils.isEmpty(url)) {
                    val clipData = ClipData.newPlainText("Source Text", url)
                    clipboardManager?.primaryClip = clipData
                    AlertUtils.showSnackBarShortMessage(it, "Copy text success.")
                }
//                openApp()
            }
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

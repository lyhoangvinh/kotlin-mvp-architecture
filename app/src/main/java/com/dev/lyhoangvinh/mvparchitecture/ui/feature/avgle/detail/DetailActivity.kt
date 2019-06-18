package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BasePresenterActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.NetworkUtils
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.activity_detail.*
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.utils.WebAppInterface
import com.dev.lyhoangvinh.mvparchitecture.utils.openApp
import kotlinx.android.synthetic.main.toolbar_back.*
import lyhoangvinh.com.myutil.androidutils.AlertUtils


class DetailActivity : BasePresenterActivity<DetailView, DetailPresenter>(), DetailView {

    override fun getLayoutResource() = R.layout.activity_detail

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
                displayZoomControls = false
                builtInZoomControls = false
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            webViewDetail.addJavascriptInterface(WebAppInterface(), "MyJSInterface")
            webViewDetail.webChromeClient = WebChromeClient()
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
                    } else {
                        super.shouldOverrideUrlLoading(view, url)
                    }
                }
            }
            presenter.observeConnection(url)
            webViewDetail.loadUrl(url)
            tvTitleToolBar.text = url
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

            imvCopy.setOnClickListener {
//                if (!TextUtils.isEmpty(url)) {
//                    val clipData = ClipData.newPlainText("Source Text", url)
//                    clipboardManager?.primaryClip = clipData
//                    AlertUtils.showSnackBarShortMessage(it, "Copy text success.")
//                }
                openApp()
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

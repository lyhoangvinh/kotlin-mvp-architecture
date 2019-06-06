package com.dev.lyhoangvinh.mvparchitecture.utils

import android.webkit.JavascriptInterface
import org.json.JSONObject

class WebAppInterface {
    @JavascriptInterface
    fun handleMyEvent(jsonString: String) {
        val data: JSONObject = JSONObject(jsonString)
        // Do stuff with event data
    }
}
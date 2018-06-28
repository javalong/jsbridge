package com.javalong.jsbridge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.javalong.lib.jsbridge.BridgeHandler
import com.javalong.lib.jsbridge.BridgeHelper
import com.javalong.lib.jsbridge.ResponseCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.javaScriptEnabled = true
        var helper = BridgeHelper()
        var handler = Handler()
        helper.init(webView, null)
        helper.registerHandler("test1", object : BridgeHandler {
            override fun call(data: JSONObject, callback: ResponseCallback) {
                Toast.makeText(this@MainActivity, data.toJSONString(), Toast.LENGTH_LONG).show()
                var a = JSONObject()
                a.put("a", "sss")
                a.put("v", "sss")
                handler.postDelayed(Runnable { callback.call(a) }, 2000)
            }
        })
        webView.loadUrl("file:///android_asset/test.html")
        btTestNative.setOnClickListener({
            var a = JSONObject()
            a.put("a", "111")
            a.put("v", "222")
            helper.callAsyncHandler("testNative", a, object : ResponseCallback {
                override fun call(data: JSONObject?) {
                    Log.e("aaaaa",a.toJSONString())
                }
            })
            var b = JSONObject()
            b.put("a", "333")
            b.put("v", "4444")
            helper.callAsyncHandler("testNative", b, object : ResponseCallback {
                override fun call(data: JSONObject?) {
                    Log.e("aaaaa",b.toJSONString())
                }
            })
        })
    }
}

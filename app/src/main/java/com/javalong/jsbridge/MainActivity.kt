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
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //这里开启debug模式 可以在chrome中进行调试
        WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.javaScriptEnabled = true
        var helper = BridgeHelper()
        var handler = Handler()
        //这里的webViewClient可以实现自己的做一些特殊的处理
        helper.init(webView, null)
        //注册方法提供给js端调用
        helper.registerHandler("testJs", object : BridgeHandler {
            override fun call(data: JSONObject, callback: ResponseCallback) {
                Toast.makeText(this@MainActivity, data.toJSONString(), Toast.LENGTH_LONG).show()
                var mockData = JSONObject()
                mockData.put("data1", "aaa")
                mockData.put("data2", "sss")
                //这里延迟，可以使调用同步方法和调用异步方法的效果更加明显
                handler.postDelayed(Runnable { callback.call(mockData) }, 2000)
                Log.e(TAG, "js成功调用native中注册的方法testJs")
            }
        })
        webView.loadUrl("file:///android_asset/test.html")
        var mockData1 = JSONObject()
        mockData1.put("data1", "111")
        mockData1.put("data2", "222")
        //异步调用js方法
        helper.callAsyncHandler("testNative", mockData1, object : ResponseCallback {
            override fun call(data: JSONObject?) {
                Log.e(TAG, "native异步调用js中注册的方法testNative,js在执行注册方法时回调callback方法，并传入参数:" + data?.toJSONString())
            }
        })
        var mockData2 = JSONObject()
        mockData2.put("data1", "333")
        mockData2.put("data2", "4444")
        //异步调用js方法
        helper.callAsyncHandler("testNative", mockData2, object : ResponseCallback {
            override fun call(data: JSONObject?) {
                Log.e(TAG, "native异步调用js中注册的方法testNative,js在执行注册方法时回调callback方法，并传入参数:" + data?.toJSONString())
            }
        })
        //同步调用js方法
        helper.callSyncHandler("testNative", mockData1, object : ResponseCallback {
            override fun call(data: JSONObject?) {
                Log.e(TAG, "native同步调用js中注册的方法testNative,js在执行注册方法时回调callback方法，并传入参数:" + data?.toJSONString())
            }
        })
        //同步调用js方法
        helper.callSyncHandler("testNative", mockData2, object : ResponseCallback {
            override fun call(data: JSONObject?) {
                Log.e(TAG, "native同步调用js中注册的方法testNative,js在执行注册方法时回调callback方法，并传入参数:" + data?.toJSONString())
            }
        })
        btTestNative.setOnClickListener({
            var a = JSONObject()
            a.put("a", "111")
            a.put("v", "222")
            helper.callAsyncHandler("testNative", a, object : ResponseCallback {
                override fun call(data: JSONObject?) {
                    Log.e(TAG, "native同步调用js中注册的方法testNative,js在执行注册方法时回调callback方法，并传入参数:" + data?.toJSONString())
                }
            })
            var b = JSONObject()
            b.put("a", "333")
            b.put("v", "4444")
            helper.callAsyncHandler("testNative", b, object : ResponseCallback {
                override fun call(data: JSONObject?) {
                    Log.e(TAG, "native同步调用js中注册的方法testNative,js在执行注册方法时回调callback方法，并传入参数:" + data?.toJSONString())
                }
            })
        })
    }
}

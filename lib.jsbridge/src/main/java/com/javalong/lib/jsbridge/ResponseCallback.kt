package com.javalong.lib.jsbridge

import com.alibaba.fastjson.JSONObject

interface ResponseCallback {
    fun call(data: JSONObject?)
}
package com.javalong.lib.jsbridge

import com.alibaba.fastjson.JSONObject

interface BridgeHandler {
    fun call(data: JSONObject, callback: ResponseCallback)
}
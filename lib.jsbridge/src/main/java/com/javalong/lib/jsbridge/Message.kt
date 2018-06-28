package com.javalong.lib.jsbridge

import com.alibaba.fastjson.JSONObject

/**
 * 传递的消息
 */
class Message {
    var handlerName = ""
    var data = JSONObject()
    var sync = false
    var callbackKey: String = "-1"

    constructor(handlerName: String, data: JSONObject, callbackKey: String, sync: Boolean) {
        this.handlerName = handlerName
        this.data = data
        this.sync = sync
        this.callbackKey = callbackKey
    }

    constructor()

}
/**
 * 加载自己的页面后，再去加载该js
 */
(function () {
    if (Bridge == null) {
        NO_CALL_FUNC_KEY = -1;
        PREFEX_CALL_FUNCS_KEY = "jj_";
        SCHEME = "jsbridge://";
        JSBRIDGE_LOADED = SCHEME + "jsbridge_loaded";
        CALL_HANDLE_URL = SCHEME + "call_handle_url";
        CALL_BACK_URL = SCHEME + "call_back_url";
        WAIT_TIME_OUT = 100;

        //有可能连续调用，间隔时间非常短，直接时间也无法区分
        unique = 0;
        //同步方法是否正在执行
        syncMessageLoop = false;
        //同步消息队列，等待前面的方法执行完成后
        syncMessages = [];
        //异步消息队列，多个消息一起执行
        asyncMessages = [];
        //异步队列，因为有可能会有多个消息一起请求
        asyncMessageWait = false;
        //回调的方法的队列，如果恨多个callback一起执行
        callbackMessages = [];
        callbackMessageWait = false;

        bridgeFuncs = {};
        callFuncs = {};


        //注册方法供native调用
        registerHandler = function (handlerName, bridgeFunc) {
            bridgeFuncs[handlerName] = bridgeFunc;
        };
        pushCallFunc = function (callFunc) {
            //callhandler方法可以调用多次，应该都有自己对应的回调
            var funcKey = NO_CALL_FUNC_KEY;
            if (callFunc != null) {
                var funcKey = PREFEX_CALL_FUNCS_KEY + unique + (new Date()).getTime();
                callFuncs[funcKey] = callFunc;
            }
            return funcKey;
        };
        //同步调用native的方法
        callSyncHandler = function (handlerName, data, callFunc) {
            var funcKey = pushCallFunc(callFunc);
            var message = {
                data: data,
                callbackKey: funcKey,
                handlerName: handlerName,
                sync: true
            };
            syncMessages.push(message);
            unique++;
            if (!syncMessageLoop) {
                syncMessageLoop = true;
                var msg = syncMessages.shift();
                if (msg == null) {
                    syncMessageLoop = false;
                    return;
                }
                location.href = CALL_HANDLE_URL + "?message=" + encodeURI(JSON.stringify(msg));
            }
        };
        //异步调用native的方法
        callAsyncHandler = function (handlerName, data, callFunc) {
            var funcKey = pushCallFunc(callFunc);
            var message = {
                data: data,
                callbackKey: funcKey,
                handlerName: handlerName,
                sync: false
            };
            unique++;
            asyncMessages.push(message);
            if (!asyncMessageWait) {
                asyncMessageWait = true;
                setTimeout(function () {
                    //遍历asyncMessages,执行调用bridge，执行完毕后，清空，再打开入口
                    location.href = CALL_HANDLE_URL + "?messages=" + encodeURI(JSON.stringify(asyncMessages));
                    asyncMessages = [];
                    asyncMessageWait = false;
                }, WAIT_TIME_OUT);
            }
        };

        executeBridgeFunc = function (msg) {
            msg = JSON.parse(msg);
            //判断bridgeFuncs是否注册了对应的方法
            if (bridgeFuncs[msg.handlerName] != null) {
                //如果有就直接执行，并且传入一个回调方法
                bridgeFuncs[msg.handlerName](msg.data, function (responseParam) {
                    //当调用了callback方法后会进入这里
                    if (responseParam == null) {
                        responseParam = {};
                    }
                    //这里需要传入参数是否是同步的消息，为了native端能够继续执行同步消息
                    responseParam.sync = msg.sync;
                    //封装一个消息对象，传入native发送过来的消息的对应的回调方法的key
                    var callbackMsg = {
                        data: responseParam,
                        id: msg.callbackKey
                    };

                    //由于location.href频繁调用会造成消息丢失，只能接收到最后的一个location.href，所以这里需要把消息存入队列，一次性发送
                    callbackMessages.push(callbackMsg);
                    if (!callbackMessageWait) {
                        callbackMessageWait = true;
                        setTimeout(function () {
                            //执行完毕bridge后，回调native方法
                            location.href = CALL_BACK_URL + "?messages=" + encodeURI(JSON.stringify(callbackMessages));
                            //发送完成后需要清空数据，防止重复发送数据
                            callbackMessages = [];
                            callbackMessageWait = false;
                        }, WAIT_TIME_OUT);
                    }
                })
            }
        };
        executeCallFunc = function (data, id) {
            var obj = JSON.parse(data);
            if (callFuncs[id] != null) {
                callFuncs[id](obj);
                //执行完毕回调后，就把方法移除
                callFuncs[id] = null
            }
            //每次执行完回调后，如果是一个同步的消息从同步消息队列中去获取最新的消息发送
            if (obj.sync) {
                var msg = syncMessages.shift();
                if (msg == null) {
                    syncMessageLoop = false;
                    return;
                }
                location.href = CALL_HANDLE_URL + "?message=" + encodeURI(JSON.stringify(msg));
            }
        };
        var Bridge = window.Bridge = {
            registerHandler: registerHandler,
            pushCallFunc: pushCallFunc,
            callSyncHandler: callSyncHandler,
            callAsyncHandler: callAsyncHandler,
            executeBridgeFunc: executeBridgeFunc,
            executeCallFunc: executeCallFunc
        };

        var evt = document.createEvent("Events");
        evt.initEvent("onBridgeLoaded");
        document.dispatchEvent(evt);
        //同时也发送信息告诉native端，bridge加载完成
        location.href = JSBRIDGE_LOADED;
    }
})();

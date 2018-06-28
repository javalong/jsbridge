package com.javalong.lib.jsbridge

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.view.KeyEvent
import android.webkit.*

open class WebViewClientProxy(val client: WebViewClient?) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        if (client == null) {
            super.onPageFinished(view, url)
        } else {
            client.onPageFinished(view, url)
        }
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse {
        return if (client == null) {
            super.shouldInterceptRequest(view, url)
        } else {
            client.shouldInterceptRequest(view, url)
        }
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse {
        if (client == null) {
            return super.shouldInterceptRequest(view, request)
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                client.shouldInterceptRequest(view, request)
            } else {
                return super.shouldInterceptRequest(view, request)
            }
        }
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        return client?.shouldOverrideKeyEvent(view, event)
                ?: super.shouldOverrideKeyEvent(view, event)
    }

    override fun onSafeBrowsingHit(view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) {
        if (client == null) {
            super.onSafeBrowsingHit(view, request, threatType, callback)
        } else {
            client.onSafeBrowsingHit(view, request, threatType, callback)
        }
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        if (client == null) {
            super.doUpdateVisitedHistory(view, url, isReload)
        } else {
            client.doUpdateVisitedHistory(view, url, isReload)
        }
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        if (client == null) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        } else {
            client.onReceivedError(view, errorCode, description, failingUrl)
        }
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        if (client == null) {
            super.onReceivedError(view, request, error)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                client.onReceivedError(view, request, error)
            } else {
                super.onReceivedError(view, request, error)
            }
        }
    }

    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        if (client == null) {
            return super.onRenderProcessGone(view, detail)
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                client.onRenderProcessGone(view, detail)
            } else {
                return super.onRenderProcessGone(view, detail)
            }
        }
    }

    override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
        if (client == null) {
            super.onReceivedLoginRequest(view, realm, account, args)
        } else {
            client.onReceivedLoginRequest(view, realm, account, args)
        }
    }

    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        if (client == null) {
            super.onReceivedHttpError(view, request, errorResponse)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                client.onReceivedHttpError(view, request, errorResponse)
            } else {
                super.onReceivedHttpError(view, request, errorResponse)
            }
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (client == null) {
            super.onPageStarted(view, url, favicon)
        } else {
            client.onPageStarted(view, url, favicon)
        }
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        if (client == null) {
            super.onScaleChanged(view, oldScale, newScale)
        } else {
            client.onScaleChanged(view, oldScale, newScale)
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (client == null) {
            return super.shouldOverrideUrlLoading(view, url)
        } else {
            return client.shouldOverrideUrlLoading(view, url)
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (client == null) {
            return super.shouldOverrideUrlLoading(view, request)
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                client.shouldOverrideUrlLoading(view, request)
            } else {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        if (client == null) {
            return super.onPageCommitVisible(view, url)
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                client.onPageCommitVisible(view, url)
            } else {
                return super.onPageCommitVisible(view, url)
            }
        }
    }

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        if (client == null) {
            return super.onUnhandledKeyEvent(view, event)
        } else {
            return client.onUnhandledKeyEvent(view, event)
        }
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        if (client == null) {
            return super.onReceivedClientCertRequest(view, request)
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                client.onReceivedClientCertRequest(view, request)
            } else {
                return super.onReceivedClientCertRequest(view, request)
            }
        }
    }

    override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
        if (client == null) {
            return super.onReceivedHttpAuthRequest(view, handler, host, realm)
        } else {
            return client.onReceivedHttpAuthRequest(view, handler, host, realm)
        }
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        if (client == null) {
            return super.onReceivedSslError(view, handler, error)
        } else {
            return client.onReceivedSslError(view, handler, error)
        }
    }

    override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
        if (client == null) {
            return super.onTooManyRedirects(view, cancelMsg, continueMsg)
        } else {
            return client.onTooManyRedirects(view, cancelMsg, continueMsg)
        }
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        if (client == null) {
            return super.onFormResubmission(view, dontResend, resend)
        } else {
            return client.onFormResubmission(view, dontResend, resend)
        }
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        if (client == null) {
            return super.onLoadResource(view, url)
        } else {
            return client.onLoadResource(view, url)
        }
    }
}
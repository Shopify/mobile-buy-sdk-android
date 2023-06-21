package com.shopify.checkout.models.errors

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import java.lang.Exception

class WebviewException(val request: WebResourceRequest?, val error: WebResourceError?, val response: WebResourceResponse?) : Exception()

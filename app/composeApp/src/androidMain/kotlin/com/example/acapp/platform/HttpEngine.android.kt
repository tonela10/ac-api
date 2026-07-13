package com.example.acapp.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.net.Proxy

actual fun httpClientEngine(): HttpClientEngine = OkHttp.create {
    config {
        proxy(Proxy.NO_PROXY)
    }
}

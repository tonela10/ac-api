package com.example.acapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import java.net.Proxy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SingletonImageLoader.setSafe {
            ImageLoader.Builder(this)
                .components {
                    add(KtorNetworkFetcherFactory(
                        httpClient = HttpClient(OkHttp) {
                            engine { config { proxy(Proxy.NO_PROXY) } }
                        }
                    ))
                }
                .build()
        }

        setContent { App() }
    }
}

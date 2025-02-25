package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun getHttpClientEngine(): HttpClientEngine = OkHttp.create()
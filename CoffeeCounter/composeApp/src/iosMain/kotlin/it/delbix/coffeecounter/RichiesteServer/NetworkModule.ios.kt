package it.delbix.coffeecounter.RichiesteServer

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun getHttpClientEngine(): HttpClientEngine = Darwin.create()
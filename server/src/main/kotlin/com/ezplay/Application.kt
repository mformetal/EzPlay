package com.ezplay

import com.ezplay.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Paths

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureRouting()

        launch(coroutineContext + Dispatchers.IO) {
            MusicFileSystem().songs()
                .forEach {

                }
        }
    }).start(wait = true)
}

package metal.ezplay.player

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import metal.ezplay.network.Routes

class SongDownloader(private val client: HttpClient,
    private val fileSystem: FileSystem,
    private val internalFileStorage: Path) {

    private val webSocketClient = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun download(songId: Int) {
        webSocketClient.webSocket(
            urlString = Routes.websocket(songId),
            request = {
                headers {
                    append(HttpHeaders.Accept, ContentType.Any.toString())
                    append(HttpHeaders.ContentType, ContentType.Any.toString())
                    append(HttpHeaders.Connection, "Upgrade")
                }
            },
            block = {
                val outputRoutine = launch { output() }
                val inputRoutine = launch { input() }

                inputRoutine.join() // Wait for completion; either "exit" or error
                outputRoutine.cancelAndJoin()
            }
        )
    }

    suspend fun DefaultClientWebSocketSession.output() {
//        try {
//            for (message in incoming) {
//                println("MESSAGE: $message")
////                message as? Frame.Text ?: continue
////                println(message.readText())
//            }
//        } catch (e: Exception) {
//            println("Error while receiving: " + e.localizedMessage)
//        }
    }

    suspend fun DefaultClientWebSocketSession.input() {

    }
}
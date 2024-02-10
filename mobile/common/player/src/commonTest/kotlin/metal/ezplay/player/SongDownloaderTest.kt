package metal.ezplay.player

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import metal.ezplay.multiplatform.dto.PreviewDto
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SongDownloaderTest {

    private val files = mutableListOf<Path>()
    private lateinit var fileDirectory: Path

    private lateinit var engine: MockEngine

    private lateinit var downloader: SongDownloader

    @BeforeTest
    fun setUp() {
        fileDirectory = Path("test")
        SystemFileSystem.createDirectories(fileDirectory)
    }

    @AfterTest
    fun cleanup() {
        files.forEach { file ->
            SystemFileSystem.delete(file)
        }
        SystemFileSystem.delete(fileDirectory)
    }

    @Test
    fun `should download audio file into path`() = runTest {
        val songId = 0
        val fileSize = 100L
        val fileName = "test.mp3"
        createDownloader { request ->
            val requestType = request.url.pathSegments.get(request.url.pathSegments.lastIndex - 1)
            if (requestType == "preview") {
                respondWithPreview(
                    PreviewDto(
                        fileSize = fileSize,
                        fileName = "test.mp3"
                    )
                )
            } else {
                respond(
                    ByteReadChannel(
                        ByteArray(fileSize.toInt()) {
                            it.toByte()
                        }
                    )
                )
            }
        }

        val audioFile = downloader.download(songId).also(files::add)

        val metadata = assertNotNull(SystemFileSystem.metadataOrNull(audioFile))
        assertEquals(metadata.size, fileSize)
        assertEquals(audioFile.name, fileName)
    }

    private fun MockRequestHandleScope.respondWithPreview(dto: PreviewDto) =
        respondJson(dto)

    private inline fun <reified T : Any> MockRequestHandleScope.respondJson(
        item: T,
        status: HttpStatusCode = HttpStatusCode.OK,
    ) =
        respond(
            content = Json.encodeToString(item),
            status = status,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )

    private fun createDownloader(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        engine = MockEngine(handler)

        downloader = SongDownloader(
            client = HttpClient(engine) {
                install(ContentNegotiation) {
                    json()
                }
            },
            fileSystem = SystemFileSystem,
            internalFileStorage = fileDirectory
        )
    }
}
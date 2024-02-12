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
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
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

    private val testDispatcher = StandardTestDispatcher()

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
    fun `should download audio file into path`() = runTest(testDispatcher) {
        val songId = 0
        val fileSize = 100L
        val fileName = "test.mp3"
        createDownloader { request ->
            val requestType = request.url.pathSegments[request.url.pathSegments.lastIndex - 1]
            if (requestType == "preview") {
                respondWithPreview(
                    PreviewDto(
                        fileSize = fileSize,
                        fileName = fileName
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
        yield()
        advanceUntilIdle()
        advanceTimeBy(10000L)
        runCurrent()
//        testDispatcher.scheduler.advanceUntilIdle()
//        testDispatcher.scheduler.advanceTimeBy(100000L)
//        delay(10000)

        val metadata = assertNotNull(SystemFileSystem.metadataOrNull(audioFile))
        assertEquals(audioFile.name, fileName)
        assertEquals(metadata.size, fileSize)
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

    private fun TestScope.createDownloader(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        engine = MockEngine(handler)

        downloader = SongDownloader(
            scope = this,
            backgroundDispatcher = testDispatcher,
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
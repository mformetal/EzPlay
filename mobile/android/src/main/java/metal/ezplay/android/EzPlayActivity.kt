package metal.ezplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import metal.ezplay.library.LibraryViewModel
import metal.ezplay.network.EzPlayApi

class EzPlayActivity : ComponentActivity() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val api = EzPlayApi(client)
    private val viewModel = LibraryViewModel(api)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.fetchLibrary()

            viewModel.uiState.collect {
                println("STATE: $it")
            }
        }
    }
}
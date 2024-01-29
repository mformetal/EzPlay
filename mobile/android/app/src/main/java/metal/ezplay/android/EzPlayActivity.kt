package metal.ezplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import metal.ezplay.library.LibraryScreen
import org.koin.androidx.compose.koinViewModel
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

        setContent {
            val navController = rememberNavController()
            AppTheme {
                BottomBar(navController) {
                    NavHost(
                        navController = navController,
                        startDestination = "library"
                    ) {
                        composable(route = "library") {
                            LibraryScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}
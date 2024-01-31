package metal.ezplay.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import metal.ezplay.nowplaying.NowPlayingViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel, nowPlayingViewModel: NowPlayingViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchLibrary()
    }

    val library by viewModel.uiState.collectAsState()
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(library.songs) { song ->
            AsyncImage(
                model = song.imageUrl,
                contentDescription = null,
                onError = {
                    val result = it.result
                }
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = song.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = song.artist.name, style = MaterialTheme.typography.bodySmall)
            }
            Divider()
        }
    }
}
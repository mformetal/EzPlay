package metal.ezplay.library

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LibraryScreen(viewModel: LibraryViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchLibrary()
    }

    val library by viewModel.uiState.collectAsState()
    val songs = library.artists
        .flatMap { artist ->
            artist.albums
        }.flatMap { album ->
            album.songs
        }
    LazyColumn {
        items(songs) { song ->
            Text(song.name)
        }
    }
}
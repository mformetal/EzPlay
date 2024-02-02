package metal.ezplay.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import metal.ezplay.android.compose.extra_small_padding
import metal.ezplay.android.compose.medium_padding
import metal.ezplay.android.compose.small_padding
import metal.ezplay.nowplaying.NowPlayingViewModel

@Composable
fun LibraryScreen(modifier: Modifier, viewModel: LibraryViewModel, nowPlayingViewModel: NowPlayingViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchLibrary()
    }

    val library by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(small_padding)) {
            items(library.songs) { song ->
                Row(modifier = Modifier.fillMaxWidth()
                    .padding(small_padding)
                    .clickable {
                        nowPlayingViewModel.play(song)
                    }) {
//                    AsyncImage(
//                        model = song.imageUrl,
//                        contentDescription = null,
//                        onError = {
//                            println(it.result.throwable.message)
//                        }
//                    )

                    Column(verticalArrangement = Arrangement.spacedBy(extra_small_padding)) {
                        Text(text = song.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = song.artist.name, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Divider()
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(medium_padding),
            onClick = {

            }
        ) {
            Icon(Icons.Filled.PlayArrow, "Play everything.")
        }
    }
}
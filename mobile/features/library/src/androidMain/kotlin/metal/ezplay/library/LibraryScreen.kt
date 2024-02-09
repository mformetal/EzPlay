package metal.ezplay.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import metal.ezplay.android.compose.extra_small_padding
import metal.ezplay.android.compose.medium_padding
import metal.ezplay.android.compose.small_padding
import metal.ezplay.multiplatform.extensions.takeIfInstance
import metal.ezplay.nowplaying.NowPlayingViewModel

@Composable
fun LibraryScreen(modifier: Modifier, viewModel: LibraryViewModel, nowPlayingViewModel: NowPlayingViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchLibrary()
    }

    val library by viewModel.uiState.collectAsState()
    val dataState = library.data

    Box(modifier = modifier.fillMaxSize()) {
        when (dataState) {
            LibraryDataState.Loading -> LoadingState()
            LibraryDataState.Error -> TODO()
            is LibraryDataState.Success -> SuccessState(nowPlayingViewModel, dataState)
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(medium_padding),
            onClick = {
                viewModel.playButtonClicked()
            }
        ) {
            Icon(Icons.Filled.PlayArrow, "Play everything.")
        }
    }
}

@Composable
private fun BoxScope.LoadingState() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp).align(Alignment.Center),
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
private fun SuccessState(nowPlayingViewModel: NowPlayingViewModel, state: LibraryDataState.Success) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(small_padding)) {
        items(state.items) { song ->
            Row(modifier = Modifier.fillMaxWidth()
                .padding(small_padding)
                .clickable {
                    nowPlayingViewModel.play(song)
            }) {
                Column(verticalArrangement = Arrangement.spacedBy(extra_small_padding)) {
                    Text(text = song.name, style = MaterialTheme.typography.bodyLarge)
                    Text(text = song.artist.name, style = MaterialTheme.typography.bodySmall)
                }
            }
            Divider()
        }
    }
}
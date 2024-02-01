package metal.ezplay.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Colors
import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun NowPlayingScreen(nowPlayingViewModel: NowPlayingViewModel) {
    val state by nowPlayingViewModel.uiState.collectAsState()

    state.song?.let { song ->
        Box(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ).fillMaxWidth().zIndex(2f)) {
                Column {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = song.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer ,style = MaterialTheme.typography.bodyLarge)
                        Text(text = song.artist.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall)
                    }
                }

                IconButton(onClick = {
                    if (state.isPlaying) {
                        nowPlayingViewModel.pause()
                    } else {
                        nowPlayingViewModel.play()
                    }
                }) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
package metal.ezplay.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Colors
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import metal.ezplay.android.compose.medium_padding
import metal.ezplay.android.compose.small_padding
import metal.ezplay.player.MusicPlayerState
import metal.ezplay.android.xml.R as XmlR

@Composable
fun ColumnScope.NowPlayingScreen(modifier: Modifier, nowPlayingViewModel: NowPlayingViewModel) {
    val state by nowPlayingViewModel.uiState.collectAsState()

    Box(modifier = modifier
        .background(MaterialTheme.colorScheme.primaryContainer)
        .zIndex(2f)) {
        LinearProgressIndicator(
            progress = state.progress,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(start = small_padding, end = small_padding)
            .fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = state.songName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge)
                Text(text = state.artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }

            IconButton(onClick = {
                nowPlayingViewModel.musicControlsClicked()
            }) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter =
                    painterResource(
                        if (state.isPlaying) XmlR.drawable.baseline_pause_24 else XmlR.drawable.baseline_play_arrow_24
                    ),
                    contentDescription = null
                )
            }

            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}
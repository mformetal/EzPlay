package metal.ezplay.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import metal.ezplay.android.compose.medium_padding
import metal.ezplay.android.compose.small_padding
import metal.ezplay.android.xml.R as XmlR

@Composable
fun NowPlayingScreen(paddingValues: PaddingValues, nowPlayingViewModel: NowPlayingViewModel) {
    val state by nowPlayingViewModel.uiState.collectAsState()

    state.song?.let { song ->
        Box(modifier = Modifier.fillMaxSize()
            .zIndex(2f)
            .padding(bottom = paddingValues.calculateBottomPadding())) {
            Row(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ).fillMaxWidth()
                .padding(small_padding)) {
                Column(modifier = Modifier.weight(1f)) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = song.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer ,style = MaterialTheme.typography.bodyLarge)
                        Text(text = song.artist.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall)
                    }
                }

                IconButton(onClick = {
                    nowPlayingViewModel.musicControlsClicked()
                }) {
                    Icon(
                        painter =
                        painterResource(
                            if (state.isPlaying) XmlR.drawable.baseline_pause_24 else
                            XmlR.drawable.baseline_play_arrow_24
                        ),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
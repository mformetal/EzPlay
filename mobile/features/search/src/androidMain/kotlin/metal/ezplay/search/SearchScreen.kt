package metal.ezplay.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import metal.ezplay.android.compose.extra_small_padding
import metal.ezplay.android.compose.medium_padding
import metal.ezplay.android.compose.small_padding

@Composable
fun SearchScreen(modifier: Modifier, viewModel: SearchViewModel) {
    val songs = viewModel.searchResults.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        topBar = {
            SearchField(viewModel) {
                songs.refresh()
            }
        },
        content = {
            LazyColumn(modifier = modifier.padding(it),
                verticalArrangement = Arrangement.spacedBy(small_padding)) {
                when (val loadState = songs.loadState.refresh) {
                    is LoadState.Error -> {
                        item {
                            Text(loadState.error.message!!)
                        }
                    }
                    LoadState.Loading -> {
                        item {
                            Row {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                    is LoadState.NotLoading -> {
                        items(songs.itemCount) { index ->
                            val song = songs[index]!!
                            Row(modifier = Modifier.fillMaxWidth()
                                .padding(small_padding)
                                .clickable {
                                    viewModel.play(song)
                                }) {
                                Column(verticalArrangement = Arrangement.spacedBy(extra_small_padding)) {
                                    Text(text = song.name,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyLarge)
                                    Text(text = song.artist.name,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            Divider()
                        }
                    }
                }
            }
        },
    )
}
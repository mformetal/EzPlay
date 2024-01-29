package metal.ezplay.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import metal.ezplay.dto.SongDto
import metal.ezplay.network.EzPlayApi

class NowPlayingViewModel(private val api: EzPlayApi) : ViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingState.Empty)
    val uiState: StateFlow<NowPlayingState> = _uiState.asStateFlow()

    fun play(song: SongDto) {
        viewModelScope.launch {
            val channel = api.play(song)
        }
    }
}
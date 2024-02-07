package metal.ezplay.library

import metal.ezplay.dto.SongDto

data class LibraryState(
    val isLoading: Boolean = true,
    val songs: List<SongDto> = emptyList()
)
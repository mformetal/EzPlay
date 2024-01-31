package metal.ezplay.library

import metal.ezplay.dto.SongDto

data class LibraryState(
    val songs: List<SongDto> = emptyList()
)
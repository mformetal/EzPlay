package metal.ezplay.library

import metal.ezplay.dto.ArtistDto

data class LibraryState(
    val artists: List<ArtistDto> = emptyList()
)
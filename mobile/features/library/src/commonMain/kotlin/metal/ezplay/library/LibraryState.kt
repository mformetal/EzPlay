package metal.ezplay.library

import metal.ezplay.multiplatform.dto.SongDto

data class LibraryState(
    val data: LibraryDataState = LibraryDataState.Loading
)
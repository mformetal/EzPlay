package metal.ezplay.library

import metal.ezplay.multiplatform.dto.SongDto

sealed class LibraryDataState {

    data object Error : LibraryDataState()

    data object Loading : LibraryDataState()

    data class Success(val items: List<SongDto>) : LibraryDataState()
}
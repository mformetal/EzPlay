package metal.ezplay.network

import metal.ezplay.multiplatform.dto.SongId

object Routes {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    val LIBRARY = BASE_URL.plus("library")

    fun preview(songId: Int) = LIBRARY.plus("/preview/$songId")

    fun download(songId: Int) = LIBRARY.plus("/download/$songId")

    fun ids() = LIBRARY.plus("/ids")

    fun song(songId: Int) = LIBRARY.plus("/songs/$songId")
}
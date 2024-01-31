package metal.ezplay.network

object Routes {

    private const val BASE_URL = "https://10.0.2.2:8080/"

    val LIBRARY = BASE_URL.plus("library")

    fun preview(songId: Int) = LIBRARY.plus("/preview/$songId")

    fun download(songId: Int) = LIBRARY.plus("/download/$songId")

    fun play(songId: Int) = LIBRARY.plus("/play/$songId")
}
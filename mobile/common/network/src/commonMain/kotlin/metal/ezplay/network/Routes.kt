package metal.ezplay.network

object Routes {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    val LIBRARY = BASE_URL.plus("library")

    fun play(songId: Int) = BASE_URL.plus("play/$songId")
}
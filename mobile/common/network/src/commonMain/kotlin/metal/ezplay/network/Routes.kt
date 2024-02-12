package metal.ezplay.network

object Routes {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    object Songs {
        private val SONGS = BASE_URL.plus("songs")

        fun page() = SONGS

        fun preview(songId: Int) = SONGS.plus("/preview/$songId")

        fun downloadSong(songId: Int) = SONGS.plus("/download/$songId")

        fun ids() = SONGS.plus("/ids")

        fun info(songId: Int) = SONGS.plus("/$songId")

        fun play(songId: Int) = SONGS.plus("/play/$songId")
    }
}
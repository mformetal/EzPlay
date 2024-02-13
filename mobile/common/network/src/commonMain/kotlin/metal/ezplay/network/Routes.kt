package metal.ezplay.network

object Routes {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    object Songs {
        private const val BASE = BASE_URL.plus("songs")

        fun page() = BASE

        fun preview(songId: Int) = BASE.plus("/preview/$songId")

        fun downloadSong(songId: Int) = BASE.plus("/download/$songId")

        fun ids() = BASE.plus("/ids")

        fun info(songId: Int) = BASE.plus("/$songId")

        fun play(songId: Int) = BASE.plus("/play/$songId")
    }
}

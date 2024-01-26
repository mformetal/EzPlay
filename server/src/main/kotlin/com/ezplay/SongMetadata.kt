package com.ezplay

data class SongMetadata(
    val path: String,
    val songName: String,
    val songDuration: Int,
    val artistName: String,
    val albumName: String
)
package com.ezplay.extensions

import io.ktor.util.extension
import java.nio.file.Path

val Path.hasSongExtension: Boolean
    get() = extension == "mp3" || extension == "flac"
package com.ezplay.db

import com.ezplay.MusicFileSystem
import com.ezplay.db.tables.Albums
import com.ezplay.db.tables.Artists
import com.ezplay.db.tables.Songs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

object DatabaseSingleton {

    private lateinit var database: Database

    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Artists, Albums, Songs)
        }
    }

    fun populate(coroutineScope: CoroutineScope, coroutineContext: CoroutineContext) {
        coroutineScope.launch(coroutineContext + Dispatchers.IO) {
            val songs = MusicFileSystem().songs()

            val artistIds = query {
                Artists.batchInsert(songs.distinctBy { it.artistName }) { metadata ->
                    this[Artists.name] = metadata.artistName
                }.associate { resultRow ->
                    resultRow[Artists.name] to resultRow[Artists.id]
                }
            }

            val albumIds = query {
                Albums.batchInsert(songs.distinctBy { it.albumName }) { metadata ->
                    this[Albums.name] = metadata.albumName
                    this[Albums.artist] = artistIds.getValue(metadata.artistName)
                }.associate { resultRow ->
                    resultRow[Albums.name] to resultRow[Albums.id]
                }
            }

            query {
                Songs.batchInsert(songs) { metadata ->
                    this[Songs.name] = metadata.songName
                    this[Songs.localPath] = metadata.path
                    this[Songs.artist] = artistIds.getValue(metadata.artistName)
                    this[Songs.album] = albumIds.getValue(metadata.albumName)
                }
            }
        }
    }

    suspend fun <T> query(block: suspend (Database) -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block(database) }
}
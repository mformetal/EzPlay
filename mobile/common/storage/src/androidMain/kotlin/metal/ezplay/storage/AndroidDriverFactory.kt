package metal.ezplay.storage

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import metal.ezplay.storage.AppDatabase

class AndroidDriverFactory(private val context: Context): DriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "test.db")
    }
}
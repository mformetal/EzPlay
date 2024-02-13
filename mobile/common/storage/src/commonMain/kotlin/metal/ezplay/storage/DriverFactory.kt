package metal.ezplay.storage

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

interface DriverFactory {
    fun createDriver(): SqlDriver
}

expect fun Scope.createDatabase(): AppDatabase

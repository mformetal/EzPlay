package metal.ezplay.storage

import app.cash.sqldelight.db.SqlDriver

interface DriverFactory {
  fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): AppDatabase =
  driverFactory.createDriver().run(AppDatabase::invoke)
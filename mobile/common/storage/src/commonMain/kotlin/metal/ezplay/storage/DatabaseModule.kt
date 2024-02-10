package metal.ezplay.storage

import org.koin.dsl.module

val databaseModule = module {
    single { createDatabase() }
}
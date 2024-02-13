package metal.ezplay.storage

import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun Scope.createDatabase(): AppDatabase {
    return AndroidDriverFactory(androidContext()).createDriver().run(AppDatabase::invoke)
}
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.jonasgerdes.stoppelmap.news.database.model.NewsDatabase
import org.koin.core.scope.Scope

actual fun Scope.createDriver(fileName: String): SqlDriver {
    return NativeSqliteDriver(NewsDatabase.Schema, fileName)
}
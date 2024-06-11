import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.jonasgerdes.stoppelmap.news.database.model.NewsDatabase
import org.koin.core.scope.Scope

actual fun Scope.createDriver(fileName: String): SqlDriver {
    return AndroidSqliteDriver(NewsDatabase.Schema, get<Context>(), fileName)
}
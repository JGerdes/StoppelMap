import java.io.File
import java.io.FileInputStream
import java.util.*

fun loadProperties(
    propertiesFileName: String,
    onSuccess: (properties: Properties) -> Unit = {},
    onFailure: () -> Unit = {}
) =
    File(propertiesFileName).let { propertiesFile ->
        if (propertiesFile.exists() && propertiesFile.canRead()) {
            FileInputStream(propertiesFile).use {
                with(Properties()) {
                    load(it)
                    onSuccess(this)
                }
            }
        } else {
            onFailure()
        }
    }

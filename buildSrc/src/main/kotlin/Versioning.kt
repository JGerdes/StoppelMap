import kotlinx.datetime.Clock
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toLocalDateTime
import org.gradle.api.Project
import java.io.File
import java.util.Properties

fun generateVersions(): Version {
    val now = Clock.System.now().toLocalDateTime(FixedOffsetTimeZone(UtcOffset(hours = 1)))
    val beats = ((3600 * now.hour + 60 * now.minute) / 86.4f).toInt()
    val year = now.year - 2000
    val month = now.monthNumber
    val monthPadded = month.toString().padStart(2, '0')
    val day = now.dayOfMonth
    val dayPadded = day.toString().padStart(2, '0')
    val paddedBeats = beats.toString().padStart(3, '0')
    return Version(
        name = "v$year.$monthPadded.$dayPadded.$paddedBeats",
        semanticName = "$year.$month.${(1000 * day + beats).toString().padStart(5)}",
        code = 10000000 * year + 100000 * month + 1000 * day + beats
    )
}

fun Project.loadVersions(): Version {
    val versionFile = File(rootDir, "version.properties").also {
        if (!it.exists()) it.createNewFile()
    }
    val versionProperties =
        Properties().also {
            it.load(versionFile.inputStream())
        }

    return Version(
        name = versionProperties.getProperty("name"),
        semanticName = versionProperties.getProperty("semanticName"),
        code = versionProperties.getProperty("code").toInt(),
    )
}

data class Version(val name: String, val semanticName: String, val code: Int)

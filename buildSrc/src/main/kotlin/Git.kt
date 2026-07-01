import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object Git {
    fun Project.getCommit(): Commit {
        val output = ByteArrayOutputStream()
        val process = ProcessBuilder("git", "rev-parse", "HEAD")
            .redirectErrorStream(true)
            .start()
        process.inputStream.copyTo(output)
        process.waitFor()
        val sha = output.toString().trim()
        return Commit(
            sha = sha,
            shortSha = sha.substring(0, minOf(8, sha.length)),
        )
    }
}


data class Commit(
    val sha: String,
    val shortSha: String,
)

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object Git {
    fun Project.getCommit(): Commit {
        val output = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "HEAD")
            standardOutput = output
        }
        val sha = output.toString().trim()
        return Commit(
            sha = sha,
            shortSha = sha.substring(0, 8),
        )
    }
}


data class Commit(
    val sha: String,
    val shortSha: String,
)

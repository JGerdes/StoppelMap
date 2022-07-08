import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object Git {
    val Project.commitSha: String
        get() {
            val output = ByteArrayOutputStream()
            exec {
                commandLine("git", "rev-parse", "HEAD")
                standardOutput = output
            }
            return output.toString().trim().substring(0, 8)
        }
}

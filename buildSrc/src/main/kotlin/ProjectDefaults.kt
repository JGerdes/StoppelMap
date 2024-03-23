import org.gradle.api.JavaVersion

object ProjectDefaults {
    val JAVA_COMPATIBILITY_VERSION = JavaVersion.VERSION_11
    const val KOTLIN_JVM_TARGET = "11"

    const val MINIFY_RELEASE = false
}
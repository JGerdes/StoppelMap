plugins {
    kotlin
    application
}

application {
    mainClass.set("com.jonasgerdes.stoppelmap.server.api.ApplicationKt")
}

dependencies {
    with(DependencyVersions) {
        implementation("io.ktor:ktor-server-core:$ktorServer")
        implementation("io.ktor:ktor-server-netty:$ktorServer")
        implementation("io.ktor:ktor-server-call-logging:$ktorServer")
        implementation("ch.qos.logback:logback-classic:$logback")
    }
}

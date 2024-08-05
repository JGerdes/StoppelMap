plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}



task("runPreparation", JavaExec::class) {
    group = "preparation"
    mainClass.set("com.jonasgerdes.stoppelmap.preparation.PreparationKt")
    val versions = loadVersions()
    setArgs(listOf("versionCode=${versions.code}"))
    classpath = sourceSets["main"].runtimeClasspath
}

task("fetchEvents", JavaExec::class) {
    group = "preparation"
    mainClass.set("com.jonasgerdes.stoppelmap.preparation.schedule.EventParserKt")
    classpath = sourceSets["main"].runtimeClasspath
}

task("crawlBusRoutes", JavaExec::class) {
    group = "preparation"
    mainClass.set("com.jonasgerdes.stoppelmap.preparation.transportation.crawler.CrawlBusWebsitesKt")
    classpath = sourceSets["main"].runtimeClasspath
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}


dependencies {

    implementation(project(":shared:data"))
    implementation(project(":shared:dto"))
    implementation(project(":shared:data-conversion"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json.core)
    implementation(libs.koin.core)
    implementation(libs.sqldelight.driver.sqlite)
    implementation(libs.gson)
    implementation(libs.jsoup)
}

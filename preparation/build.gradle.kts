plugins {
    id("kotlin")
    id("application")
}



task("runPreparation", JavaExec::class) {
    group = "preparation"
    mainClass.set("com.jonasgerdes.stoppelmap.preparation.PreparationKt")
    classpath = sourceSets["main"].runtimeClasspath
}

task("fetchEvents", JavaExec::class) {
    group = "preparation"
    mainClass.set("com.jonasgerdes.stoppelmap.preparation.EventParserKt")
    classpath = sourceSets["main"].runtimeClasspath
}


dependencies {

    implementation(project(":data"))

    with(DependencyVersions) {
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        implementation("io.insert-koin:koin-core:$koin")
        implementation("com.squareup.sqldelight:sqlite-driver:$sqldelight")
        implementation("com.google.code.gson:gson:2.8.4")
        implementation("com.github.filosganga:geogson-core:1.2.21")
        implementation("org.jsoup:jsoup:1.11.3")
    }
}

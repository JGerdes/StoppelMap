plugins {
    alias(libs.plugins.kotlin.jvm)
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

    implementation(libs.kotlinx.datetime)
    implementation(libs.koin.core)
    implementation(libs.sqldelight.driver.sqlite)
    implementation(libs.gson)
    implementation(libs.geogson)
    implementation(libs.jsoup)
}

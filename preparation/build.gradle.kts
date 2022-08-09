plugins {
    id("kotlin")
    id("application")
}



task("runPreparation", JavaExec::class) {
    group = "preparation"
    mainClass.set("com.jonasgerdes.stoppelmap.preparation.PreparationKt")
    classpath = sourceSets["main"].runtimeClasspath
}


dependencies {

    implementation(project(":data"))

    with(DependencyVersions) {
        //implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        implementation("io.insert-koin:koin-core:$koin")
        implementation("com.squareup.sqldelight:sqlite-driver:$sqldelight")
    }
}

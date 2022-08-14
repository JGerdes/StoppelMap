plugins {
    id("kotlin")
}


dependencies {

    with(DependencyVersions) {
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
    }
}

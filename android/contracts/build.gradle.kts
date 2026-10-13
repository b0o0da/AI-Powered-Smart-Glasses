plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}

// Fixtures live at the repo root (contracts/fixtures, tests/fixtures); tests read them in place.
tasks.test {
    systemProperty("avn.repoRoot", rootDir.parentFile.absolutePath)
}

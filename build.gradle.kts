plugins {
    kotlin("jvm") version "1.8.21"
}

group = "com.vandenbreemen.kcoderwiki"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {

    val kttVersion = "1.0.1.0000"
    implementation("com.github.kevinvandenbreemen:ktt:$kttVersion")

    val grucdVersion = "1.0.4.2003"
    implementation("com.github.kevinvandenbreemen:grucd:$grucdVersion")

    val coroutinesVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}
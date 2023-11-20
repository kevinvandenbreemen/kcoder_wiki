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

    val kttVersion = "1.0.1.1000"
    implementation("com.github.kevinvandenbreemen:ktt:$kttVersion")

    val grucdPlantVersion = "1.0.0.2000"
    implementation("com.github.kevinvandenbreemen:grucd-plantuml:$grucdPlantVersion")

    val grucdVersion = "1.0.6.2000"
    implementation("com.github.kevinvandenbreemen:grucd:$grucdVersion")

    val coroutinesVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    val cache4KVersion = "0.12.0"
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4KVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

val fatJar = task("FatJar", type = Jar::class) {

    val jarName = "kcoder_wiki.jar"

    archiveFileName.set(jarName)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes["Main-Class"] = "com.vandenbreemen.kcoderwiki.Main"

    }
    from(configurations.runtimeClasspath.get().map {
        if(it.isDirectory) it else zipTree(it)
    })
    with(tasks.jar.get() as CopySpec)

    copy {
        from("./build/libs/$jarName")
        into("./")
    }
    println("Built and copied $jarName")
}
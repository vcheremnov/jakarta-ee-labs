import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm") version kotlinVersion apply false
}

allprojects {
    group = "ru.nsu.ccfit.cheremnov"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xallow-result-return-type")
            jvmTarget = "11"
        }
    }
}

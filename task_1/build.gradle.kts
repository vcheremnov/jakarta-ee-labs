plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "${group}.AppKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":osmdata"))
    implementation("org.apache.commons:commons-compress:1.20")
    implementation("org.apache.logging.log4j:log4j-api:2.14.0")
    implementation("org.apache.logging.log4j:log4j-core:2.14.0")
}

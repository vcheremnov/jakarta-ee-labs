plugins {
    java
    kotlin("jvm")
    application
    id("com.github.jacobono.jaxb") version "1.3.5"
}

application {
    mainClassName = "${group}.AppKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":osmdata"))

    implementation("org.apache.logging.log4j:log4j-api:2.14.0")
    implementation("org.apache.logging.log4j:log4j-core:2.14.0")

    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
    jaxb("org.glassfish.jaxb:jaxb-xjc:2.3.1")
}

jaxb {
    xsdDir = "${project.name}/src/main/resources"
    xjc {
        destinationDir = "src/main/java/"
        generatePackage = "${group}.model.generated"
    }
}
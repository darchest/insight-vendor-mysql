plugins {
    kotlin("jvm") version "2.0.10"
    `java-library`
}

group = "org.darchest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":insight"))

    implementation("com.mysql:mysql-connector-j:9.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}
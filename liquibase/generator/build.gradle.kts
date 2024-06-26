plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.ajulay"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.4")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.4")

}

kotlin {
    jvmToolchain(21)
}

tasks.register<JavaExec>("runSQLGenerator") {
    group = "build"
    description = "Run the SQL Generator"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.ajulay.generator.SQLGeneratorKt")
}

// Ensure the SQL generator runs before the build task
tasks.named("build") {
    dependsOn("runSQLGenerator")
}
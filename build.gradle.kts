plugins {
    application
    java
}

group = "com.mirchr"
version = providers.gradleProperty("version").getOrElse("0.0.0-SNAPSHOT")

val jdkVersion = providers.gradleProperty("jdkVersion").getOrElse("25").toInt()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(jdkVersion)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "DumpTomcatSessions"
}

tasks.jar {
    archiveBaseName = "DumpTomcatSessions"
    archiveClassifier = "jdk$jdkVersion"
    manifest {
        attributes(
            "Main-Class" to "DumpTomcatSessions",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Build-Jdk-Spec" to jdkVersion.toString(),
        )
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all")
}

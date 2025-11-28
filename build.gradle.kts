plugins {
    id("java")
}

group = "com.jodexindustries.vertexfx"
version = "0.0.1"

repositories {
    mavenCentral()
}

tasks.jar {
    archiveClassifier.set("beta")
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2-1")
    annotationProcessor("org.jetbrains:annotations:26.0.2-1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
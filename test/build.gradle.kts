plugins {
    id("java")
}

group = "com.jodexindustries.vertexfx"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(parent!!)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.jodexindustries.vertexfx.LinearInterpolationTest"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

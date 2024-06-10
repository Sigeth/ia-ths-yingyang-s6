plugins {
    id("java")
    application
}

group = "fr.isen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "fr.isen.iathsproject.Main"
}

tasks.jar {
    manifest.attributes["Main-Class"] = application.mainClass
}

tasks.test {
    useJUnitPlatform()
}
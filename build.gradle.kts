plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.redisson:redisson:3.27.2")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

configurations.all {
    exclude(group = "org.slf4j", module = "slf4j-log4j12")
    exclude(group = "org.slf4j", module = "slf4j-jdk14")
    exclude(group = "org.slf4j", module = "slf4j-simple")
    exclude(group = "org.slf4j", module = "slf4j-nop")
    exclude(group = "org.slf4j", module = "slf4j-jcl")
}

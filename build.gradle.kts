plugins {
    id("java")
}

group = "org"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.assertj:assertj-core:3.27.6")

    // Apache FileUtils
    implementation("commons-io:commons-io:2.21.0")
    //testImplementation("commons-io:commons-io:2.21.0")

    // ObjectMapper
    //implementation("com.fasterxml.jackson.core:jackson-databind:3.0.3")
    implementation("tools.jackson.core:jackson-databind:3.0.3")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
}
plugins {
    id("java")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "WW2BAM"
    }
}



group = "me.WesleyH"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20220320")
}

tasks.test {
    useJUnitPlatform()
}
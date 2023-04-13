
plugins {
    kotlin("jvm") version "1.5.30"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.31"
    application
}

group = "com.converter"
version = "0.0.1"
application {
    mainClass.set("com.converter.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    val kotlin_version = "1.5.30"
    val ktor_version = "1.6.3"
    val koin_version = "2.2.3"

    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("org.slf4j:slf4j-api:1.7.32")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("junit:junit:4.13.1")
    implementation("org.json:json:20210307")
    implementation("com.google.guava:guava:11.0.2")
    implementation ("redis.clients:jedis:3.4.0")
    implementation ("io.insert-koin:koin-core:$koin_version")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation ("org.jobrunr:jobrunr:6.1.1")
    testImplementation("org.mockito:mockito-core:2.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.assertj:assertj-core:3.6.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
}
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
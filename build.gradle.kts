val jvmVersion by extra("11")
val junitVersion by extra("4.12")
val kotlinVersion by extra("1.5.0")
val nettyVersion by extra("3.2.10.Final")
val fastUtilVersion by extra("8.5.4")
val koinVersion by extra("3.0.1")
val mysqlVersion by extra("5.0.5")
val xpp3Version by extra("1.1.4c")
val xStreamVersion by extra("1.4.1")

plugins {
    java
    application
    kotlin("jvm") version "2.0.20"
}

group = "nb"
version = "1.0-0"

repositories {
    jcenter()
    mavenCentral()
}

fun DependencyHandlerScope.localImpl(fileName: String, path: String = "lib/") = implementation(files("$path$fileName"))

fun DependencyHandlerScope.localJar(fileName: String, path: String = "lib/") = localImpl("$fileName.jar", path)

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("scripting-common"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("reflect"))
    implementation("it.unimi.dsi:fastutil:$fastUtilVersion")
    implementation("mysql:mysql-connector-java:$mysqlVersion")
    implementation("org.jboss.netty:netty:$nettyVersion")
    implementation("xpp3:xpp3:$xpp3Version")
    implementation("com.thoughtworks.xstream:xstream:$xStreamVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    localJar("upnp")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
    compilerOptions {
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xjvm-default=all",
        )
    }
}
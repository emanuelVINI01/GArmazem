import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

group = "com.emanuelvini"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
    implementation ("fr.minuskube.inv:smart-invs:1.2.7")
    implementation ("com.github.SaiintBrisson.command-framework:bukkit:1.3.1")
    implementation("de.tr7zw:item-nbt-api:2.11.3")
    implementation ("com.github.HenryFabio:sql-provider:9561f20fd2")
    implementation("com.github.HenryFabio.configuration-injector:bukkit:1.0.2")
    implementation("com.github.ben-manes.caffeine:jcache:3.1.8")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}
tasks.withType<ShadowJar> {
    relocate("de.tr7zw.changeme.nbtapi", "com.emanuelvini.garmazem.nbtapi")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

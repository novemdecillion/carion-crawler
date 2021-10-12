import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  java

  id("org.springframework.boot") version "2.5.5" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
  kotlin("jvm") version "1.5.31" apply false
  kotlin("plugin.spring") version "1.5.31" apply false
}

subprojects {
  apply {
    plugin("io.spring.dependency-management")
    plugin("org.jetbrains.kotlin.jvm")
    plugin("org.jetbrains.kotlin.plugin.spring")
  }

  group = "io.github.novemdecillion"
  version = "0.0.1-SNAPSHOT"
  java.sourceCompatibility = JavaVersion.VERSION_11

  repositories {
    mavenCentral()
  }


  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.12.0")
  }

  the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
    imports {
      mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
      mavenBom("org.testcontainers:testcontainers-bom:1.16.0")
    }
    dependencies {
      dependency("com.codeborne:selenide:5.25.0")
    }
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "11"
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

idea {
  module {
    isDownloadSources = true
  }
}

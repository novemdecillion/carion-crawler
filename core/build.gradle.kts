import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType

plugins {
  id("io.github.novemdecillion.jooq-generator")
}

sourceSets.main {
  java.srcDir("src/main/jooq")
}

dependencies {
  api("org.springframework.boot:spring-boot-starter-jooq")
  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.postgresql:postgresql")
}

jooqGenerator {
  driver = "org.testcontainers.jdbc.ContainerDatabaseDriver"
  url = "jdbc:tc:postgresql:11:///build-test"
  user = "admin"
  password = "password123"
  packageName = "io.github.novemdecillion.carioncrawler.adapter.jooq"
//  appendForcedTypes = listOf(
//    ForcedType()
//      .also {
//        it.isEnumConverter = true
//        it.includeExpression = """CLIENT\.TYPE"""
//        it.userType = "jp.co.supportas.domain.ClientType"
//      })
}
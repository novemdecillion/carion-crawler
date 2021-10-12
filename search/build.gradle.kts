import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType

apply {
  plugin("org.springframework.boot")
}

dependencies {
  implementation(project(":core"))

  implementation("org.springframework.boot:spring-boot-starter")
  implementation("com.codeborne:selenide")
  "developmentOnly"("org.springframework.boot:spring-boot-devtools")
}

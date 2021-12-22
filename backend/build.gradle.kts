apply {
  plugin("org.springframework.boot")
}

dependencies {
  implementation(project(":core"))

  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:12.0.0")
  implementation("com.graphql-java:graphql-java-extended-scalars:17.0")

  implementation("com.nimbusds:nimbus-jose-jwt:9.15.2")

  implementation("org.apache.commons:commons-lang3")

  // Tool
  "developmentOnly"("org.springframework.boot:spring-boot-devtools")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
}


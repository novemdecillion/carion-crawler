package io.github.novemdecillion.carioncrawler.adapter.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConfigurationProperties(prefix = "app.jwt")
@ConstructorBinding
data class AppJwtProperties(
  val secret: String,
  val expiration: Duration = Duration.ofMinutes(30),
  val refresh: Duration = Duration.ofMinutes(5)
)

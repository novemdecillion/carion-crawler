package io.github.novemdecillion.carioncrawler.adapter.search

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.LocalDate

@ConfigurationProperties(prefix = "app.google-search")
@ConstructorBinding
data class GoogleSearchProperties (
  val startAt: LocalDate,
  val keywords: List<String>,
  val excludeUrlStartWith: List<String>
)
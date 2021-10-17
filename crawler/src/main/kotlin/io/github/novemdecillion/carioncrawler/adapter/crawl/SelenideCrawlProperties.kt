package io.github.novemdecillion.carioncrawler.adapter.crawl

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.LocalDate

@ConfigurationProperties(prefix = "app.selenide-crawl")
@ConstructorBinding
data class SelenideCrawlProperties (
  val storeFolder: String
)
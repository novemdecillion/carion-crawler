package io.github.novemdecillion.carioncrawler

import io.github.novemdecillion.carioncrawler.adapter.crawl.SelenideCrawlService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CrawlApplication(val crawlService: SelenideCrawlService)

fun main(args: Array<String>) {
  runApplication<CrawlApplication>(*args)
}

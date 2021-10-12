package io.github.novemdecillion.carioncrawler

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import io.github.novemdecillion.carioncrawler.adapter.search.GoogleSearchService

@SpringBootApplication
@ConfigurationPropertiesScan
class SearchApplication

fun main(args: Array<String>) {
  runApplication<SearchApplication>(*args).registerShutdownHook()
}

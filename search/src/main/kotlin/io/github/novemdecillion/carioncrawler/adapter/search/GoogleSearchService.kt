package io.github.novemdecillion.carioncrawler.adapter.search

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import io.github.novemdecillion.carioncrawler.adapter.db.SearchedPageRepository
import io.github.novemdecillion.carioncrawler.adapter.db.StateRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class GoogleSearchService(val searchProperties: GoogleSearchProperties, val searchedRepository: SearchedPageRepository,
                          val stateRepository: StateRepository) {
  companion object {
    val log = LoggerFactory.getLogger(GoogleSearchService::class.java)
    val DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
  }

  @Scheduled(cron = "\${app.google-search.cron}")
  @EventListener(ApplicationReadyEvent::class)
  fun search() {
    try {
      Configuration.headless = true
      Configuration.startMaximized = true

      val searchStartDate = stateRepository
        .getSearchedDate(searchProperties.startAt.minusDays(1))
        .plusDays(1)

      searchProperties.keywords
        .forEach {
          search(it, searchStartDate)
        }

      stateRepository.setSearchedDate(LocalDate.now().minusDays(1))
    } catch (ex: Exception) {
      log.error("検索エラー", ex)
    }
  }

  fun search(keyword: String, searchStartDate: LocalDate) {
    val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8)
    val encodedPeriod = URLEncoder.encode("cdr:1,cd_min:${searchStartDate.format(DATE_PATTERN)},cd_max:", StandardCharsets.UTF_8)

    Selenide.open("https://www.google.com/search?q=${encodedKeyword}&tbs=${encodedPeriod}")
    while (true) {
      enumerateLink()

      val pNext = Selenide.`$`("#pnnext")
      if (!pNext.`is`(Condition.exist)) {
        break
      }
      pNext.click()
    }
    Selenide.closeWebDriver()
  }

  fun enumerateLink() {
    Selenide.`$`("#search").should(Condition.appear)
    Selenide.`$$`("#search a[href]")
      .mapNotNull { link ->
        link.attr("href")
      }
      .filter { link ->
        null == searchProperties.excludeUrlStartWith.find { link.startsWith(it) }
      }
      .forEach {
        searchedRepository.insert(it)
      }
  }
}
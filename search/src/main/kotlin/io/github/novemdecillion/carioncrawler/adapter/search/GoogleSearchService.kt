package io.github.novemdecillion.carioncrawler.adapter.search

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import io.github.novemdecillion.carioncrawler.adapter.db.SearchKeywordRepository
import io.github.novemdecillion.carioncrawler.adapter.db.SearchedPageRepository
import org.openqa.selenium.NoSuchElementException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

@Service
class GoogleSearchService(val searchProperties: GoogleSearchProperties, val searchedRepository: SearchedPageRepository,
                          val keywordRepository: SearchKeywordRepository, val transactionTemplate: TransactionTemplate) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(GoogleSearchService::class.java)
    val DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
  }

  @Scheduled(cron = "\${app.google-search.cron}")
  @EventListener(ApplicationReadyEvent::class)
  fun search() {
    try {
      Configuration.headless = true
      Configuration.startMaximized = true
      Configuration.timeout = 10_000
      System.setProperty("chromeoptions.args", "--no-zygote")
      
      transactionTemplate
        .execute {
          keywordRepository.selectAll()
        }
        ?.forEach { keywordEntity ->

          val searchStartDate = (keywordEntity.searchedAt ?: searchProperties.startAt.minusDays(1))
            .plusDays(1)

          // 開始日が今なら、もう検索済み
          if (searchStartDate == LocalDate.now()) {
            return
          }
          search(keywordEntity.keyword!!, searchStartDate)

          transactionTemplate
            .execute {
              keywordRepository.updateSearchAt(keywordEntity.keyword!!, LocalDate.now().minusDays(1))
            }
        }
    } catch (ex: Exception) {
      log.error("検索エラー", ex)
    }
  }

  fun search(keyword: String, searchStartDate: LocalDate) {
    val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8)
    val encodedPeriod = URLEncoder.encode("cdr:1,cd_min:${searchStartDate.format(DATE_PATTERN)},cd_max:", StandardCharsets.UTF_8)

    Selenide.open("https://www.google.com/search?q=${encodedKeyword}&tbs=${encodedPeriod}&;filter=0")
    while (true) {
      if (!enumerateLink()) {
        break
      }

      // ロボット判定回避のため5〜10秒スリープ
      Thread.sleep(Random(Date().time).nextLong(5_000, 10_000))

      // 次ページ操作
      val pNext = Selenide.`$`("#pnnext")
      if (!pNext.`is`(Condition.exist)) {
        break
      }
      pNext.click()
    }
    Selenide.closeWebDriver()
  }

  fun enumerateLink(): Boolean {
    try {
      Selenide.`$`("#search").should(Condition.appear)
    } catch (ex: NoSuchElementException) {
      if (Selenide.`$`("#captcha-form").`is`(Condition.exist)) {
        /*
          おそらく
          Our systems have detected unusual traffic from your computer network.
          This page checks to see if it's really you sending the requests, and not a robot.
          が表示された。
        */
        log.info("ロボット判定が表示された。", ex)
      } else {
        log.error("検索結果の取得に失敗。", ex)
      }
      return false
    }

    Selenide.`$$`("#search a[href]")
      .mapNotNull { link ->
        link.attr("href")
      }
      .filter { link ->
        null == searchProperties.excludeUrlStartWith.find { link.startsWith(it) }
      }
      .forEach { link ->
        transactionTemplate
          .execute {
            searchedRepository.insert(link)
          }
      }
    return true
  }
}
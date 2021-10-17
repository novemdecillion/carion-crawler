package io.github.novemdecillion.carioncrawler.adapter.crawl

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import io.github.novemdecillion.carioncrawler.adapter.db.CrawledPageRepository
import io.github.novemdecillion.carioncrawler.adapter.db.CrawledStatus
import io.github.novemdecillion.carioncrawler.adapter.db.SearchKeywordRepository
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime

@Service
class SelenideCrawlService(val crawlProperties: SelenideCrawlProperties,
                           val transactionTemplate: TransactionTemplate,
                           val keywordRepository: SearchKeywordRepository,
                           val crawledRepository: CrawledPageRepository) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(SelenideCrawlService::class.java)
  }

  val visited = mutableSetOf<String>()
  val keywords = mutableSetOf<String>()

  @Scheduled(cron = "\${app.selenide-crawl.cron}")
  @EventListener(ApplicationReadyEvent::class)
  fun crawl() {
    Configuration.headless = true
    Configuration.startMaximized = true
    Configuration.downloadsFolder = crawlProperties.storeFolder

    transactionTemplate
      .execute {
        keywords.clear()
        keywords.addAll(
          keywordRepository.selectAll()
            .flatMap { it.keyword!!.split(StringUtils.SPACE) })
        crawledRepository.selectCrawlTargetUrl()
      }
      ?.forEach {
        try {
          val folder = URLEncoder.encode(it.url!!, StandardCharsets.UTF_8)
          Configuration.reportsFolder = "${crawlProperties.storeFolder}/$folder"

          visited.clear()
          crawl(it.url!!, it.createAt!!)
        } catch (ex: Exception) {
          log.error("${it.url}への巡回でエラーが発生しました。", ex)
        } finally {
          Selenide.closeWebDriver()
        }
      }
  }

  fun crawl(url: String, searchedAt: OffsetDateTime, prevUrl: String? = null, level: Int = 0) {
    // 訪問済み?
    if (visited.contains(url)) {
      return
    }
    visited.add(url)

    val uri = URI(url)
    if ((uri.host != null)
      && (prevUrl != null)
      && (uri.host != URI(prevUrl).host)
    ) {
      transactionTemplate
        .execute {
          crawledRepository.insertOrUpdate(url, CrawledStatus.DIFFERENT_HOST, searchedAt)
        }
      return
    }

    try {
      Selenide.open(url)
    } catch (ex: Exception) {
      log.error("${url}へのアクセスでエラーが発生しました。", ex)
      transactionTemplate
        .execute {
          crawledRepository.insertOrUpdate(url, CrawledStatus.ERROR, searchedAt, ex.localizedMessage)
        }
      return
    }

    val currentUrl = WebDriverRunner.getWebDriver().currentUrl

    // URLが変わらないので、おそらくダウンロードが行われた。
    if (prevUrl == currentUrl) {
      transactionTemplate
        .execute {
          crawledRepository.insertOrUpdate(currentUrl, CrawledStatus.DOWNLOAD, searchedAt)
        }
      return
    }

    // ページにキーワードが存在しない
    val allText = Selenide.element("body").text()
    if(!keywords.any { allText.contains(it) } ) {
      transactionTemplate
        .execute {
          crawledRepository.insertOrUpdate(currentUrl, CrawledStatus.NO_KEYWORD, searchedAt)
        }
      return
    }

    val file = URLEncoder.encode(currentUrl, StandardCharsets.UTF_8)
    Selenide.screenshot(file)
    transactionTemplate
      .execute {
        crawledRepository.insertOrUpdate(currentUrl, CrawledStatus.SUCCESS, searchedAt)
      }

    Selenide.`$$`("a[href]")
      .mapNotNull {
        it.attr("href")
      }
      .filter {
        when(URI(it).scheme?.lowercase()) {
          null, "http", "https" -> true
          else -> false
        }
      }
      .forEach {
        crawl(it, searchedAt, currentUrl,level + 1)
      }
  }
}
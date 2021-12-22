package io.github.novemdecillion.carioncrawler.adapter.crawl

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import io.github.novemdecillion.carioncrawler.adapter.db.CrawledPageRepository
import io.github.novemdecillion.carioncrawler.adapter.db.CrawledStatus
import io.github.novemdecillion.carioncrawler.adapter.db.SearchKeywordRepository
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.CrawledPageEntity
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.OutputType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.net.URI
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
    Configuration.reportsFolder = crawlProperties.storeFolder

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
    val crawledPageEntity = CrawledPageEntity(url = url, searchedAt = searchedAt)

    // 別ホストへのリンク?
    val uri = URI(url)
    if ((uri.host != null)
      && (prevUrl != null)
      && (uri.host != URI(prevUrl).host)
    ) {
      transactionTemplate
        .execute {
          crawledPageEntity.status = CrawledStatus.DIFFERENT_HOST
          crawledRepository.insertOrUpdate(crawledPageEntity)
        }
      return
    }

    // 訪問済み?
    if (visited.contains(url)) {
      return
    }
    visited.add(url)

    // 再起呼出オーバー
    if (crawlProperties.maxDepth < level) {
      transactionTemplate
        .execute {
          crawledRepository.selectByUrl(url)
            ?: run {
              crawledPageEntity.status = CrawledStatus.EXCEED_DEPTH
              crawledRepository.insertOrUpdate(crawledPageEntity)
            }
        }
      return
    }

    // アクセス
    try {
      Selenide.open(url)
    } catch (ex: Exception) {
      log.error("${url}へのアクセスでエラーが発生しました。", ex)
      transactionTemplate
        .execute {
          crawledPageEntity.status = CrawledStatus.ERROR
          crawledRepository.insertOrUpdate(crawledPageEntity)
        }
      return
    }

    // URLが変わらないので、おそらくダウンロードが行われた。
    if (prevUrl == url) {
      transactionTemplate
        .execute {
          crawledPageEntity.status = CrawledStatus.DOWNLOAD
          crawledRepository.insertOrUpdate(crawledPageEntity)
        }
      return
    }

    crawledPageEntity.html = WebDriverRunner.source()
    crawledPageEntity.data = Selenide.screenshot(OutputType.BYTES)

    // ページにキーワードが存在しない
    if(keywords.any { crawledPageEntity.html?.contains(it) != true }) {
      transactionTemplate
        .execute {
          crawledPageEntity.status = CrawledStatus.NO_KEYWORD
          crawledRepository.insertOrUpdate(crawledPageEntity)
        }
      return
    }

    transactionTemplate
      .execute {
        crawledPageEntity.status = CrawledStatus.SUCCESS
        crawledRepository.insertOrUpdate(crawledPageEntity)

      }

    Selenide.`$$`("a[href]")
      .mapNotNull {
        it.attr("href")
      }
      .mapNotNull {
        try {
          URI(it)
        } catch (ex: Exception) {
          log.error("URL解析エラー", ex)
          null
        }
      }
      .map {
        // フラグメント削除
        URI(it.scheme, it.schemeSpecificPart, null)
      }
      .filter {
        when(it.scheme?.lowercase()) {
          "http", "https" -> true
          else -> false
        }
      }
      .forEach {
        crawl(it.toString(), searchedAt, url,level + 1)
      }
  }
}
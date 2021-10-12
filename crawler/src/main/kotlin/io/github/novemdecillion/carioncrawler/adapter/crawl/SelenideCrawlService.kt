package io.github.novemdecillion.carioncrawler.adapter.crawl

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import org.openqa.selenium.OutputType
import org.springframework.stereotype.Service
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path

@Service
class SelenideCrawlService {
  val visited = mutableSetOf<String>()

  fun crawl() {
    Configuration.headless = true
    Configuration.startMaximized = true

    crawl("https://www.oriconsul.com/")
    Selenide.closeWebDriver()
  }

  fun crawl(url: String, prevUrl: String? = null, level: Int = 0) {
    // 訪問済み?
    if (visited.contains(url)) {
      return
    }
    visited.add(url)

    val folder = URLEncoder.encode(url, StandardCharsets.UTF_8)
    Configuration.downloadsFolder = "reports/$folder"
    Configuration.reportsFolder = "reports"

    try {
      Selenide.open(url)
    } catch (ex: Exception) {
      // TODO エラーを記録して終了
      return
    }

    // URLが変わらないので、おそらくダウンロードが行われた。
    if (prevUrl == WebDriverRunner.getWebDriver().currentUrl) {
      return
    }

//    val pageSource = WebDriverRunner.getWebDriver().pageSource
    Selenide.screenshot(folder)

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
        crawl(it, url,level + 1)
      }
  }
}
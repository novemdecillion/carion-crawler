package io.github.novemdecillion.carioncrawler.adapter.web

import io.github.novemdecillion.carioncrawler.adapter.db.CrawledPageRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/public")
class ResourceController(private val crawledPageRepository: CrawledPageRepository) {
  @GetMapping("/crawled")
  fun crawledPageSnapshot(@RequestParam url: String): ResponseEntity<*> {
    val data = crawledPageRepository.selectByUrl(url)?.data
      ?: return ResponseEntity.notFound().build<Unit>()

    val headers = HttpHeaders()
    headers.contentType = MediaType.IMAGE_PNG
    return ResponseEntity(data, headers, HttpStatus.OK)
  }
}
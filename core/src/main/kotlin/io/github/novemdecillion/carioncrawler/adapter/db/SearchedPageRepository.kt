package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.CrawledPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchedPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.CRAWLED_PAGE
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCHED_PAGE
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCH_KEYWORD
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class SearchedPageRepository(val dsl: DSLContext) {
  fun insert(url: String) {
    val record = SearchedPageRecord(url = url, createAt = OffsetDateTime.now())
    dsl.insertInto(SEARCHED_PAGE).set(record)
      .onConflict(SEARCHED_PAGE.URL)
      .doNothing()
      .execute()
  }

  fun selectAll(): Result<SearchedPageRecord> {
    return dsl.fetch(SEARCHED_PAGE)
  }

  fun select(limit: Int?, offset: Int?) : List<SearchedPageRecord> {
    return dsl.selectFrom(SEARCHED_PAGE)
      .orderBy(SEARCHED_PAGE.CREATE_AT.desc())
      .let {
        if (null != limit) {
          if (null != offset) {
            it.limit(limit).offset(offset)
          } else {
            it.limit(limit)
          }
        } else it
      }
      .fetch().toList()
  }

  fun count(): Int {
    return dsl.fetchCount(SEARCHED_PAGE)
  }
}
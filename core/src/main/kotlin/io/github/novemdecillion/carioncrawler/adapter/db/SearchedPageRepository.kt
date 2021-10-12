package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchedPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCHED_PAGE
import org.jooq.DSLContext
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
}
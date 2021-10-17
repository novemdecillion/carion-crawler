package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.SearchKeywordEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchKeywordRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCH_KEYWORD
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class SearchKeywordRepository(val dsl: DSLContext) {
  fun selectAll(): List<SearchKeywordEntity> {
    return dsl.fetch(SEARCH_KEYWORD).into(SearchKeywordEntity::class.java)
  }

  fun updateSearchAt(keyword: String, date: LocalDate): Int {
    return dsl.executeUpdate(SearchKeywordRecord(keyword, date))
  }
}
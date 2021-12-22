package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.SearchKeywordEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchKeywordRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchedPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCHED_PAGE
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

  fun select(limit: Int?, offset: Int?) : List<SearchKeywordRecord> {
    return dsl.selectFrom(SEARCH_KEYWORD)
      .orderBy(SEARCH_KEYWORD.KEYWORD)
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
    return dsl.fetchCount(SEARCH_KEYWORD)
  }

  fun insert(keyword: String): Int {
    return dsl.executeInsert(SearchKeywordRecord().also { it.keyword = keyword })
  }

  fun delete(keyword: String): Int {
    return dsl.executeDelete(SearchKeywordRecord().also { it.keyword = keyword })
  }

}
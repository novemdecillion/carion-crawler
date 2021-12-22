package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.STATE
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class StateRepository(val dsl: DSLContext) {
  companion object {
    const val CRAWLED_PAGE_URL_FILTERS = "crawled page url filters"
  }

  fun setCrawledPageUrlFilters(urlFilters: Collection<String>): Int {
    val value = if (urlFilters.isEmpty()) null else urlFilters.joinToString()
    return dsl.insertInto(STATE)
      .set(STATE.KEY, CRAWLED_PAGE_URL_FILTERS)
      .set(STATE.VALUE, value)
      .onConflict(STATE.KEY).doUpdate().set(STATE.VALUE, value)
      .execute()
  }

  fun getCrawledPageUrlFilters(): List<String> {
    return dsl.selectFrom(STATE)
      .where(STATE.KEY.equal(CRAWLED_PAGE_URL_FILTERS))
      .fetchOne()
      ?.get(STATE.VALUE)
      ?.split(", ")
      ?: emptyList()
  }

}
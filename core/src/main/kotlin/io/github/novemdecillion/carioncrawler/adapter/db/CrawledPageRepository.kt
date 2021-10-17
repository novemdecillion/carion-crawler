package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.CrawledPageEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.SearchedPageEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.CrawledPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchedPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.CRAWLED_PAGE
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCHED_PAGE
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class CrawledPageRepository(val dsl: DSLContext) {
  fun insertOrUpdate(entity: CrawledPageEntity): Int {
    val record = entity.into(CrawledPageRecord())
    if (entity.exclude == null) {
      record.reset(CRAWLED_PAGE.EXCLUDE)
    }
    return dsl.insertInto(CRAWLED_PAGE).set(record)
      .onConflict(CRAWLED_PAGE.URL)
      .doUpdate()
      .set(record)
      .execute()
  }

  fun selectByUrl(url: String): CrawledPageEntity? {
    return dsl.fetchOne(CRAWLED_PAGE, CRAWLED_PAGE.URL.equal(url))?.into(CrawledPageEntity::class.java)
  }

  fun selectCrawlTargetUrl(): List<SearchedPageEntity> {
    return dsl.select(SEARCHED_PAGE.asterisk())
      .from(SEARCHED_PAGE)
      .leftJoin(CRAWLED_PAGE).on(SEARCHED_PAGE.URL.equal(CRAWLED_PAGE.URL))
      .where(CRAWLED_PAGE.URL.isNull.or(SEARCHED_PAGE.CREATE_AT.notEqual(CRAWLED_PAGE.CRAWLED_AT)))
      .fetchInto(SearchedPageEntity::class.java)
  }

}
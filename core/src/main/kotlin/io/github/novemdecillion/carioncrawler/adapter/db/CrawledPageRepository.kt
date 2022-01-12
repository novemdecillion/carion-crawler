package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.CrawledPageEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.SearchedPageEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.CrawledPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.CRAWLED_PAGE
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.SEARCHED_PAGE
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class CrawledPageRepository(val dsl: DSLContext) {
  companion object {
    const val FILTER_NOT = "!"
    const val FILTER_LIKE = "%"
  }

  fun insertOrUpdate(entity: CrawledPageEntity): Int {
    val record = entity.into(CrawledPageRecord())
    if (entity.exclude == null) {
      record.reset(CRAWLED_PAGE.EXCLUDE)
    }
    if (entity.crawledAt == null) {
      record.crawledAt = OffsetDateTime.now()
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
      .where(CRAWLED_PAGE.URL.isNull.or(SEARCHED_PAGE.CREATE_AT.notEqual(CRAWLED_PAGE.SEARCHED_AT)))
      .fetchInto(SearchedPageEntity::class.java)
  }

  fun select(urlFilters: Collection<String>?, status: Collection<CrawledStatus>?, limit: Int?, offset: Int?) : List<CrawledPageRecord> {
    return dsl.selectFrom(CRAWLED_PAGE)
      .let { statement ->
        val conditions = mutableListOf<Condition>()
        urlFilters
          ?.let {
            urlFiltersConditions(it)
          }
          ?.also { conditions.addAll(it) }

        if (!status.isNullOrEmpty()) {
          conditions.add(CRAWLED_PAGE.STATUS.`in`(status))
        }

        if (conditions.isNotEmpty()) {
          statement.where(conditions)
        } else statement
      }
      .orderBy(CRAWLED_PAGE.CRAWLED_AT.desc())
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

  fun count(urlFilters: Collection<String>?, status: Collection<CrawledStatus>?): Int {
    val conditions = mutableListOf<Condition>()
    urlFilters
      ?.let {
        urlFiltersConditions(it)
      }
      ?.also { conditions.addAll(it) }

    if (!status.isNullOrEmpty()) {
      conditions.add(CRAWLED_PAGE.STATUS.`in`(status))
    }
    return if (conditions.isNotEmpty()) {
      dsl.fetchCount(CRAWLED_PAGE, conditions)
    } else dsl.fetchCount(CRAWLED_PAGE)
  }

  fun urlFiltersConditions(urlFilters: Collection<String>?): List<Condition>? {
    return urlFilters
      ?.mapNotNull { urlFilter ->
        when {
          !urlFilter.startsWith(FILTER_NOT) && !urlFilter.contains(FILTER_LIKE) -> CRAWLED_PAGE.URL.equal(urlFilter)
          !urlFilter.startsWith(FILTER_NOT) && urlFilter.contains(FILTER_LIKE) -> CRAWLED_PAGE.URL.like(urlFilter)
          urlFilter.startsWith(FILTER_NOT) && !urlFilter.contains(FILTER_LIKE) -> CRAWLED_PAGE.URL.notEqual(urlFilter.removePrefix(FILTER_NOT))
          urlFilter.startsWith(FILTER_NOT) && urlFilter.contains(FILTER_LIKE) -> CRAWLED_PAGE.URL.notLike(urlFilter.removePrefix(FILTER_NOT))
          else -> null
        }
      }
  }

}
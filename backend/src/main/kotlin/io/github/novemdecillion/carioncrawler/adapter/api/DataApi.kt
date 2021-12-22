package io.github.novemdecillion.carioncrawler.adapter.api

import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import io.github.novemdecillion.carioncrawler.adapter.db.*
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.CrawledPageRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchKeywordRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchedPageRecord
import io.github.novemdecillion.carioncrawler.adapter.security.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class DataApi(
  private val crawledPageRepository: CrawledPageRepository,
  private val searchedPageRepository: SearchedPageRepository,
  private val searchKeywordRepository: SearchKeywordRepository,
  private val stateRepository: StateRepository
) : GraphQLQueryResolver, GraphQLMutationResolver {

  @PreAuthorize(AUTHENTICATED)
  fun searchedPageCount(): Int {
    return searchedPageRepository.count()
  }

  @PreAuthorize(AUTHENTICATED)
  fun searchedPages(limit: Int?, offset: Int?): List<SearchedPageRecord> {
    return searchedPageRepository.select(limit, offset)
  }

  @PreAuthorize(AUTHENTICATED)
  fun searchKeywordCount(): Int {
    return searchKeywordRepository.count()
  }

  @PreAuthorize(AUTHENTICATED)
  fun searchKeywords(limit: Int?, offset: Int?): List<SearchKeywordRecord> {
    return searchKeywordRepository.select(limit, offset)
  }

  @PreAuthorize(AUTHENTICATED)
  fun addSearchKeyword(keyword: String): Boolean {
    return 1 == searchKeywordRepository.insert(keyword)
  }

  @PreAuthorize(AUTHENTICATED)
  fun deleteSearchKeyword(keyword: String): Boolean {
    return 1 == searchKeywordRepository.delete(keyword)
  }

  @PreAuthorize(AUTHENTICATED)
  fun crawledPageCount(status: Collection<CrawledStatus>?, enableUrlFilter: Boolean?): Int {
    val urlFilters = if (enableUrlFilter == true) {
      stateRepository.getCrawledPageUrlFilters()
    } else null
    return crawledPageRepository.count(urlFilters, status)
  }

  @PreAuthorize(AUTHENTICATED)
  fun crawledPages(status: Collection<CrawledStatus>?, enableUrlFilter: Boolean?, limit: Int?, offset: Int?): List<CrawledPageRecord> {
    val urlFilters = if (enableUrlFilter == true) {
      stateRepository.getCrawledPageUrlFilters()
    } else null

    return crawledPageRepository.select(urlFilters, status, limit, offset)
  }

  @PreAuthorize(AUTHENTICATED)
  fun getCrawledPageUrlFilters(): List<String> {
    return stateRepository.getCrawledPageUrlFilters()
  }

  @PreAuthorize(AUTHENTICATED)
  fun setCrawledPageUrlFilters(urlFilters: List<String>): Boolean {
    return 1 == stateRepository.setCrawledPageUrlFilters(urlFilters)
  }
}

@Component
class CreditorWithClaimFormResolver : GraphQLResolver<CrawledPageRecord> {
  fun existData(record: CrawledPageRecord): Boolean {
    return record.data != null
  }
}
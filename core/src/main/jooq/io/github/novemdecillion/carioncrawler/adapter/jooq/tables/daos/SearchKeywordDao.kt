/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.carioncrawler.adapter.jooq.tables.daos


import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.SearchKeywordTable
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos.SearchKeywordEntity
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchKeywordRecord

import java.time.LocalDate

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class SearchKeywordDao(configuration: Configuration?) : DAOImpl<SearchKeywordRecord, SearchKeywordEntity, String>(SearchKeywordTable.SEARCH_KEYWORD, SearchKeywordEntity::class.java, configuration) {

    /**
     * Create a new SearchKeywordDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: SearchKeywordEntity): String? = o.keyword

    /**
     * Fetch records that have <code>keyword BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfKeywordTable(lowerInclusive: String?, upperInclusive: String?): List<SearchKeywordEntity> = fetchRange(SearchKeywordTable.SEARCH_KEYWORD.KEYWORD, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>keyword IN (values)</code>
     */
    fun fetchByKeywordTable(vararg values: String): List<SearchKeywordEntity> = fetch(SearchKeywordTable.SEARCH_KEYWORD.KEYWORD, *values)

    /**
     * Fetch a unique record that has <code>keyword = value</code>
     */
    fun fetchOneByKeywordTable(value: String): SearchKeywordEntity? = fetchOne(SearchKeywordTable.SEARCH_KEYWORD.KEYWORD, value)

    /**
     * Fetch records that have <code>searched_at BETWEEN lowerInclusive AND upperInclusive</code>
     */
    fun fetchRangeOfSearchedAtTable(lowerInclusive: LocalDate?, upperInclusive: LocalDate?): List<SearchKeywordEntity> = fetchRange(SearchKeywordTable.SEARCH_KEYWORD.SEARCHED_AT, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>searched_at IN (values)</code>
     */
    fun fetchBySearchedAtTable(vararg values: LocalDate): List<SearchKeywordEntity> = fetch(SearchKeywordTable.SEARCH_KEYWORD.SEARCHED_AT, *values)
}
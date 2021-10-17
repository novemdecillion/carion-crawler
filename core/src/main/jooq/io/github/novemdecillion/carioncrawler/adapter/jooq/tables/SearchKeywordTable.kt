/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.carioncrawler.adapter.jooq.tables


import io.github.novemdecillion.carioncrawler.adapter.jooq.DefaultSchema
import io.github.novemdecillion.carioncrawler.adapter.jooq.keys.SEARCH_KEYWORD_PKEY
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.SearchKeywordRecord

import java.time.LocalDate

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row2
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class SearchKeywordTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, SearchKeywordRecord>?,
    aliased: Table<SearchKeywordRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<SearchKeywordRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>search_keyword</code>
         */
        val SEARCH_KEYWORD = SearchKeywordTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<SearchKeywordRecord> = SearchKeywordRecord::class.java

    /**
     * The column <code>search_keyword.keyword</code>.
     */
    val KEYWORD: TableField<SearchKeywordRecord, String?> = createField(DSL.name("keyword"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>search_keyword.searched_at</code>.
     */
    val SEARCHED_AT: TableField<SearchKeywordRecord, LocalDate?> = createField(DSL.name("searched_at"), SQLDataType.LOCALDATE, this, "")

    private constructor(alias: Name, aliased: Table<SearchKeywordRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<SearchKeywordRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>search_keyword</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>search_keyword</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>search_keyword</code> table reference
     */
    constructor(): this(DSL.name("search_keyword"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, SearchKeywordRecord>): this(Internal.createPathAlias(child, key), child, key, SEARCH_KEYWORD, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<SearchKeywordRecord> = SEARCH_KEYWORD_PKEY
    override fun getKeys(): List<UniqueKey<SearchKeywordRecord>> = listOf(SEARCH_KEYWORD_PKEY)
    override fun `as`(alias: String): SearchKeywordTable = SearchKeywordTable(DSL.name(alias), this)
    override fun `as`(alias: Name): SearchKeywordTable = SearchKeywordTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): SearchKeywordTable = SearchKeywordTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): SearchKeywordTable = SearchKeywordTable(name, null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<String?, LocalDate?> = super.fieldsRow() as Row2<String?, LocalDate?>
}

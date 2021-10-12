/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.carioncrawler.adapter.jooq.tables


import io.github.novemdecillion.carioncrawler.adapter.jooq.DefaultSchema
import io.github.novemdecillion.carioncrawler.adapter.jooq.keys.CRAWLED_PAGE_PKEY
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.CrawledPageRecord

import java.time.OffsetDateTime

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row4
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
open class CrawledPageTable(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, CrawledPageRecord>?,
    aliased: Table<CrawledPageRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<CrawledPageRecord>(
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
         * The reference instance of <code>crawled_page</code>
         */
        val CRAWLED_PAGE = CrawledPageTable()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<CrawledPageRecord> = CrawledPageRecord::class.java

    /**
     * The column <code>crawled_page.url</code>.
     */
    val URL: TableField<CrawledPageRecord, String?> = createField(DSL.name("url"), SQLDataType.VARCHAR(2047).nullable(false), this, "")

    /**
     * The column <code>crawled_page.html</code>.
     */
    val HTML: TableField<CrawledPageRecord, String?> = createField(DSL.name("html"), SQLDataType.CLOB, this, "")

    /**
     * The column <code>crawled_page.text</code>.
     */
    val TEXT: TableField<CrawledPageRecord, String?> = createField(DSL.name("text"), SQLDataType.CLOB, this, "")

    /**
     * The column <code>crawled_page.seen</code>.
     */
    val SEEN: TableField<CrawledPageRecord, OffsetDateTime?> = createField(DSL.name("seen"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<CrawledPageRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<CrawledPageRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>crawled_page</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>crawled_page</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>crawled_page</code> table reference
     */
    constructor(): this(DSL.name("crawled_page"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, CrawledPageRecord>): this(Internal.createPathAlias(child, key), child, key, CRAWLED_PAGE, null)
    override fun getSchema(): Schema = DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<CrawledPageRecord> = CRAWLED_PAGE_PKEY
    override fun getKeys(): List<UniqueKey<CrawledPageRecord>> = listOf(CRAWLED_PAGE_PKEY)
    override fun `as`(alias: String): CrawledPageTable = CrawledPageTable(DSL.name(alias), this)
    override fun `as`(alias: Name): CrawledPageTable = CrawledPageTable(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): CrawledPageTable = CrawledPageTable(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): CrawledPageTable = CrawledPageTable(name, null)

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row4<String?, String?, String?, OffsetDateTime?> = super.fieldsRow() as Row4<String?, String?, String?, OffsetDateTime?>
}

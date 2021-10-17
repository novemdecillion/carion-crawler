/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos


import io.github.novemdecillion.carioncrawler.adapter.db.CrawledStatus
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.interfaces.ICrawledPage

import java.time.OffsetDateTime


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class CrawledPageEntity(
    override var url: String? = null, 
    override var status: CrawledStatus? = null, 
    override var note: String? = null, 
    override var exclude: Boolean? = null, 
    override var searchedAt: OffsetDateTime? = null, 
    override var crawledAt: OffsetDateTime? = null
): ICrawledPage {


    override fun toString(): String {
        val sb = StringBuilder("CrawledPageEntity (")

        sb.append(url)
        sb.append(", ").append(status)
        sb.append(", ").append(note)
        sb.append(", ").append(exclude)
        sb.append(", ").append(searchedAt)
        sb.append(", ").append(crawledAt)

        sb.append(")")
        return sb.toString()
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: ICrawledPage) {
        url = from.url
        status = from.status
        note = from.note
        exclude = from.exclude
        searchedAt = from.searchedAt
        crawledAt = from.crawledAt
    }

    override fun <E : ICrawledPage> into(into: E): E {
        into.from(this)
        return into
    }
}

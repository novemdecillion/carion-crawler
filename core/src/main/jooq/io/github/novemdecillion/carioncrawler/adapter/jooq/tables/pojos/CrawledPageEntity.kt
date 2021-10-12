/*
 * This file is generated by jOOQ.
 */
package io.github.novemdecillion.carioncrawler.adapter.jooq.tables.pojos


import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.interfaces.ICrawledPage

import java.time.OffsetDateTime


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class CrawledPageEntity(
    override var url: String? = null, 
    override var html: String? = null, 
    override var text: String? = null, 
    override var seen: OffsetDateTime? = null
): ICrawledPage {


    override fun toString(): String {
        val sb = StringBuilder("CrawledPageEntity (")

        sb.append(url)
        sb.append(", ").append(html)
        sb.append(", ").append(text)
        sb.append(", ").append(seen)

        sb.append(")")
        return sb.toString()
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    override fun from(from: ICrawledPage) {
        url = from.url
        html = from.html
        text = from.text
        seen = from.seen
    }

    override fun <E : ICrawledPage> into(into: E): E {
        into.from(this)
        return into
    }
}

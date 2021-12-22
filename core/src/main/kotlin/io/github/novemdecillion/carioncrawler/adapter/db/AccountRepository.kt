package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.records.AccountRecord
import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.ACCOUNT
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(val dsl: DSLContext) {
  fun selectByUsername(username: String): AccountRecord? {
    return dsl.fetchOne(ACCOUNT, ACCOUNT.USERNAME.equal(username))
  }
}
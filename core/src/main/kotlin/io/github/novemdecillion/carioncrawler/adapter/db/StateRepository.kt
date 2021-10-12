package io.github.novemdecillion.carioncrawler.adapter.db

import io.github.novemdecillion.carioncrawler.adapter.jooq.tables.references.STATE
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Repository
class StateRepository(val dsl: DSLContext) {
  companion object {
    const val SEARCHED_DATE = "searchedDate"
  }

  fun setSearchedDate(date: LocalDate) {
    val value = date.format(DateTimeFormatter.ISO_DATE)
    dsl.insertInto(STATE)
      .set(STATE.KEY, SEARCHED_DATE)
      .set(STATE.VALUE, value)
      .onConflict(STATE.KEY).doUpdate().set(STATE.VALUE, value)
      .execute()
  }

  fun getSearchedDate(dateIfNull: LocalDate): LocalDate {
    return dsl.selectFrom(STATE)
      .where(STATE.KEY.equal(SEARCHED_DATE))
      .fetchOne()
      ?.get(STATE.VALUE)
      ?.let {
        LocalDate.parse(it, DateTimeFormatter.ISO_DATE)
      }
      ?: dateIfNull
  }

}
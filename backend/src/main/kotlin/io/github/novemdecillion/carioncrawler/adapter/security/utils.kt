package io.github.novemdecillion.carioncrawler.adapter.security

import io.github.novemdecillion.carioncrawler.domain.Account
import org.springframework.security.core.context.SecurityContextHolder

fun currentUser(): Account? {
  return (SecurityContextHolder.getContext().authentication.principal as? Account)
}

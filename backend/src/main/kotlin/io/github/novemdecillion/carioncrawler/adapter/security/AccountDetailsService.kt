package io.github.novemdecillion.carioncrawler.adapter.security

import io.github.novemdecillion.carioncrawler.adapter.db.AccountRepository
import io.github.novemdecillion.carioncrawler.domain.Account
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest

@Service
class AccountDetailsService(private val accountRepository: AccountRepository) : UserDetailsService {
  @Transactional(readOnly = true)
  override fun loadUserByUsername(username: String): UserDetails {
    return accountRepository.selectByUsername(username)
      ?.into(Account::class.java)
      ?: throw UsernameNotFoundException("アカウント: $username は見つかりませんでした。")
  }
}

fun doAuthentication(username: String, request: HttpServletRequest, userDetailsService: AccountDetailsService) {
  val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
  val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
  authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
  SecurityContextHolder.getContext().authentication = authentication
}

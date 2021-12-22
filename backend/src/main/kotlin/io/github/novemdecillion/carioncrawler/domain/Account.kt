package io.github.novemdecillion.carioncrawler.domain

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

enum class Role {
  ADMIN
}

class Account(
  username: String,
  password: String?,
  val role: Role
) : User(username, password, listOf(SimpleGrantedAuthority(role.name)))
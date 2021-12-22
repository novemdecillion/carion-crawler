package io.github.novemdecillion.carioncrawler.adapter.security

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

@Component
class CookieProcessor(private val jwtProperties: AppJwtProperties) {
  companion object {
    val log = LoggerFactory.getLogger(CookieProcessor::class.java)
    const val COOKIE_NAME = "COGITOERGOSUM"
    const val SAME_SITE_STRICT = "Strict"
  }

  fun requestTokenCookie(request: HttpServletRequest): Cookie? {
    return request.cookies?.firstOrNull { it.name ==  COOKIE_NAME }
  }

  fun generateCookie(serverName: String, token: String): ResponseCookie {
    return ResponseCookie.from(COOKIE_NAME, token).domain(serverName)
      // 1年間
      .maxAge(60 * 60 * 24 * 365)
      .path("/")
      .secure(true).httpOnly(true).sameSite(SAME_SITE_STRICT)
      .build()
  }

  fun temporarySetTokenCookie(request: HttpServletRequest, token: String) {
    val cookie = generateCookie(request.serverName, token)
    request.setAttribute(COOKIE_NAME, cookie)
  }

  fun temporaryClearTokenCookie(request: HttpServletRequest) {
    val cookie = ResponseCookie.from(COOKIE_NAME, StringUtils.EMPTY)
      .domain(request.serverName)
      .path("/")
      .maxAge(0)
      .secure(true).httpOnly(true).sameSite(SAME_SITE_STRICT)
      .build()
    request.setAttribute(COOKIE_NAME, cookie)
  }
}
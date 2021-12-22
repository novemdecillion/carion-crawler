package io.github.novemdecillion.carioncrawler.adapter.security

import com.nimbusds.jwt.JWT
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthTokenFilter(
  private val jwtProperties: AppJwtProperties, private val tokenProvider: JwtProvider,
  private val cookieProcessor: CookieProcessor,
  private val userDetailsService: AccountDetailsService
) : OncePerRequestFilter() {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try {
      cookieProcessor.requestTokenCookie(request)
        ?.value
        ?.let {
          tokenProvider.parseAndValidateJwtToken(it)
        }
        ?.also { jwt ->
          request.setAttribute(JwtAuthTokenFilter::class.java.name, jwt)
          doAuthentication(jwt.username(), request, userDetailsService)
        }
    } catch (ex: Exception) {
      logger.error("認証の検証または認証情報の設定エラー。", ex)
    } finally {
      filterChain.doFilter(request, response)

      if (!request.isAsyncStarted) {
        flushCookie(request, response, jwtProperties, tokenProvider, cookieProcessor)
      }
    }
  }
}

fun flushCookie(
  request: HttpServletRequest,
  response: HttpServletResponse,
  jwtProperties: AppJwtProperties,
  tokenProvider: JwtProvider,
  cookieProcessor: CookieProcessor
) {

  (request.getAttribute(CookieProcessor.COOKIE_NAME) as? ResponseCookie)
    ?.also {
      response.setHeader(HttpHeaders.SET_COOKIE, it.toString())
    }
    ?: run {
      (request.getAttribute(JwtAuthTokenFilter::class.java.name) as? JWT)
        ?.also { jwt ->

          // Cookieが作成されてからの経過時間
          val elapsedTime = Date().time - (jwt.jwtClaimsSet.expirationTime.time - jwtProperties.expiration.toMillis())
          if (jwtProperties.refresh.toMillis() < elapsedTime) {
            val renewalJwt = tokenProvider.generateJwtToken(jwt.username())
            val cookie = cookieProcessor.generateCookie(request.serverName, renewalJwt.serialize())
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())
          }
        }
    }
}
package io.github.novemdecillion.carioncrawler.adapter.api

import com.nimbusds.jwt.JWT
import graphql.kickstart.servlet.context.GraphQLServletContext
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.carioncrawler.adapter.security.*
import io.github.novemdecillion.carioncrawler.domain.Account
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AccountApi(private val authenticationManager: AuthenticationManager,
                 private val jwtProvider: JwtProvider,
                 private val cookieProcessor: CookieProcessor) : GraphQLQueryResolver, GraphQLMutationResolver {

  @PreAuthorize(AUTHENTICATED)
  fun ping(environment: DataFetchingEnvironment): Boolean {
    // JWTを使いまわして、期限を更新させない
    val context = environment.getContext<GraphQLServletContext>()
    (context.httpServletRequest.getAttribute(JwtAuthTokenFilter::class.java.name) as? JWT)
      ?.also { jwt ->
        cookieProcessor.temporarySetTokenCookie(context.httpServletRequest, jwt.serialize())
      }
    return true
  }

  fun myAccount(): Account? {
    return currentUser()
  }

  fun login(username: String, password: String, environment: DataFetchingEnvironment): Account? {
    val authentication: Authentication = authenticationManager
      .authenticate(UsernamePasswordAuthenticationToken(username, password))

    SecurityContextHolder.getContext().authentication = authentication
    val jwt = jwtProvider.generateJwtToken(username)

    val context = environment.getContext<GraphQLServletContext>()
    cookieProcessor.temporarySetTokenCookie(context.httpServletRequest, jwt.serialize())

    val user = (authentication.principal as Account)
    user.eraseCredentials()

    return user
  }

  fun logout(environment: DataFetchingEnvironment): Boolean {
    val context = environment.getContext<GraphQLServletContext>()
    cookieProcessor.temporaryClearTokenCookie(context.httpServletRequest)
    return true
  }

}
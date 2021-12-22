package io.github.novemdecillion.carioncrawler.adapter.security

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(val accountDetailsService: AccountDetailsService,
                            val jwtProperties: AppJwtProperties,
                            val jwtAuthTokenFilter: JwtAuthTokenFilter) : WebSecurityConfigurerAdapter() {
  companion object {
    val log = LoggerFactory.getLogger(SecurityConfiguration::class.java)
    const val PATH_API = "/api/**"
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  override fun configure(http: HttpSecurity) {
    // @formatter:off
    http
      .authorizeRequests()
      .antMatchers(PATH_API).permitAll()
      .anyRequest().authenticated()

    http.csrf().disable()

    http
      .exceptionHandling()
      .authenticationEntryPoint { _, response, authException ->
        log.debug("認証エラー", authException)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//        if (authException is AuthenticationCredentialsNotFoundException) {
//          response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//        } else {
//          response.sendError(HttpServletResponse.SC_FORBIDDEN)
//        }
      }

//    http.anonymous().disable()

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

    // @formatter:on
  }

  override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
    authenticationManagerBuilder
      .userDetailsService(accountDetailsService)
      .passwordEncoder(passwordEncoder())
  }

  @Bean
  override fun authenticationManagerBean(): AuthenticationManager {
    return super.authenticationManagerBean()
  }
}
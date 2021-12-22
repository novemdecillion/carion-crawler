package io.github.novemdecillion.carioncrawler.adapter.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier
import com.nimbusds.jwt.util.DateUtils
import io.github.novemdecillion.carioncrawler.adapter.security.AppJwtProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider(val jwtProperties: AppJwtProperties) {
  val verifier = MACVerifier(jwtProperties.secret)
  val signer = MACSigner(jwtProperties.secret)

  fun generateJwtToken(username: String): SignedJWT {
    val climeSet = JWTClaimsSet.Builder()
      .subject(username)
      .issueTime(Date())
      .expirationTime(Date(System.currentTimeMillis() + jwtProperties.expiration.toMillis()))
      .build()
    val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), climeSet)
    signedJWT.sign(signer)
    return signedJWT
  }

  fun parseAndValidateJwtToken(token: String): SignedJWT? {
    return try {
      val signedJWT = SignedJWT.parse(token)
      signedJWT.verify(verifier)

      signedJWT.jwtClaimsSet.expirationTime
        ?.also {
          if (!DateUtils.isAfter(it, Date(), DefaultJWTClaimsVerifier.DEFAULT_MAX_CLOCK_SKEW_SECONDS.toLong())) {
            return null
          }
        }

      return signedJWT
    } catch (ex: Exception) {
      null
    }
  }
}

fun JWT.username(): String = this.jwtClaimsSet.subject

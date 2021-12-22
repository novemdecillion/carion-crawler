package io.github.novemdecillion.carioncrawler.adapter.web

import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.buf.EncodedSolidusHandling
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.StrictHttpFirewall


@Configuration
class TomcatConfig {
//  @Bean
//  fun tomcatCustomizer(): WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
//    return WebServerFactoryCustomizer { factory: TomcatServletWebServerFactory ->
//      factory.addConnectorCustomizers(
//        TomcatConnectorCustomizer { connector: Connector ->
//          connector.encodedSolidusHandling = EncodedSolidusHandling.DECODE.value
//        })
//    }
//  }
//
//  @Bean
//  fun httpFirewall(): HttpFirewall {
//    val firewall = StrictHttpFirewall()
//    firewall.setUnsafeAllowAnyHttpMethod(true)
//    return firewall
//  }
}
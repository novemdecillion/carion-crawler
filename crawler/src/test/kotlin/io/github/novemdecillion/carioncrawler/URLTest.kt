package io.github.novemdecillion.carioncrawler

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.URL

class URLTest {
  @Test
  fun test() {
//    Assertions.assertThat(URL("facetime:user@example.com").protocol).isEqualTo("facetime")
//    Assertions.assertThat(URL("path/to").protocol).isEqualTo("")
//    Assertions.assertThat(URL("/path/to").protocol).isEqualTo("")
//    Assertions.assertThat(URL("http://host/path/to").protocol).isEqualTo("http")


    Assertions.assertThat(URI("facetime:user@example.com").scheme).isEqualTo("facetime")
    Assertions.assertThat(URI("path/to").scheme).isNull()
    Assertions.assertThat(URI("/path/to").scheme).isNull()
    Assertions.assertThat(URI("http://host/path/to").scheme).isEqualTo("http")

  }
}
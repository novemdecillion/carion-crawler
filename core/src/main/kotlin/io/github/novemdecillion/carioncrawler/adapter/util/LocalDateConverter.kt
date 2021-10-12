package io.github.novemdecillion.carioncrawler.adapter.util

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
@ConfigurationPropertiesBinding
class LocalDateConverter : Converter<String, LocalDate> {
  override fun convert(source: String): LocalDate? {
    return LocalDate.parse(source, DateTimeFormatter.ISO_DATE)
  }
}

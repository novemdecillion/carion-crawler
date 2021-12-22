package io.github.novemdecillion.carioncrawler.adapter.graphql

import graphql.schema.DataFetchingEnvironment
import java.time.OffsetDateTime

fun DataFetchingEnvironment.now(): OffsetDateTime {
  return graphQlContext[NowInstrumentation.NOW]
}

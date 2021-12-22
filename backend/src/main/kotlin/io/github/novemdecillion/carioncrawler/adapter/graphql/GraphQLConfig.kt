package io.github.novemdecillion.carioncrawler.adapter.graphql

import graphql.kickstart.execution.config.DefaultExecutionStrategyProvider
import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.kickstart.tools.SchemaParserDictionary
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class GraphQLConfig {
  @Bean
  fun dateTimeScaler(): GraphQLScalarType = ExtendedScalars.DateTime
  @Bean
  fun dateScaler(): GraphQLScalarType = ExtendedScalars.Date
  //  @Bean
//  fun timeScaler(): GraphQLScalarType = ExtendedScalars.Time
//  @Bean
//  fun longScaler(): GraphQLScalarType = ExtendedScalars.GraphQLLong
//  @Bean
//  fun bigDecimalScaler(): GraphQLScalarType = ExtendedScalars.GraphQLBigDecimal
//  @Bean
//  fun uploadScaler(): GraphQLScalarType = ApolloScalars.Upload

//  @Bean
//  fun schemaParserDictionary(): SchemaParserDictionary? {
//    return SchemaParserDictionary()
//      .add(Account::class.java)
//  }

//  @Bean
//  fun executionStrategyProvider(transactionManager: PlatformTransactionManager): DefaultExecutionStrategyProvider {
//    return DefaultExecutionStrategyProvider(
//      TransactionalExecutionStrategy(transactionManager),
//      TransactionalSerialExecutionStrategy(transactionManager), null)
//  }

}
package io.github.novemdecillion.carioncrawler.adapter.graphql

import graphql.kickstart.execution.context.GraphQLContext
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.servlet.context.DefaultGraphQLServletContextBuilder
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.carioncrawler.domain.Account
import org.dataloader.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass

@Component
class GraphQLServletContextBuilder(
  @Autowired(required = false) private val mappedBatchLoaders: Collection<MappedBatchLoader<*, *>>?,
  @Autowired(required = false) private val mappedBatchLoaderWithContexts: Collection<MappedBatchLoaderWithContext<*, *>>?,
) : DefaultGraphQLServletContextBuilder() {
  val dataLoaderRegistry: DataLoaderRegistry = DataLoaderRegistry()
    .also { registry ->
      val options = DataLoaderOptions().setCachingEnabled(false).setBatchingEnabled(false)
      mappedBatchLoaders?.forEach {
        val dataLoader = DataLoaderFactory.newMappedDataLoader(it, options)
        registry.register(it::class.java.simpleName, dataLoader)
      }
      mappedBatchLoaderWithContexts?.forEach {
        val dataLoader = DataLoaderFactory.newMappedDataLoader(it, options)
        registry.register(it::class.java.simpleName, dataLoader)
      }
    }

  override fun build(request: HttpServletRequest, response: HttpServletResponse): GraphQLContext {
    return DefaultGraphQLServletContext.createServletContext()
      .with(dataLoaderRegistry)
      .with(request).with(response).build()
  }
}

fun <K, V, T : MappedBatchLoader<K, V>> DataFetchingEnvironment.mappedBatchLoader(key: KClass<T>): DataLoader<K, V> {
  return dataLoaderRegistry.getDataLoader(key.java.simpleName)
}

fun <K, V, T : MappedBatchLoaderWithContext<K, V>> DataFetchingEnvironment.mappedBatchLoaderWithContext(key: KClass<T>): DataLoader<K, V> {
  return dataLoaderRegistry.getDataLoader(key.java.simpleName)
}

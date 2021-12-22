package io.github.novemdecillion.carioncrawler.adapter.graphql

import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters
import graphql.schema.DataFetchingEnvironment
import io.github.novemdecillion.carioncrawler.adapter.graphql.NowInstrumentation.Companion.NOW
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class NowInstrumentation() : SimpleInstrumentation() {
  companion object {
    const val NOW = "NOW"
  }

  override fun beginExecuteOperation(parameters: InstrumentationExecuteOperationParameters): InstrumentationContext<ExecutionResult> {
    if (!parameters.executionContext.graphQLContext.hasKey(NOW)) {
      parameters.executionContext.graphQLContext.put(NOW, OffsetDateTime.now())
    }
    return SimpleInstrumentationContext.noOp()
  }
}

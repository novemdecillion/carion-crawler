package io.github.novemdecillion.carioncrawler.adapter.security

import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters
import org.springframework.stereotype.Component
import graphql.kickstart.servlet.context.GraphQLServletContext

@Component
class CookieInstrumentation(private val jwtProperties: AppJwtProperties, private val tokenProvider: JwtProvider,
                            private val cookieProcessor: CookieProcessor
) : SimpleInstrumentation() {

  override fun beginExecuteOperation(parameters: InstrumentationExecuteOperationParameters): InstrumentationContext<ExecutionResult> {
    return SimpleInstrumentationContext.whenCompleted { _, _ ->
      val context = parameters.executionContext.getContext<GraphQLServletContext>()
      val request = context.httpServletRequest
      val response = context.httpServletResponse

      flushCookie(request, response, jwtProperties, tokenProvider, cookieProcessor)
    }
  }
}
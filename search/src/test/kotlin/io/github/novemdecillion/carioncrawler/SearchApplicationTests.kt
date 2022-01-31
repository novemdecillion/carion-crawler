package io.github.novemdecillion.carioncrawler

import io.github.novemdecillion.carioncrawler.adapter.db.SearchKeywordRepository
import io.github.novemdecillion.carioncrawler.adapter.search.GoogleSearchService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SearchApplicationTests(val searchService: GoogleSearchService, val keywordRepository: SearchKeywordRepository) {

	@Test
	fun contextLoads() {
      val date = LocalDate.now().minusDays(3)

      keywordRepository.selectAll()
        .forEach { searchKeywordEntity ->
          keywordRepository.updateSearchAt(searchKeywordEntity.keyword!!, date)
        }

      searchService.search()
	}

}

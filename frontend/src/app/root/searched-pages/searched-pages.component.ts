import { Component } from '@angular/core';
import { format, parseISO } from 'date-fns';
import { map, Observable } from 'rxjs';
import { SearchedPageFragment, SearchedPagesGQL } from 'src/graphql/graphql';
import { formatGraphQLDateTime } from '../miscellaneous';

@Component({
  selector: 'app-searched-pages',
  templateUrl: './searched-pages.component.html',
  styles: [
  ]
})
export class SearchedPagesComponent {
  pageSize = 100;
  page = 1;
  total = 0;
  dataSource$!: Observable<SearchedPageFragment[]>

  constructor(private searchedPagesGQL: SearchedPagesGQL) {
    this.onLoad();
  }

  onPageChange() {
    this.onLoad();
  }

  onLoad(): void {
    this.dataSource$ = this.searchedPagesGQL
      .fetch({ skipCount: false, limit: this.pageSize, offset: (this.page - 1) * this.pageSize })
      .pipe(
        map(res => {
          this.total = res.data.searchedPageCount ?? 0;
          return res.data.searchedPages?.map(page => {
            page.createAt = formatGraphQLDateTime(page.createAt);
            return page;
          }) ?? []
        })
      );
  }

}

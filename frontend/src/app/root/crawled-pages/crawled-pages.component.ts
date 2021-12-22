import { Component, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { map, Observable } from 'rxjs';
import { CrawledPageFragment, CrawledPagesGQL, GetCrawledPageUrlFiltersGQL, SetCrawledPageUrlFiltersGQL } from 'src/graphql/graphql';
import { formatGraphQLDateTime } from '../miscellaneous';

type CrawledPageForDisplay = CrawledPageFragment & { encodedUrl: string };

@Component({
  selector: 'app-crawled-pages',
  templateUrl: './crawled-pages.component.html',
  styles: [
  ]
})
export class CrawledPagesComponent {

  pageSize = 100;
  page = 1;
  total = 0;
  dataSource$!: Observable<CrawledPageForDisplay[]>
  urlFiltersFormGroup: FormGroup;

  constructor(formBuilder: FormBuilder,
      private modal: NgbModal,
      private crawledPagesGQL: CrawledPagesGQL,
      private getUrlFiltersGQL: GetCrawledPageUrlFiltersGQL,
      private setUrlFiltersGQL: SetCrawledPageUrlFiltersGQL) {
    this.urlFiltersFormGroup = formBuilder.group({
      urlFilters: [undefined]
    });
    this.onLoad();
  }

  onPageChange() {
    this.onLoad();
  }

  onLoad(): void {
    this.dataSource$ = this.crawledPagesGQL
      .fetch({ skipCount: false, enableUrlFilter: true ,limit: this.pageSize, offset: (this.page - 1) * this.pageSize })
      .pipe(
        map(res => {
          this.total = res.data.crawledPageCount ?? 0;
          return res.data.crawledPages?.map(page => {
            page.searchedAt = formatGraphQLDateTime(page.searchedAt);
            page.crawledAt = formatGraphQLDateTime(page.crawledAt);
            return Object.assign({ encodedUrl: encodeURIComponent(page.url) }, page);
          }) ?? []
        })
      );
  }

  onUrlFilter(template: TemplateRef<any>): void {

    this.getUrlFiltersGQL.fetch().subscribe(res => {
      this.urlFiltersFormGroup.patchValue({
        urlFilters: res.data.getCrawledPageUrlFilters?.join('\n')
      });

      this.modal.open(template)
        .closed.subscribe(_ => {

          this.setUrlFiltersGQL.mutate({ urlFilters: (this.urlFiltersFormGroup.value.urlFilters as string).split('\n') })
            .subscribe(_ => this.onLoad());
        });
    })

  }


}

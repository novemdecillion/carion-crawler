import { Component, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { map, Observable } from 'rxjs';
import { AddSearchKeywordGQL, DeleteSearchKeywordGQL, SearchKeywordFragment, SearchKeywordsGQL } from 'src/graphql/graphql';

@Component({
  selector: 'app-search-keywords',
  templateUrl: './search-keywords.component.html',
  styles: [
  ]
})
export class SearchKeywordsComponent {
  dataSource$!: Observable<SearchKeywordFragment[]>
  keywordFormGroup: FormGroup;

  constructor(
      formBuilder: FormBuilder,
      private modal: NgbModal,
      private searchKeywordsGQL: SearchKeywordsGQL, private addSearchKeywordGQL: AddSearchKeywordGQL, private deleteSearchKeywordGQL: DeleteSearchKeywordGQL) {

    this.keywordFormGroup = formBuilder.group({ keyword: [undefined, Validators.required]});
    this.onLoad();
  }

  onLoad(): void {
    this.dataSource$ = this.searchKeywordsGQL.fetch()
      .pipe(map(res => (res.data.searchKeywords ?? [])));
  }

  onAdd(template: TemplateRef<any>): void {
    this.keywordFormGroup.patchValue({
      keyword: undefined
    });

    this.modal.open(template)
      .closed.subscribe(_ => {
        this.addSearchKeywordGQL.mutate({ keyword: this.keywordFormGroup.value.keyword })
          .subscribe(res => this.onLoad());
      });
  }

  onDelete(data: SearchKeywordFragment): void {
    this.deleteSearchKeywordGQL.mutate({ keyword: data.keyword })
      .subscribe(res => this.onLoad())
  }
}

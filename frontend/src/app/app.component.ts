import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { select, Store } from '@ngrx/store';
import { combineLatest, interval } from 'rxjs';
import { MyAccountGQL, PingGQL } from 'src/graphql/graphql';
import { AppState, DEFAULT_URL, getAccount, LOGIN_URL, RootActions } from './root/state';
import { BACKGROUND_HEADER } from './xhr.interceptor';

@UntilDestroy()
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styles: []
})
export class AppComponent implements OnInit {
  isMenuCollapsed = false;
  account$ = this.store.pipe(select(getAccount));

  constructor(private router: Router, private store: Store<AppState>,
    private pingGQL: PingGQL,
    private myAccountGQL: MyAccountGQL) {}

  ngOnInit(): void {
    // リロード時、AuthGuardによりlogin画面に遷移させられるので、ここで戻す。
    let storePath = window.location.pathname;
    if (storePath == LOGIN_URL) {
      storePath = DEFAULT_URL;
    }

    this.myAccountGQL.fetch()
      .subscribe(res => {
        if (res.data.myAccount) {
          this.store.dispatch(RootActions.loginByServer({ account: res.data.myAccount }));
          if (window.location.pathname != storePath) {
            this.router.navigateByUrl(storePath);
          }
        }
      });

    // ログイン中確認
    combineLatest([this.account$, interval(5 * 60 * 1000)])
      .pipe(untilDestroyed(this))
      .subscribe(res => {
        if (res[0]) {
          this.pingGQL
            .fetch(undefined, {
              context: {
                headers: new HttpHeaders().set(BACKGROUND_HEADER, 'true')
              }
            })
            .subscribe();
        }
      });

  }

  onLogout() {

  }
}

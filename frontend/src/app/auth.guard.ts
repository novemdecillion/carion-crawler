import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { getAccount, LOGIN_URL, AppState } from './root/state';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private store: Store<AppState>) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.store
      .pipe(
        select(getAccount),
        map(account => {
          if (account === undefined) {
            console.log(`[AuthGuard]${state.url} -> ${LOGIN_URL}へ遷移。`)
            this.router.navigateByUrl(LOGIN_URL);
            return false;
          }
          console.log(`[AuthGuard]${state.url} -> 認証済み。`)
          return true;
        })
      )
  }

}

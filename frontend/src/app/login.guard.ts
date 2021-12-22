import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppState, getAccount } from './root/state';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {
  constructor(
    private router: Router,
    private store: Store<AppState>
  ) {
  }

  canActivate(next: ActivatedRouteSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.store
      .pipe(
        select(getAccount),
        map(account => {
          if (account === undefined) {
            return true;
          }
          this.router.navigateByUrl('/');
          return false;
        })
      )
  }
}

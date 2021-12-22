import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { catchError, finalize } from "rxjs/operators";
import { RootActions, RootState } from "./root/state";

export const BACKGROUND_HEADER = 'X-Background';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  constructor(private store: Store<RootState>) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let isBackground = req.headers.get(BACKGROUND_HEADER) != null;
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest').delete(BACKGROUND_HEADER)
    });
    if (!isBackground) {
      this.store.dispatch(RootActions.loadingStart());
    }

    return next.handle(xhr)
      .pipe(
        catchError(error => {
          if (error instanceof HttpErrorResponse) {
            if(error.status == 401) {
              this.store.dispatch(RootActions.logoutByServer());
            }
          }
          throw error;
        }),
        finalize(() => {
          if (!isBackground) {
            this.store.dispatch(RootActions.loadingEnd());
          }
        })
      );
  }
}

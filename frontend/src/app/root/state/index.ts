import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { createAction, createReducer, props, on, createSelector, MetaReducer, Action } from "@ngrx/store";
import { finalize, map, mergeMap } from "rxjs";
import { Account, LoginGQL, LogoutGQL } from "src/graphql/graphql";
import { Router } from "@angular/router";

export const RootActions = {
  login: createAction('[Root] Login', props<{ username: string, password: string }>()),
  loginByServer: createAction('[Root] Login by server', props<{ account: Account }>()),
  logined: createAction('[Root] Logined', props<{ account: Account }>()),

  error: createAction('[Root] error', props<{ error?: any }>()),

  logout: createAction('[Root] Logout request'),
  logoutByServer: createAction('[Root] Logout by server'),
  logouted: createAction('[Root] Logouted'),

  loadingStart: createAction('[Root] Loading start'),
  loadingEnd: createAction('[Root] Loading end'),
}

export const DEFAULT_URL = '/'
export const LOGIN_URL = '/login'

@Injectable()
export class RootEffects {

  constructor(private actions$: Actions, private readonly router: Router,
    private loginGQL: LoginGQL,
    private logoutGQL: LogoutGQL) {
  }

  readonly login$ = createEffect(
    () => this.actions$.pipe(
      ofType(RootActions.login),
      mergeMap((action) => {
        return this.loginGQL.mutate({ username: action.username, password: action.password })
          .pipe(
            map(res => {
              if (res.data?.login == null) {
                return RootActions.error({ error: 'ログインに失敗しました。' });
              }
              return RootActions.logined({ account: res.data?.login });
            }),
            finalize(() => this.router.navigateByUrl(DEFAULT_URL))
          );
      }))
  );

  readonly loginByServer$ = createEffect(
    () => this.actions$.pipe(
      ofType(RootActions.loginByServer),
      map((action) => {
        this.router.navigateByUrl(DEFAULT_URL);
        return RootActions.logined({ account: action.account });
      }))
  );

  readonly logout$ = createEffect(
    () => this.actions$.pipe(
      ofType(RootActions.logout),
      mergeMap(() => {
        return this.logoutGQL.mutate().pipe(
          map(_ => RootActions.logouted()),
          finalize(() => this.router.navigateByUrl(DEFAULT_URL)));
      })
    )
  );

  readonly logoutByServer$ = createEffect(
    () => this.actions$.pipe(
      ofType(RootActions.logoutByServer),
      map(() => {
        this.router.navigateByUrl(DEFAULT_URL);
        return RootActions.logouted();
      })
    )
  );
}


export interface RootState {
  account?: Account,
  loading: number,
  error?: string
}

export const initialRootState: RootState = {
  loading: 0,
};

export const rootReducer = createReducer(
  initialRootState,
  on(RootActions.logined, (state, { account }) => {
    return { ...state, account, error: undefined }
  }),
  on(RootActions.logouted, (state) => {
    return { ...state, account: undefined, error: undefined }
  }),

  on(RootActions.loadingStart, (state) => {
    return { ...state, error: undefined, loading: state.loading + 1 }
  }),
  on(RootActions.loadingEnd, (state) => {
    return { ...state, loading: state.loading - 1 }
  }),

  on(RootActions.error, (state, { error }) => {
    return { ...state, error }
  }),
);

export interface AppState {
  root: RootState;
}

// export const reducers: ActionReducerMap<State> = {
//   root: rootReducer
// };

// export function reducers(state: RootState | undefined, action: Action) {
//   return rootReducer(state, action);
// }

// export const metaReducers: MetaReducer<RootState>[] = [];

export const getRootState = (state: AppState) => state.root;
export const getAccount = createSelector(getRootState, state => state.account);
export const getLoading = createSelector(getRootState, state => state.loading);
export const getError = createSelector(getRootState, state => state.error);

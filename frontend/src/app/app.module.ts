import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NgbCollapseModule, NgbDropdownModule, NgbModalModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { GraphQLModule } from './graphql.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from '../environments/environment';
import { EffectsModule } from '@ngrx/effects';
import { LoginComponent } from './root/login/login.component';
import * as rootState from './root/state'
import { RouterModule, Routes } from '@angular/router';
// import { DashbordComponent } from './root/dashbord/dashbord.component';
import { AuthGuard } from './auth.guard';
import { LoginGuard } from './login.guard';
import { ReactiveFormsModule } from '@angular/forms';
import { ReactiveComponentModule } from '@ngrx/component';
import { XhrInterceptor } from './xhr.interceptor';
import { SearchKeywordsComponent } from './root/search-keywords/search-keywords.component';
import { SearchedPagesComponent } from './root/searched-pages/searched-pages.component';
import { CrawledPagesComponent } from './root/crawled-pages/crawled-pages.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard]},

  // { path: 'dashbord', component: DashbordComponent, canActivate: [AuthGuard] },

  { path: 'search-keywords', component: SearchKeywordsComponent, canActivate: [AuthGuard] },
  { path: 'searched-pages', component: SearchedPagesComponent, canActivate: [AuthGuard] },
  { path: 'crawled-pages', component: CrawledPagesComponent, canActivate: [AuthGuard] },

  { path: '', redirectTo: '/crawled-pages', pathMatch: 'prefix' }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    // DashbordComponent,
    SearchKeywordsComponent,
    SearchedPagesComponent,
    CrawledPagesComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    ReactiveComponentModule,
    HttpClientModule,
    GraphQLModule,
    RouterModule.forRoot(routes),
    StoreModule.forRoot({ root: rootState.rootReducer }),
    StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: environment.production }),
    EffectsModule.forRoot([rootState.RootEffects]),

    NgbCollapseModule,
    NgbDropdownModule,
    NgbModalModule,
    NgbPaginationModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

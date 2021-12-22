import {NgModule} from '@angular/core';
import {ApolloModule, APOLLO_OPTIONS} from 'apollo-angular';
import {ApolloClientOptions, ApolloLink, InMemoryCache} from '@apollo/client/core';
import {HttpLink} from 'apollo-angular/http';
import { onError, ErrorResponse } from "@apollo/client/link/error";
import { Store } from '@ngrx/store';
import { RootActions } from './root/state';

const uri = '/api';
export function createApollo(httpLink: HttpLink, store: Store): ApolloClientOptions<any> {
  let errorLink = onError((error: ErrorResponse) => {
    if (error.graphQLErrors
      ?.find(error =>
        error.extensions?.['type'] == 'AccessDeniedException'
      )) {
      store.dispatch(RootActions.logoutByServer());
    }
  });

  return {
    link: ApolloLink.from([errorLink, httpLink.create({uri})]),

    // link: httpLink.create({uri}),
    cache: new InMemoryCache(),
    defaultOptions: {
      query: {
        fetchPolicy: 'no-cache'
      },
      mutate: {
        fetchPolicy: 'no-cache'
      }
    }
  };
}

@NgModule({
  exports: [ApolloModule],
  providers: [
    {
      provide: APOLLO_OPTIONS,
      useFactory: createApollo,
      deps: [HttpLink, Store],
    },
  ],
})
export class GraphQLModule {}

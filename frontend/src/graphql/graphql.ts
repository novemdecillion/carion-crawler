import { gql } from 'apollo-angular';
import { Injectable } from '@angular/core';
import * as Apollo from 'apollo-angular';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  Date: any;
  DateTime: any;
};

export type Account = {
  __typename?: 'Account';
  role: Role;
  username: Scalars['ID'];
};

export type CrawledPage = {
  __typename?: 'CrawledPage';
  crawledAt: Scalars['DateTime'];
  exclude: Scalars['Boolean'];
  existData: Scalars['Boolean'];
  html?: Maybe<Scalars['String']>;
  note?: Maybe<Scalars['String']>;
  searchedAt: Scalars['DateTime'];
  status: CrawledStatus;
  url: Scalars['String'];
};

export enum CrawledStatus {
  DifferentHost = 'DIFFERENT_HOST',
  Download = 'DOWNLOAD',
  Error = 'ERROR',
  ExceedDepth = 'EXCEED_DEPTH',
  NoKeyword = 'NO_KEYWORD',
  Success = 'SUCCESS'
}

export type Mutation = {
  __typename?: 'Mutation';
  addSearchKeyword?: Maybe<Scalars['Boolean']>;
  deleteSearchKeyword?: Maybe<Scalars['Boolean']>;
  login?: Maybe<Account>;
  logout?: Maybe<Scalars['Boolean']>;
  setCrawledPageUrlFilters?: Maybe<Scalars['Boolean']>;
};


export type MutationAddSearchKeywordArgs = {
  keyword: Scalars['String'];
};


export type MutationDeleteSearchKeywordArgs = {
  keyword: Scalars['String'];
};


export type MutationLoginArgs = {
  password?: InputMaybe<Scalars['String']>;
  username: Scalars['String'];
};


export type MutationSetCrawledPageUrlFiltersArgs = {
  urlFilters?: InputMaybe<Array<Scalars['String']>>;
};

export type Query = {
  __typename?: 'Query';
  crawledPageCount: Scalars['Int'];
  crawledPages?: Maybe<Array<CrawledPage>>;
  getCrawledPageUrlFilters?: Maybe<Array<Scalars['String']>>;
  myAccount?: Maybe<Account>;
  ping?: Maybe<Scalars['Boolean']>;
  searchKeywordCount: Scalars['Int'];
  searchKeywords?: Maybe<Array<SearchKeyword>>;
  searchedPageCount: Scalars['Int'];
  searchedPages?: Maybe<Array<SearchedPage>>;
};


export type QueryCrawledPageCountArgs = {
  enableUrlFilter?: InputMaybe<Scalars['Boolean']>;
  status?: InputMaybe<Array<CrawledStatus>>;
};


export type QueryCrawledPagesArgs = {
  enableUrlFilter?: InputMaybe<Scalars['Boolean']>;
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
  status?: InputMaybe<Array<InputMaybe<CrawledStatus>>>;
};


export type QuerySearchKeywordsArgs = {
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
};


export type QuerySearchedPagesArgs = {
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
};

export enum Role {
  Admin = 'ADMIN'
}

export type SearchKeyword = {
  __typename?: 'SearchKeyword';
  keyword: Scalars['String'];
  searchedAt?: Maybe<Scalars['Date']>;
};

export type SearchedPage = {
  __typename?: 'SearchedPage';
  createAt: Scalars['DateTime'];
  url: Scalars['String'];
};

export type AccountFragment = { __typename?: 'Account', username: string, role: Role };

export type SearchedPageFragment = { __typename?: 'SearchedPage', url: string, createAt: any };

export type SearchKeywordFragment = { __typename?: 'SearchKeyword', keyword: string, searchedAt?: any | null | undefined };

export type CrawledPageFragment = { __typename?: 'CrawledPage', url: string, status: CrawledStatus, note?: string | null | undefined, exclude: boolean, crawledAt: any, existData: boolean };

export type PingQueryVariables = Exact<{ [key: string]: never; }>;


export type PingQuery = { __typename?: 'Query', ping?: boolean | null | undefined };

export type MyAccountQueryVariables = Exact<{ [key: string]: never; }>;


export type MyAccountQuery = { __typename?: 'Query', myAccount?: { __typename?: 'Account', username: string, role: Role } | null | undefined };

export type SearchedPagesQueryVariables = Exact<{
  skipCount?: InputMaybe<Scalars['Boolean']>;
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
}>;


export type SearchedPagesQuery = { __typename?: 'Query', searchedPageCount?: number, searchedPages?: Array<{ __typename?: 'SearchedPage', url: string, createAt: any }> | null | undefined };

export type SearchKeywordsQueryVariables = Exact<{
  skipCount?: InputMaybe<Scalars['Boolean']>;
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
}>;


export type SearchKeywordsQuery = { __typename?: 'Query', searchKeywordCount?: number, searchKeywords?: Array<{ __typename?: 'SearchKeyword', keyword: string, searchedAt?: any | null | undefined }> | null | undefined };

export type CrawledPagesQueryVariables = Exact<{
  skipCount?: InputMaybe<Scalars['Boolean']>;
  status?: InputMaybe<Array<CrawledStatus> | CrawledStatus>;
  enableUrlFilter?: InputMaybe<Scalars['Boolean']>;
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
}>;


export type CrawledPagesQuery = { __typename?: 'Query', crawledPageCount?: number, crawledPages?: Array<{ __typename?: 'CrawledPage', url: string, status: CrawledStatus, note?: string | null | undefined, exclude: boolean, crawledAt: any, existData: boolean }> | null | undefined };

export type GetCrawledPageUrlFiltersQueryVariables = Exact<{ [key: string]: never; }>;


export type GetCrawledPageUrlFiltersQuery = { __typename?: 'Query', getCrawledPageUrlFilters?: Array<string> | null | undefined };

export type LoginMutationVariables = Exact<{
  username: Scalars['String'];
  password: Scalars['String'];
}>;


export type LoginMutation = { __typename?: 'Mutation', login?: { __typename?: 'Account', username: string, role: Role } | null | undefined };

export type LogoutMutationVariables = Exact<{ [key: string]: never; }>;


export type LogoutMutation = { __typename?: 'Mutation', logout?: boolean | null | undefined };

export type AddSearchKeywordMutationVariables = Exact<{
  keyword: Scalars['String'];
}>;


export type AddSearchKeywordMutation = { __typename?: 'Mutation', addSearchKeyword?: boolean | null | undefined };

export type DeleteSearchKeywordMutationVariables = Exact<{
  keyword: Scalars['String'];
}>;


export type DeleteSearchKeywordMutation = { __typename?: 'Mutation', deleteSearchKeyword?: boolean | null | undefined };

export type SetCrawledPageUrlFiltersMutationVariables = Exact<{
  urlFilters?: InputMaybe<Array<Scalars['String']> | Scalars['String']>;
}>;


export type SetCrawledPageUrlFiltersMutation = { __typename?: 'Mutation', setCrawledPageUrlFilters?: boolean | null | undefined };

export const AccountFragmentDoc = gql`
    fragment Account on Account {
  username
  role
}
    `;
export const SearchedPageFragmentDoc = gql`
    fragment SearchedPage on SearchedPage {
  url
  createAt
}
    `;
export const SearchKeywordFragmentDoc = gql`
    fragment SearchKeyword on SearchKeyword {
  keyword
  searchedAt
}
    `;
export const CrawledPageFragmentDoc = gql`
    fragment CrawledPage on CrawledPage {
  url
  status
  note
  exclude
  crawledAt
  existData
}
    `;
export const PingDocument = gql`
    query Ping {
  ping
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class PingGQL extends Apollo.Query<PingQuery, PingQueryVariables> {
    document = PingDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const MyAccountDocument = gql`
    query MyAccount {
  myAccount {
    ...Account
  }
}
    ${AccountFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class MyAccountGQL extends Apollo.Query<MyAccountQuery, MyAccountQueryVariables> {
    document = MyAccountDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const SearchedPagesDocument = gql`
    query searchedPages($skipCount: Boolean = true, $limit: Int, $offset: Int) {
  searchedPageCount @skip(if: $skipCount)
  searchedPages(limit: $limit, offset: $offset) {
    ...SearchedPage
  }
}
    ${SearchedPageFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class SearchedPagesGQL extends Apollo.Query<SearchedPagesQuery, SearchedPagesQueryVariables> {
    document = SearchedPagesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const SearchKeywordsDocument = gql`
    query searchKeywords($skipCount: Boolean = true, $limit: Int, $offset: Int) {
  searchKeywordCount @skip(if: $skipCount)
  searchKeywords(limit: $limit, offset: $offset) {
    ...SearchKeyword
  }
}
    ${SearchKeywordFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class SearchKeywordsGQL extends Apollo.Query<SearchKeywordsQuery, SearchKeywordsQueryVariables> {
    document = SearchKeywordsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const CrawledPagesDocument = gql`
    query crawledPages($skipCount: Boolean = true, $status: [CrawledStatus!] = [SUCCESS, NO_KEYWORD], $enableUrlFilter: Boolean, $limit: Int, $offset: Int) {
  crawledPageCount(status: $status, enableUrlFilter: $enableUrlFilter) @skip(if: $skipCount)
  crawledPages(
    status: $status
    enableUrlFilter: $enableUrlFilter
    limit: $limit
    offset: $offset
  ) {
    ...CrawledPage
  }
}
    ${CrawledPageFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class CrawledPagesGQL extends Apollo.Query<CrawledPagesQuery, CrawledPagesQueryVariables> {
    document = CrawledPagesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const GetCrawledPageUrlFiltersDocument = gql`
    query getCrawledPageUrlFilters {
  getCrawledPageUrlFilters
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class GetCrawledPageUrlFiltersGQL extends Apollo.Query<GetCrawledPageUrlFiltersQuery, GetCrawledPageUrlFiltersQueryVariables> {
    document = GetCrawledPageUrlFiltersDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const LoginDocument = gql`
    mutation Login($username: String!, $password: String!) {
  login(username: $username, password: $password) {
    ...Account
  }
}
    ${AccountFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class LoginGQL extends Apollo.Mutation<LoginMutation, LoginMutationVariables> {
    document = LoginDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const LogoutDocument = gql`
    mutation Logout {
  logout
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class LogoutGQL extends Apollo.Mutation<LogoutMutation, LogoutMutationVariables> {
    document = LogoutDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddSearchKeywordDocument = gql`
    mutation addSearchKeyword($keyword: String!) {
  addSearchKeyword(keyword: $keyword)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddSearchKeywordGQL extends Apollo.Mutation<AddSearchKeywordMutation, AddSearchKeywordMutationVariables> {
    document = AddSearchKeywordDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteSearchKeywordDocument = gql`
    mutation deleteSearchKeyword($keyword: String!) {
  deleteSearchKeyword(keyword: $keyword)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteSearchKeywordGQL extends Apollo.Mutation<DeleteSearchKeywordMutation, DeleteSearchKeywordMutationVariables> {
    document = DeleteSearchKeywordDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const SetCrawledPageUrlFiltersDocument = gql`
    mutation setCrawledPageUrlFilters($urlFilters: [String!]) {
  setCrawledPageUrlFilters(urlFilters: $urlFilters)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class SetCrawledPageUrlFiltersGQL extends Apollo.Mutation<SetCrawledPageUrlFiltersMutation, SetCrawledPageUrlFiltersMutationVariables> {
    document = SetCrawledPageUrlFiltersDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
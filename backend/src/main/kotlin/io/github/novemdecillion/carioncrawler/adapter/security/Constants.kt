package io.github.novemdecillion.carioncrawler.adapter.security

const val ONLY_ADMIN = "hasAuthority('ADMIN')"
const val ONLY_CREDITOR = "hasAuthority('CREDITOR')"
const val ONLY_ADMIN_OR_CREDITOR = "$ONLY_ADMIN or $ONLY_CREDITOR"
const val AUTHENTICATED = "isAuthenticated()"
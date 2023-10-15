package com.hilton.demo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

private var instance: ApolloClient? = null

fun apolloClient(): ApolloClient {
    if (instance != null) {
        return instance!!
    }
    val okHttpClient = OkHttpClient.Builder().build()
    instance = ApolloClient.Builder()
        .serverUrl("https://beta.pokeapi.co/graphql/v1beta")
        .okHttpClient(okHttpClient)
        .build()
    return instance!!
}



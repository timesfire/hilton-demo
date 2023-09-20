package com.hilton.demo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

private var instance: ApolloClient? = null

fun apolloClient(): ApolloClient {
    if (instance != null) {
        return instance!!
    }

    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.sslSocketFactory(SSLSocketClient.sSLSocketFactory, SSLSocketClient.trustManager)
    val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory = SSLSocketClient.sSLSocketFactory, trustManager = SSLSocketClient.trustManager)
        .hostnameVerifier(SSLSocketClient.hostnameVerifier)
        .build()

    instance = ApolloClient.Builder()
        /*.serverUrl("https://graphql.postman-echo.com/graphql")   测试用 */
        .serverUrl("https://api.graphql.jobs/")
        .okHttpClient(okHttpClient)
        .build()
    return instance!!
}


object SSLSocketClient {
    val sSLSocketFactory: SSLSocketFactory
        //获取这个SSLSocketFactory
        get() = try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf(trustManager), SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    val trustManager: X509TrustManager
        //获取TrustManager
        get() {
            return object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

            }


        }
    val hostnameVerifier: HostnameVerifier
        get() {
            return object : HostnameVerifier {
                override fun verify(s: String, sslSession: SSLSession): Boolean {
                    return true
                }
            }
        }
}
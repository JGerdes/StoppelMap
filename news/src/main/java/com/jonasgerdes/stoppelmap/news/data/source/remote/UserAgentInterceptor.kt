package com.jonasgerdes.stoppelmap.news.data.source.remote

import com.jonasgerdes.stoppelmap.core.domain.AppVersionProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val USER_AGENT_HEADER = "User-Agent"

class UserAgentInterceptor
@Inject
constructor(
    private val appVersionProvider: AppVersionProvider
) : Interceptor {

    fun getSystemUserAgent() = try {
        System.getProperty("http.agent")
    } catch (e: Exception) {
        ""
    }

    private val userAgent get() = "StoppelMap/${appVersionProvider.getAppVersionName} {${getSystemUserAgent()}}"

    override fun intercept(chain: Interceptor.Chain) = chain.run {
        proceed(
            request()
                .newBuilder()
                .header(USER_AGENT_HEADER, userAgent)
                .build()
        )
    }
}
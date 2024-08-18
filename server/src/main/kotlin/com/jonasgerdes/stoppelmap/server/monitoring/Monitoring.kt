package com.jonasgerdes.stoppelmap.server.monitoring

import com.jonasgerdes.stoppelmap.server.util.parseUserAgent
import io.ktor.server.metrics.micrometer.MicrometerMetricsConfig
import io.ktor.server.request.path
import io.ktor.server.request.userAgent
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.slf4j.Logger
import java.util.concurrent.atomic.AtomicInteger


class Monitoring(
    private val logger: Logger
) {

    private val registry: PrometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    private val taskCounter = registry.gauge("stoppemap.server.tasks.running", AtomicInteger(0))
    val newsCounter = registry.gauge("stoppemap.server.news.all", AtomicInteger(0))
    val visibleNewsCounter = registry.gauge("stoppemap.server.news.visible", AtomicInteger(0))
    val newsCrawlLatestTimer = registry.timer("stoppemap.server.news.crawl.latest")
    val newsFetchArticleTimer = registry.timer("stoppemap.server.news.fetch.article")

    suspend fun recordTask(taskName: String, block: suspend () -> Unit) {
        taskCounter?.getAndIncrement()
        val timer = Timer.start(registry)
        block()
        timer.stop(registry.timer("stoppemap.server.tasks", listOf(Tag.of("task.name", taskName))))
        taskCounter?.getAndDecrement()
    }

    fun install(micrometerMetricsConfig: MicrometerMetricsConfig) = with(micrometerMetricsConfig) {
        registry = this@Monitoring.registry
        distinctNotRegisteredRoutes = false

        timers { call, throwable ->
            if (call.request.path() == "/static/app-config.json") {
                tag("route", "/static/app-config.json")
            }
            val userAgentString = call.request.userAgent()
            val userAgent = userAgentString?.parseUserAgent()
            tag("app.version", userAgent?.appVersion ?: "n/a")
            tag("build.type", userAgent?.buildType ?: "n/a")
            tag("os", userAgent?.os ?: "n/a")
            tag("manufacturer", userAgent?.manufacturer ?: "n/a")
            tag("device", userAgent?.device ?: "n/a")

            if (userAgent == null && call.request.path() != "/metrics") {
                logger.info("Unrecognized user agent $userAgentString")
            }
        }
    }

    fun provideMetrics() = registry.scrape()

}
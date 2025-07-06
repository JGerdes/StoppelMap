package com.jonasgerdes.stoppelmap.server

import ch.qos.logback.classic.Logger
import com.jonasgerdes.stoppelmap.server.appconfig.appConfigModule
import com.jonasgerdes.stoppelmap.server.appconfig.appConfigRoutes
import com.jonasgerdes.stoppelmap.server.config.toServerConfig
import com.jonasgerdes.stoppelmap.server.crawler.crawlerModule
import com.jonasgerdes.stoppelmap.server.crawler.crawlerTasksModule
import com.jonasgerdes.stoppelmap.server.monitoring.Monitoring
import com.jonasgerdes.stoppelmap.server.monitoring.monitoringModule
import com.jonasgerdes.stoppelmap.server.monitoring.monitoringRoutes
import com.jonasgerdes.stoppelmap.server.news.newsModule
import com.jonasgerdes.stoppelmap.server.news.newsRoutes
import com.jonasgerdes.stoppelmap.server.scheduler.TaskScheduler
import com.jonasgerdes.stoppelmap.server.scheduler.schedulerModule
import com.jonasgerdes.stoppelmap.server.statusexceptions.AuthorizationException
import io.ktor.http.CacheControl
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.host
import io.ktor.server.application.install
import io.ktor.server.application.port
import io.ktor.server.http.content.staticFiles
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.LoggerFactory
import java.io.File
import ch.qos.logback.classic.Level as LogbackLevel

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.ktorModule() {
    val serverConfig = environment.config.toServerConfig(
        host = environment.config.let { "${it.host}:${it.port}" }
    )
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger)
        .level = if (serverConfig.environment.isDev) LogbackLevel.ALL else LogbackLevel.INFO

    environment.log.info("Starting version ${serverConfig.version} with environment ${serverConfig.environment}")

    install(CallLogging)
    install(Compression)
    install(CachingHeaders)
    install(StatusPages) {
        exception<Throwable> { call: ApplicationCall, cause ->
            val status = when (cause) {
                is AuthorizationException -> HttpStatusCode.Forbidden
                else -> HttpStatusCode.InternalServerError
            }
            if (serverConfig.environment.isDev) {
                call.respondText(text = "${status.value}: $cause", status = status)
            } else {
                call.respond(status)

            }
        }
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = serverConfig.environment.isDev
            isLenient = serverConfig.environment.isPrd
        })
    }

    install(Koin) {
        printLogger(if (serverConfig.environment.isDev) Level.DEBUG else Level.INFO)
        modules(
            module {
                single { serverConfig }
                single { environment.log }
            },
            applicationModule,
            monitoringModule,
            crawlerModule,
            crawlerTasksModule(serverConfig.crawler),
            newsModule,
            schedulerModule,
            appConfigModule,
        )
    }

    install(MicrometerMetrics) {
        val monitoring: Monitoring by this@ktorModule.inject()
        monitoring.install(this)
    }

    routing {
        monitoringRoutes()
        newsRoutes()
        appConfigRoutes()

        staticFiles("/static/images", File(serverConfig.crawler.imageCacheDir, "processed")) {
            enableAutoHeadResponse()
            cacheControl {
                listOf(
                    CacheControl.MaxAge(
                        maxAgeSeconds = 604800, // year
                        visibility = CacheControl.Visibility.Public,
                    )
                )
            }
        }

        staticFiles("/static", File(serverConfig.staticDirectory)) {
            enableAutoHeadResponse()
        }
    }

    scheduleTasks()
}

@OptIn(DelicateCoroutinesApi::class)
fun Application.scheduleTasks() {
    val taskScheduler by inject<TaskScheduler>()

    GlobalScope.launch {
        taskScheduler.run()
    }
}


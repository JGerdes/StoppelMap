package com.jonasgerdes.stoppelmap.server

import ch.qos.logback.classic.Logger
import com.jonasgerdes.stoppelmap.server.appconfig.appConfigModule
import com.jonasgerdes.stoppelmap.server.config.toServerConfig
import com.jonasgerdes.stoppelmap.server.crawler.crawlerModule
import com.jonasgerdes.stoppelmap.server.crawler.crawlerTasksModule
import com.jonasgerdes.stoppelmap.server.news.newsModule
import com.jonasgerdes.stoppelmap.server.news.newsRoutes
import com.jonasgerdes.stoppelmap.server.scheduler.TaskScheduler
import com.jonasgerdes.stoppelmap.server.scheduler.schedulerModule
import io.ktor.http.CacheControl
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
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
        host = (environment as ApplicationEngineEnvironment).connectors.first()
            .let { "${it.host}:${it.port}" }
    )
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger)
        .level = if (appConfig.environment.isDev) LogbackLevel.ALL else LogbackLevel.INFO

    environment.log.info("Starting version ${appConfig.version} with environment ${appConfig.environment}")
    install(CallLogging)
    install(Compression)
    install(CachingHeaders)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = appConfig.environment.isDev
            isLenient = appConfig.environment.isPrd
        })
    }
    install(Koin) {
        printLogger(if (appConfig.environment.isDev) Level.DEBUG else Level.INFO)
        modules(
            module {
                single { appConfig }
                single { environment.log }
            },
            applicationModule,
            crawlerModule,
            crawlerTasksModule(appConfig.crawler),
            newsModule,
            schedulerModule,
        )
    }

    routing {
        newsRoutes()

        staticFiles("/static/images", File(appConfig.crawler.imageCacheDir, "processed")) {
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


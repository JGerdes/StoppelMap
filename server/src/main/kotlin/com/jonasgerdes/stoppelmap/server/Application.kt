package com.jonasgerdes.stoppelmap.server

import com.jonasgerdes.stoppelmap.server.config.toAppConfig
import com.jonasgerdes.stoppelmap.server.crawler.StoppelmarktWebsiteCrawler
import com.jonasgerdes.stoppelmap.server.crawler.crawlerModule
import com.jonasgerdes.stoppelmap.server.data.dataModule
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task
import com.jonasgerdes.stoppelmap.server.scheduler.TaskScheduler
import io.ktor.http.CacheControl
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.routing.routing
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.ktorModule() {
    val appConfig = environment.config.toAppConfig()
    environment.log.info("Starting with environment ${appConfig.environment}")
    install(CallLogging)
    install(Compression)
    install(CachingHeaders)
    install(Koin) {
        printLogger(if (appConfig.environment.isDev) Level.DEBUG else Level.INFO)
        modules(
            module {
                single { appConfig }
                single { environment.log }
                single {
                    val crawler = get<StoppelmarktWebsiteCrawler>()
                    TaskScheduler(
                        tasks = listOf(
                            Task(Schedule.Hourly(), executeOnceImmediately = true) {
                                crawler.crawlNews()
                            }
                        ),
                        clockProvider = get(),
                    )
                }
            },
            applicationModule,
            dataModule,
            crawlerModule,
        )
    }

    routing {
        get("/") {
            call.respondText("Hello Stoppelmarkt!")
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


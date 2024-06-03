package com.jonasgerdes.stoppelmap.server

import com.jonasgerdes.stoppelmap.server.config.toAppConfig
import com.jonasgerdes.stoppelmap.server.crawler.StoppelmarktWebsiteCrawler
import com.jonasgerdes.stoppelmap.server.scheduler.ClockProvider
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task
import com.jonasgerdes.stoppelmap.server.scheduler.TaskScheduler
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.ktorModule() {
    val appConfig = environment.config.toAppConfig()
    environment.log.info("Starting with environment ${appConfig.environment}")
    install(CallLogging)
    install(Koin) {
        printLogger(if (appConfig.environment.isDev) Level.DEBUG else Level.INFO)
        modules(
            module {
                single { appConfig }
                single { environment.log }
                single<ClockProvider> { ClockProvider { Clock.System.now() } }
                single {
                    StoppelmarktWebsiteCrawler(
                        baseUrl = appConfig.crawler.baseUrl,
                        version = appConfig.version,
                        logger = get(),
                        slowMode = appConfig.crawler.slowMode,
                    )
                }
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
        )
    }

    routing {
        get("/") {
            call.respondText("Hello Stoppelmarkt!")
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


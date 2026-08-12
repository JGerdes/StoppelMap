@file:OptIn(ExperimentalSerializationApi::class)

package com.jonasgerdes.stoppelmap.preparation.transportation.crawler

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.plus
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


class VbnCrawler(
    private val slowMode: Boolean = true,
    private val hourStep: Int = 0,
    private val folder: File
) {

    private val json = Json {
        prettyPrint = true
    }

    private val httpClient by lazy {
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
            install(UserAgent) {
                agent = "StoppelBot} (https://stoppelmap.de/bot)"
            }
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                })
            }
        }
    }

    suspend operator fun invoke(dates: List<LocalDate>) {
        println("fetching journeys for $dates")

        routes.forEachIndexed { routeIndex, stations ->
            val routeName = stations.first().name.removeSuffix("Hbf").trim().lowercase()
            dates.map { date ->
                val journeys = stations
                    .mapIndexed { stationIndex, station ->
                        (0..23 step hourStep).map { hour ->
                            val time = LocalTime(hour = hour, minute = 0)
                            val start = LocalDateTime(date, time)
                            println("find journeys at $start from $station ($stationIndex/${stations.size}, route $routeIndex/${routes.size})")
                            findJourneys(
                                departureStation = station,
                                arrivalStation = stoppelMarktStation,
                                start = start
                            ).flatMap { it.stops }
                        }.flatten()
                    }.flatten()


                File(folder, "journeys_${routeName}_${date}.json").outputStream().use {
                    json.encodeToStream(journeys, it)
                }
            }
        }

        routes.forEachIndexed { routeIndex, stations ->
            val routeName = stations.first().name.removeSuffix("Hbf").trim().lowercase()
            dates.let {
                it + it.maxOf { it }.plus(DatePeriod(days = 1))
            }.map { date ->
                val journeys = stations
                    .mapIndexed { stationIndex, station ->
                        (0..23).map { hour ->
                            val time = LocalTime(hour = hour, minute = 0)
                            val start = LocalDateTime(date, time)
                            println("find returns at $start to $station ($stationIndex/${stations.size}, route $routeIndex/${routes.size})")
                            findJourneys(
                                departureStation = stoppelMarktStation,
                                arrivalStation = station,
                                start = start
                            ).flatMap { it.stops }
                        }.flatten()
                    }.flatten()


                File(folder, "returns_${routeName}_${date}.json").outputStream().use {
                    json.encodeToStream(journeys, it)
                }
            }
        }
    }

    private suspend fun findJourneys(
        departureStation: Station,
        arrivalStation: Station,
        start: LocalDateTime,
    ): List<Journey> {
        pause()
        val apiResponse = executeRequest(
            departureStation = departureStation,
            arrivalStation = arrivalStation,
            start = start
        )
        apiResponse.status.takeIf { it != HttpStatusCode.OK }?.let {
            System.err.println("status: $it")
        }
        val data: JsonElement = apiResponse.body()
        val response = try {
            data.jsonObject["svcResL"]!!.jsonArray[0].jsonObject["res"]!!.jsonObject
        } catch (exception: Exception) {
            System.err.println("Failed to parse response: $data")
            exception.printStackTrace()
            runCatching {
                System.err.println("\t\tfull response: ${apiResponse.bodyAsText()}")
            }
            System.err.println("\t\trequested journey for: $departureStation, $apiResponse, $start")
            return emptyList()
        }
        val locations = try {
            response["common"]!!.jsonObject["locL"]!!.jsonArray.map { it.jsonObject }
        } catch (exception: Exception) {
            System.err.println("W: Error during location parsing")
            exception.printStackTrace()
            runCatching {
                System.err.println("\t\tResponse: $data")
                System.err.println("\t\tfull response: ${apiResponse.bodyAsText()}")
            }
            emptyList()
        }

        val journeys = try {
            response["outConL"]!!.jsonArray.map {
                it.jsonObject["secL"]!!.jsonArray.first { it.jsonObject.containsKey("jny") }.jsonObject["jny"]!!.jsonObject
            }
        } catch (exception: Exception) {
            System.err.println("W: Error during journey parsing")
            exception.printStackTrace()
            runCatching {
                System.err.println("\t\tResponse: $data")
                System.err.println("\t\tfull response: ${apiResponse.bodyAsText()}")
            }
            emptyList()
        }

        val journeyReducedToStops = try {
            journeys
                // At the end of a day, the api also returns journeys from the next day. We fetch them excplicitly,
                // filter here so we don't assign them to the wrong day
                .filter {
                    try {
                        it["date"]!!.jsonPrimitive.content.let { dateFormat.parse(it) == start.date }
                    } catch (exception: Exception) {
                        System.err.println("Failed to get date for $it")
                        exception.printStackTrace()
                        false
                    }
                }
                .map { it["stopL"]!!.jsonArray.map { it.jsonObject } }
        } catch (exception: Exception) {
            System.err.println("Failed to reduce stops")
            exception.printStackTrace()
            runCatching {
                System.err.println("\t\tjourneys: $journeys")
            }
            emptyList()
        }

        val simplifiedJourneys = journeyReducedToStops.map { journeyStops ->
            Journey(
                journeyStops.mapNotNull {
                    try {
                        val locX = it["locX"]!!.jsonPrimitive.int
                        val location = locations[locX]
                        Stop(
                            name = location["name"]!!.jsonPrimitive.content,
                            lid = location["lid"]!!.jsonPrimitive.content,
                            extId = location["extId"]?.jsonPrimitive?.content,
                            arrivalScheduled = it["aTimeS"]?.jsonPrimitive?.content,
                            departureScheduled = it["dTimeS"]?.jsonPrimitive?.content,
                            arrivalRealtime = it["aTimeR"]?.jsonPrimitive?.content,
                            departureRealtime = it["dTimeR"]?.jsonPrimitive?.content,
                        )
                    } catch (exception: Exception) {
                        System.err.println("Failed to simplify $journeyStops")
                        exception.printStackTrace()
                        runCatching {
                            System.err.println("\t\tlocations: $locations")
                        }
                        null
                    }
                })
        }
        return simplifiedJourneys
    }

    @Serializable
    data class Journey(
        val stops: List<Stop>
    )

    @Serializable
    data class Stop(
        val name: String,
        val lid: String,
        val extId: String?,
        val arrivalScheduled: String?,
        val departureScheduled: String?,
        val arrivalRealtime: String?,
        val departureRealtime: String?
    )

    private suspend fun pause() {
        if (slowMode) {
            print("Waiting")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print(".")
            delay(Random.nextInt(523, 1456).milliseconds)
            print("\n")
        }
    }


    suspend fun executeRequest(
        departureStation: Station,
        arrivalStation: Station,
        start: LocalDateTime,
    ): HttpResponse {
        val response = httpClient.post("https://fahrplaner.vbn.de/gate?rnd=1786452726525") {
            headersOf(
                "Origin" to listOf("https://fahrplaner.vbn.de")
            )
            setBody(
                assembleRequestBody(
                    departureStation = departureStation,
                    arrivalStation = arrivalStation,
                    start = start,
                )
            )
        }
        return response
    }

    val routes =
        listOf(
            listOf(
                Station(
                    lid = "A=1@O=Bremen Hbf@X=8813510@Y=53082992@U=80@L=4900050@",
                    name = "Bremen Hbf",
                ),
                Station(
                    lid = "A=1@O=Bremen Neustadt@X=8785886@Y=53076008@U=80@L=4901155@",
                    name = "Bremen Neustadt",
                ),
                Station(
                    lid = "A=1@O=Delmenhorst@X=8630121@Y=53052762@U=80@L=8000070@",
                    name = "Delmenhorst",
                ),
                Station(
                    lid = "A=1@O=Ganderkesee@X=8542791@Y=53035188@U=80@L=8002179@",
                    name = "Ganderkesee",
                ),
                Station(
                    lid = "A=1@O=Brettorf@X=8447038@Y=52967112@U=80@L=8001176@",
                    name = "Brettorf",
                ),
                Station(
                    lid = "A=1@O=Wildeshausen@X=8431127@Y=52897437@U=80@L=8006436@",
                    name = "Wildeshausen",
                ),
                Station(
                    lid = "A=1@O=Rechterfeld@X=8391224@Y=52838773@U=80@L=8004970@",
                    name = "Rechterfeld",
                ),
                Station(
                    lid = "A=1@O=Goldenstedt(Oldb)@X=8394433@Y=52800578@U=80@L=8002320@",
                    name = "Goldenstedt(Oldb)",
                ),
                Station(
                    lid = "A=1@O=Lutten@X=8343563@Y=52768019@U=80@L=8003800@",
                    name = "Lutten",
                ),
            ),
            listOf(
                Station(
                    lid = "A=1@O=Osnabrück Hbf@X=8060052@Y=52273703@U=80@L=4930294@",
                    name = "Osnabrück Hbf",
                ),
                Station(
                    lid = "A=1@O=Osnabrück Altstadt@X=8045346@Y=52281271@U=80@L=4900689@",
                    name = "Osnabrück Altstadt",
                ),
                Station(
                    lid = "A=1@O=Halen Bahnhof@X=7945044@Y=52339126@U=80@L=8002537@",
                    name = "Halen Bahnhof",
                ),
                Station(
                    lid = "A=1@O=Achmer@X=7941988@Y=52390580@U=80@L=4900414@",
                    name = "Achmer",
                ),
                Station(
                    lid = "A=1@O=Bramsche(Hase)@X=7975050@Y=52410851@U=80@L=8001122@",
                    name = "Bramsche(Hase)",
                ),
                Station(
                    lid = "A=1@O=Hesepe@X=7967463@Y=52439545@U=80@L=8000167@",
                    name = "Hesepe",
                ),
                Station(
                    lid = "A=1@O=Rieste@X=8010755@Y=52484940@U=80@L=8005096@",
                    name = "Rieste",
                ),
                Station(
                    lid = "A=1@O=Neuenkirchen(Oldb)@X=8059584@Y=52508321@U=80@L=4904271@",
                    name = "Neuenkirchen(Oldb)",
                ),
                Station(
                    lid = "A=1@O=Holdorf(Oldb)@X=8138051@Y=52574814@U=80@L=8002962@",
                    name = "Holdorf(Oldb)",
                ),
                Station(
                    lid = "A=1@O=Steinfeld(Oldb)@X=8197946@Y=52592577@U=80@L=8005703@",
                    name = "Steinfeld(Oldb)",
                ),
                Station(
                    lid = "A=1@O=Mühlen(Oldb)@X=8208437@Y=52619221@U=80@L=8004105@",
                    name = "Mühlen(Oldb)",
                ),
                Station(
                    lid = "A=1@O=Lohne(Oldb)@X=8229346@Y=52665381@U=80@L=4903738@",
                    name = "Lohne(Oldb)",
                ),
                Station(
                    lid = "A=1@O=Vechta@X=8280548@Y=52729096@U=80@L=4906061@",
                    name = "Vechta",
                ),
            )
        )

    val stoppelMarktStation = Station(
        lid = "A=1@O=Vechta-Stoppelmarkt@X=8292046@Y=52748369@U=80@L=9087020@p=1786407100@",
        name = "Vechta-Stoppelmarkt",
    )

    data class Station(
        val lid: String,
        val name: String,
    )

    enum class EtePrefix(val prefix: String) {
        Arrival("arr_0"),
        Departure("dep_0")
    }

    fun String.eteIdFromLid(prefix: EtePrefix): String = try {
        val parts = split("@")
            .filter { it.isNotBlank() && it.contains("=") }
            .map { it.split("=").let { it[0] to it[1] } }.toMap()
        return listOf(prefix.prefix, "S", parts["O"], parts["L"], parts["X"], parts["Y"]).joinToString("|")
    } catch (ioobe: IndexOutOfBoundsException) {
        throw IllegalArgumentException("Failed to parse [$this].", ioobe)
    }


    private fun assembleRequestBody(
        departureStation: Station,
        arrivalStation: Station,
        start: LocalDateTime,
    ) = """
        {
          "id": "ixw8tuemw8kz9m4k",
          "ver": "1.66",
          "lang": "deu",
          "auth": {
            "type": "AID",
            "aid": "vbn89n98n908cv"
          },
          "client": {
            "id": "VBN",
            "type": "WEB",
            "name": "webapp",
            "l": "vs_vbn",
            "v": 10008
          },
          "formatted": false,
          "svcReqL": [
            {
              "meth": "TripSearch",
              "req": {
                "jnyFltrL": [
                  {
                    "type": "GROUP",
                    "mode": "INC",
                    "value": "RQ_PT"
                  },
                  {
                    "type": "PROD",
                    "mode": "INC",
                    "value": 776
                  }
                ],
                "getPolyline": true,
                "getPasslist": true,
                "gisFltrL": [
                  {
                    "type": "P",
                    "mode": "FBT",
                    "profile": {
                      "type": "F",
                      "maxdist": "2000"
                    }
                  },
                  {
                    "type": "P",
                    "mode": "FB",
                    "profile": {
                      "type": "B",
                      "maxdist": "20000"
                    }
                  },
                  {
                    "type": "M",
                    "mode": "FBT",
                    "meta": "foot_speed_normal"
                  },
                  {
                    "type": "M",
                    "mode": "FBT",
                    "meta": "bike_speed_normal"
                  }
                ],
                "depLocL": [
                  {
                    "lid": "${departureStation.lid}",
                    "name": "${departureStation.name}",
                    "eteId": "${departureStation.lid.eteIdFromLid(EtePrefix.Departure)}"
                  }
                ],
                "arrLocL": [
                  {
                   "lid": "${arrivalStation.lid}",
                    "name": "${arrivalStation.name}",
                    "eteId": "${arrivalStation.lid.eteIdFromLid(EtePrefix.Arrival)}"
                  }
                ],
                "outFrwd": true,
                "outTime": "${start.time.format(timeFormat)}",
                "outDate": "${start.date.format(dateFormat)}",
                "ushrp": true,
                "liveSearch": false,
                "maxChg": "0",
                "minChgTime": "-1"
              },
              "id": "1|108|"
            }
          ]
        }""".trimIndent()


    companion object {
        private val timeFormat = LocalTime.Format {
            hour()
            minute()
            second()
        }
        private val dateFormat = LocalDate.Format {
            year()
            monthNumber()
            dayOfMonth()
        }
    }
}
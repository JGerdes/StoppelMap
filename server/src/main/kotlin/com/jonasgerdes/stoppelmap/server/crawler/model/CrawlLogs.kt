package com.jonasgerdes.stoppelmap.server.crawler.model

import org.slf4j.Logger

class CrawlLogs(
    val resource: String,
    val title: String? = null,
    private var messages: List<LogMessage> = emptyList()
) {
    val logs get() = this
    fun addLog(message: String, type: Type): CrawlLogs {
        messages = messages + LogMessage(message, type)
        return this
    }

    fun info(message: String) = addLog(message, Type.INFO)
    fun pending(message: String) = addLog(message, Type.PENDING)
    fun check(message: String) = addLog(message, Type.CHECK)
    fun warn(message: String) = addLog(message, Type.WARNING)
    fun error(message: String) = addLog(message, Type.ERROR)
    fun attach(message: String) = addLog(message, Type.ATTACHMENT)

    fun hasErrorOrWarning() = messages.any { it.type == Type.ERROR || it.type == Type.WARNING }

    fun logTo(logger: Logger) {
        logger.info("Start crawling ${title?.let { "$it " } ?: ""}of resource $resource")
        messages.forEach {
            when (it.type) {
                Type.WARNING -> logger.warn("${it.type.emoji} ${it.message}")
                Type.ERROR -> logger.error("${it.type.emoji} ${it.message}")
                Type.ATTACHMENT -> Unit // No-op
                else -> logger.info("${it.type.emoji} ${it.message}")
            }
        }
    }

    fun asFormattedString() = StringBuilder()
        .append("Start crawling ${title?.let { "$it " } ?: ""}of resource $resource\n")
        .apply {
            messages.forEach {
                append("${it.type.emoji} ${it.message}\n")
            }
        }
        .toString()


    data class LogMessage(val message: String, val type: Type)

    enum class Type(val emoji: String) {
        INFO("ℹ️"),
        PENDING("⏳"),
        CHECK("✅"),
        WARNING("⚠️"),
        ERROR("🚨"),
        ATTACHMENT("📎"),
    }
}

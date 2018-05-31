package com.jonasgerdes.stoppelmap.model

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField


object SQLiteConstants {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    const val jdbcUrlPrefix = "jdbc:sqlite:"
}

fun openSQLite(file: File): Connection? {
    if (!file.exists()) {
        file.createNewFile()
    }
    val url = "${SQLiteConstants.jdbcUrlPrefix}${file.absolutePath}"
    return DriverManager.getConnection(url)
}

fun Statement.createTable(obj: Any) {
    val table = obj::class.java.simpleName.toSnake().toPlural()
    val fields = obj::class.primaryConstructor!!.parameters.map {
        val type = (it.type.classifier as KClass<*>).toSqliteType()
        val name = it.name!!.toSnake()
        val notNull = if (it.type.isMarkedNullable) {
            ""
        } else {
            " NOT NULL"
        }
        "$name $type$notNull"
    }.joinToString(", ")
    execute("CREATE TABLE IF NOT EXISTS $table($fields)")
}


fun <T : Any> Connection.insert(items: List<T>) {
    createStatement().createTable(items.first())
    val table = items.first()::class.java.simpleName.toSnake().toPlural()
    val fields = getFields(items.first())
    val columns = fields.keys.joinToString(", ")
    val placeholder = fields.keys.map { "?" }.joinToString(", ")
    val query = "INSERT INTO $table ($columns) VALUES ($placeholder)"
    val ps = prepareStatement(query)
    items.map { getFields(it) }.forEach {
        it.values.forEachIndexed { index, value ->
            ps.setString(index + 1, value)
        }
        ps.addBatch()
        //ps.clearParameters()
    }
    System.out.println("Batch insert ${items.size} elements into $table")
    autoCommit = false
    ps.executeBatch()
    autoCommit = true
}

private fun String.toPlural() = if (last() == 's') {
    this + "es"
} else {
    this + "s"
}

private fun createInsertQuery(obj: Any): String {
    val table = obj::class.java.simpleName.toSnake().toPlural()
    val fields = getFields(obj)
    val columns = fields.keys.joinToString(", ")
    val values = fields.values.joinToString(", ")
    val query = "INSERT INTO $table ($columns) VALUES ($values)"
    return query
}

private fun getFields(obj: Any): MutableMap<String, String?> {
    val fields = mutableMapOf<String, String?>()
    obj::class.declaredMemberProperties.forEach {
        val name = it.name.toSnake()
        it.javaField!!.isAccessible = true
        val value = it.javaField!!.get(obj)
        fields[name] = getValue(value)
    }
    return fields
}

fun getValue(value: Any?): String? {
    return when (value) {
        null -> null
        is String -> value
        is Boolean -> if (value) "1" else "0"
        is Date -> SQLiteConstants.dateFormat.format(value)
        else -> value.toString()
    }
}

fun KClass<*>.toSqliteType(): String {
    return when (java.name) {
        "java.lang.String" -> "text"
        "java.lang.Double" -> "real"
        "java.util.Date" -> "text"
        "int" -> "integer"
        "boolean" -> "integer"
        else -> throw RuntimeException("Unsupported type ${java.name}")
    }
}

fun String.toSnake() = mapIndexed { index, char ->
    if (char.isUpperCase()) {
        if (index != 0) {
            "_"
        } else {
            ""
        } + char.toLowerCase()
    } else char.toString()
}.joinToString("")
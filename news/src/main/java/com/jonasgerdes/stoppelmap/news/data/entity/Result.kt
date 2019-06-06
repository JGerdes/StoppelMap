package com.jonasgerdes.stoppelmap.news.data.entity

sealed class Result {
    object Success : Result()
    object Error : Result()
    object NetworkError : Result()
}
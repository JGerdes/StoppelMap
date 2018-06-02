package com.jonasgerdes.stoppelmap.model.news

data class NewsResponse(val versionName: String = "",
                        val version: Int = 0,
                        val updated: String = "",
                        val items: List<Item>)


data class Item(val images: List<String>?,
                val subTitle: String? = "",
                val publishDate: String = "",
                val title: String = "",
                val content: String = "",
                val teaser: String = "")



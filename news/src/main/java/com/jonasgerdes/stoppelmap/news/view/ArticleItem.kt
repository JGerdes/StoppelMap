package com.jonasgerdes.stoppelmap.news.view

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.xwray.groupie.kotlinandroidextensions.Item

abstract class ArticleItem : Item() {
    abstract val article: Article
}
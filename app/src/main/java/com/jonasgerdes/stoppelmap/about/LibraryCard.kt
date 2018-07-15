package com.jonasgerdes.stoppelmap.about

import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.setTextOrHide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_library.*
import kotlinx.android.synthetic.main.about_card_library.view.*

data class LibraryCard(
        val name: String,
        val author: String,
        val license: License,
        val githubUrl: String? = null
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = name
        viewHolder.author.text = author
        viewHolder.license.text = license.name
        viewHolder.urlGitHub.setTextOrHide(githubUrl)
    }

    override fun getLayout() = R.layout.about_card_library

}

sealed class License(
        val name: String,
        val fullText: String,
        val link: String
) {

    data class Apache2(val url:String = "") : License("Apache 2", "", url)
    data class MIT(val url:String = "") : License("MIT", "", url)
}
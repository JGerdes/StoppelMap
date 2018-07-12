package com.jonasgerdes.stoppelmap.about

import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.setTextOrHide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_author.*

data class LibraryCard(
        val name: String,
        val author: String,
        val license: String,
        val githubUrl: String? = null
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = name
        viewHolder.work.text = work
        viewHolder.urlWebsite.setTextOrHide(website)
        viewHolder.urlGitHub.setTextOrHide(githubUrl)
        viewHolder.mail.setTextOrHide(mail)
    }

    override fun getLayout() = R.layout.about_card_author

}
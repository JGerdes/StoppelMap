package com.jonasgerdes.stoppelmap.about

import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.setTextOrHide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_author.*

data class AuthorCard(
        val name: String,
        val work: String,
        val website: String? = null,
        val githubUrl: String? = null,
        val mail: String? = null
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




package com.jonasgerdes.stoppelmap.about.view

import com.jonasgerdes.androidutil.view.setTextOrHide
import com.jonasgerdes.stoppelmap.about.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_author.view.*

data class AuthorCard(
    val name: String,
    val work: String,
    val website: String? = null,
    val githubUrl: String? = null,
    val mail: String? = null
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.name.text = name
            itemView.work.text = work
            itemView.urlWebsite.setTextOrHide(website)
            itemView.urlGitHub.setTextOrHide(githubUrl)
            itemView.mail.setTextOrHide(mail)
        }
    }

    override fun getLayout() = R.layout.about_card_author

}




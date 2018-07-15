package com.jonasgerdes.stoppelmap.about

import android.content.Intent
import android.net.Uri
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.setTextOrHide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_library.*

data class LibraryCard(
        val name: String,
        val author: String,
        val license: License,
        val githubUrl: String? = null,
        val sourceUrl: String? = null
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = name
        viewHolder.author.text = author.split(";").joinToString("\n")
        viewHolder.license.text = license.name
        viewHolder.license.setOnClickListener {
            viewHolder.itemView.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(license.link))
            )
        }
        viewHolder.urlGitHub.setTextOrHide(githubUrl)
        viewHolder.sourceUrl.setTextOrHide(sourceUrl)
    }

    override fun getLayout() = R.layout.about_card_library

}

open class License(
        val name: String,
        val fullText: String? = null,
        val link: String
) {

    data class Apache2(val url: String = "https://www.apache.org/licenses/LICENSE-2.0.txt") : License("Apache 2.0", "", url)
    data class MIT(val url: String = "https://opensource.org/licenses/MIT") : License("MIT", "", url)
}
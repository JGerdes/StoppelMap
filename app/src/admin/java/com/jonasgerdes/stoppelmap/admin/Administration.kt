package com.jonasgerdes.stoppelmap.admin

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import com.jonasgerdes.stoppelmap.R
import java.util.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
object Administration {

    fun init(context: Context) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val id = "stoppelmap_admin"
            val shortcutManager = context.getSystemService(ShortcutManager::class.java)
            val shortcut = ShortcutInfo.Builder(context, id).setShortLabel("Admin")
                    .setLongLabel("Admin tools")
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_admin_32dp))
                    .setIntent(
                            Intent(context, AdminActivity::class.java)
                                    .setAction(Intent.ACTION_VIEW)
                    )
                    .build()
            Log.d("Administration", "created shortcuts")
            shortcutManager.dynamicShortcuts = Arrays.asList(shortcut)
        }
    }
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadWidgetSettingsActivity
import com.jonasgerdes.stoppelmap.countdown.widget.skyline.SkylineWidgetSettingsActivity
import com.jonasgerdes.stoppelmap.widget.countdown.CountdownWidgetReceiver
import com.jonasgerdes.stoppelmap.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider

@Composable
fun CountDownWidgetSuggestionCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.widget_suggestion_card_title),
                style = MaterialTheme.typography.titleMedium
            )
            WidgetSuggestion(
                previewDrawable = R.drawable.widget_countdown_preview,
                previewContentDescription = R.string.widget_countdown_description,
                onAddTap = { addCountdownWidget(context) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            WidgetSuggestion(
                previewDrawable = R.drawable.widget_preview_heart,
                previewContentDescription = R.string.widget_suggestion_card_gingerbread_contentDescription,
                onAddTap = { addGingerbreadWidget(context) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            WidgetSuggestion(
                previewDrawable = R.drawable.widget_preview_skyline,
                previewContentDescription = R.string.widget_suggestion_card_skyline_contentDescription,
                onAddTap = { addSkylineWidget(context) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun WidgetSuggestion(
    @DrawableRes previewDrawable: Int,
    @StringRes previewContentDescription: Int,
    onAddTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable {
                onAddTap()
            }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painterResource(id = previewDrawable),
                contentDescription = stringResource(id = previewContentDescription)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = stringResource(id = R.string.widget_suggestion_card_add))
        }
    }
}

private fun addCountdownWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, CountdownWidgetReceiver::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        appWidgetManager.requestPinAppWidget(provider, null, null)
    }
}

private fun addGingerbreadWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, GingerbreadHeartWidgetProvider::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        val successCallback: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, GingerbreadWidgetSettingsActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it or PendingIntent.FLAG_MUTABLE
                    } else it
                }
            )
        appWidgetManager.requestPinAppWidget(provider, null, successCallback)
    }
}

private fun addSkylineWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, SilhouetteWidgetProvider::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        val successCallback: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, SkylineWidgetSettingsActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it or PendingIntent.FLAG_MUTABLE
                    } else it
                }
            )
        appWidgetManager.requestPinAppWidget(provider, null, successCallback)
    }
}

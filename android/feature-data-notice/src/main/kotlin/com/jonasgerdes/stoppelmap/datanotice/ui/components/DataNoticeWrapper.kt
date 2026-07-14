package com.jonasgerdes.stoppelmap.datanotice.ui.components

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.shared.dataupdate.ui.DataNoticeViewModel
import com.jonasgerdes.stoppelmap.theme.util.localizedString
import org.koin.androidx.compose.koinViewModel

@Composable
fun DataNoticeWrapper(
    content: @Composable () -> Unit,
) {
    val viewModel: DataNoticeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val showNotices by remember { derivedStateOf { state.notices.isNotEmpty() } }
    if (showNotices) {
        Column {
            content()
            state.notices.forEach { notice ->
                Snackbar(Modifier.padding(4.dp)) {
                    FlowRow(
                        verticalArrangement = spacedBy(2.dp),
                        horizontalArrangement = spacedBy(8.dp),
                        itemVerticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Info, null)
                        Text(localizedString(notice.title), fontWeight = FontWeight.Bold)
                        Text(localizedString(notice.content))
                    }
                }
            }
        }
    } else {
        content()
    }
}
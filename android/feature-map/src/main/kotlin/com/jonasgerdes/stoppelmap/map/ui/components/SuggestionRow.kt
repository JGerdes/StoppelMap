package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.ui.iconRes

@Composable
fun SuggestionRow(
    suggestions: List<SearchResult>,
    onSuggestionSelected: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
        horizontalArrangement = Arrangement.Absolute.spacedBy(4.dp)
    ) {
        items(
            suggestions,
            key = { it.term + it.resultEntities.joinToString { it.slug } }) { suggestion ->
            ElevatedSuggestionChip(
                shape = RoundedCornerShape(100),
                onClick = { onSuggestionSelected(suggestion) },
                label = { Text(suggestion.term) },
                icon = {
                    suggestion.icon?.iconRes?.let { res ->
                        Icon(
                            painterResource(id = res),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }
    }
}
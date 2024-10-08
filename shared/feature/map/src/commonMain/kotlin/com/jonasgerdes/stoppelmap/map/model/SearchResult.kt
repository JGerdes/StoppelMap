package com.jonasgerdes.stoppelmap.map.model

data class SearchResult(
    val term: String,
    val icon: MapIcon?,
    val score: Float,
    val resultEntities: List<StallSummary>,
    val type: Type
) {
    enum class Type {
        SingleStall,
        Collection,
    }

    fun supportingText() = when (type) {
        Type.SingleStall -> resultEntities.first().typeName?.let { type ->
            val subType = resultEntities.first().subTypeName
            if (subType == null) type else "$subType ($type)"
        }

        Type.Collection -> "${resultEntities.size} mal auf dem Stoppelmarkt" //TODO: localize
    }
}

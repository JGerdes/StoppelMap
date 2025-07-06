package mobi.waterdog.kgeojson

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PositionSerializer::class)
data class Position(val lng: Double, val lat: Double)

class PositionSerializer : KSerializer<Position> {
    private val serializer = ListSerializer(Double.serializer())
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: Position) {
        serializer.serialize(encoder, listOf(value.lng, value.lat))
    }

    override fun deserialize(decoder: Decoder): Position {
        val geoJsonCoordinatesList = decoder.decodeSerializableValue(serializer)
        return Position(geoJsonCoordinatesList[0], geoJsonCoordinatesList[1])
    }
}
package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.UUID

object LocationSerializer : KSerializer<Location> {

    override val descriptor = LocationSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Location) {
        val surrogate = LocationSurrogate(
            value.world?.uid,
            value.x,
            value.y,
            value.z,
            value.yaw,
            value.pitch
        )
        encoder.encodeSerializableValue(LocationSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Location {
        val surrogate = decoder.decodeSerializableValue(LocationSurrogate.serializer())
        return Location(
            surrogate.world?.let(Bukkit::getWorld),
            surrogate.x,
            surrogate.y,
            surrogate.z,
            surrogate.yaw,
            surrogate.pitch
        )
    }
}

@Serializable
@SerialName("Location")
private data class LocationSurrogate(
    @Serializable(with = UUIDSerializer::class)
    val world: UUID?,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
)
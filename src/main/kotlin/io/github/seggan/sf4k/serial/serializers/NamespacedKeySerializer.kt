package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import org.bukkit.NamespacedKey

object NamespacedKeySerializer : DelegatingSerializer<NamespacedKey, String>(serializer()) {
    override fun toData(value: NamespacedKey): String = value.toString()
    override fun fromData(value: String): NamespacedKey = NamespacedKey.fromString(value)
        ?: throw SerializationException("Invalid NamespacedKey: $value")
}
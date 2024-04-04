package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer

inline fun <reified T> PersistentDataContainer.set(
    key: NamespacedKey,
    value: T,
    strategy: SerializationStrategy<T> = serializer()
) {
    PersistentDataEncoder.encode(strategy, key, value, this)
}

inline fun <reified T> PersistentDataContainer.get(
    key: NamespacedKey,
    strategy: DeserializationStrategy<T> = serializer()
): T? {
    return PersistentDataDecoder.decode(strategy, key, this)
}
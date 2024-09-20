package io.github.seggan.sf4k.serial.pdc

import io.github.seggan.sf4k.serial.serializers.BukkitSerializerRegistry
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.plugin.Plugin
import java.util.*

/**
 * Sets a value in the [PersistentDataContainer] with the given [NamespacedKey].
 *
 * @receiver The [PersistentDataContainer] to set the value in.
 * @param key The key to set the value at.
 * @param value The value to set.
 * @param strategy The serialization strategy to use. Defaults to [serializer].
 * @param T The type of the value.
 */
inline fun <reified T> PersistentDataContainer.setData(
    key: NamespacedKey,
    value: T,
    strategy: SerializationStrategy<T> = BukkitSerializerRegistry.serializer<T>()
) {
    PersistentDataEncoder.encode(strategy, key, value, this)
}

/**
 * Gets a value from the [PersistentDataContainer] with the given [NamespacedKey].
 *
 * @receiver The [PersistentDataContainer] to get the value from.
 * @param key The key to get the value from.
 * @param strategy The deserialization strategy to use. Defaults to [serializer].
 * @param T The type of the value.
 * @return The value, or null if it does not exist.
 */
inline fun <reified T> PersistentDataContainer.getData(
    key: NamespacedKey,
    strategy: DeserializationStrategy<T> = BukkitSerializerRegistry.serializer<T>()
): T? {
    return PersistentDataDecoder.decode(strategy, key, this)
}

private val keyCache = WeakHashMap<NamespacedKey, Plugin>()

internal val NamespacedKey.plugin: Plugin
    get() = keyCache.getOrPut(this) {
        Bukkit.getPluginManager().plugins.find { it.name.equals(namespace, ignoreCase = true) }
            ?: throw IllegalArgumentException("No plugin found with name $namespace")
    }
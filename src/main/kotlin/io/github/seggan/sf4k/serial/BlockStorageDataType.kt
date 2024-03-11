package io.github.seggan.sf4k.serial

import me.mrCookieSlime.Slimefun.api.BlockStorage
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

/**
 * An interface for serializing and deserializing data for [BlockStorage].
 *
 * @param T The type of data to serialize and deserialize.
 */
interface BlockStorageDataType<T> {

    /**
     * Serializes the given [value] to a [String].
     *
     * @param value The value to serialize.
     *
     * @return The serialized value.
     */
    fun serialize(value: T): String

    /**
     * Deserializes the given [value] to a [T].
     *
     * @param value The value to deserialize.
     *
     * @return The deserialized value, or `null` if the value cannot be deserialized.
     */
    fun deserialize(value: String): T?

    companion object {

        /**
         * A [BlockStorageDataType] for [String]s.
         */
        val STRING: BlockStorageDataType<String> = BasicBlockStorageDataType(String::toString, String::toString)

        /**
         * A [BlockStorageDataType] for [Boolean]s.
         */
        val BOOLEAN: BlockStorageDataType<Boolean> = BasicBlockStorageDataType(
            Boolean::toString,
            String::toBooleanStrictOrNull
        )

        /**
         * A [BlockStorageDataType] for [Int]s.
         */
        val INT: BlockStorageDataType<Int> = BasicBlockStorageDataType(
            Int::toString,
            String::toIntOrNull
        )

        /**
         * A [BlockStorageDataType] for [Long]s.
         */
        val LONG: BlockStorageDataType<Long> = BasicBlockStorageDataType(
            Long::toString,
            String::toLongOrNull
        )

        /**
         * A [BlockStorageDataType] for [Float]s.
         */
        val FLOAT: BlockStorageDataType<Float> = BasicBlockStorageDataType(
            Float::toString,
            String::toFloatOrNull
        )

        /**
         * A [BlockStorageDataType] for [Double]s.
         */
        val DOUBLE: BlockStorageDataType<Double> = BasicBlockStorageDataType(
            Double::toString,
            String::toDoubleOrNull
        )

        /**
         * A [BlockStorageDataType] for [UUID]s.
         */
        val UUID: BlockStorageDataType<UUID> = UuidDataType

        /**
         * A [BlockStorageDataType] for [OfflinePlayer]s.
         */
        val PLAYER: BlockStorageDataType<OfflinePlayer> = BasicCompositeBlockStorageDataType(
            UUID,
            OfflinePlayer::getUniqueId,
            Bukkit::getOfflinePlayer
        )
    }
}

/**
 * A basic implementation of [BlockStorageDataType] that takes functions
 * for serializing and deserializing.
 *
 * @param T The type of data to serialize and deserialize.
 * @param serializer The function to serialize data.
 * @param deserializer The function to deserialize data.
 */
class BasicBlockStorageDataType<T>(
    private val serializer: (T) -> String,
    private val deserializer: (String) -> T?
) : BlockStorageDataType<T> {
    override fun serialize(value: T): String = serializer(value)

    override fun deserialize(value: String): T? = deserializer(value)
}

/**
 * A [BlockStorageDataType] that uses an intermediate [BlockStorageDataType]
 * to serialize and deserialize data.
 *
 * @param S The type of data to serialize and deserialize.
 * @param T The type of data that the intermediate [BlockStorageDataType] serializes and deserializes.
 * @param intermediate The intermediate [BlockStorageDataType] to use.
 */
abstract class CompositeBlockStorageDataType<S, T>(
    private val intermediate: BlockStorageDataType<T>
) : BlockStorageDataType<S> {

    final override fun serialize(value: S): String = intermediate.serialize(partSerialize(value))
    final override fun deserialize(value: String): S? {
        return partDeserialize(intermediate.deserialize(value) ?: return null)
    }

    /**
     * Serializes the given [value] to a [T].
     *
     * @param value The value to serialize.
     *
     * @return The serialized value.
     */
    abstract fun partSerialize(value: S): T

    /**
     * Deserializes the given [value] to a [S].
     *
     * @param value The value to deserialize.
     *
     * @return The deserialized value, or `null` if the value cannot be deserialized.
     */
    abstract fun partDeserialize(value: T): S?
}

/**
 * A [CompositeBlockStorageDataType] that uses functions for serializing and deserializing.
 *
 * @param S The type of data to serialize and deserialize.
 * @param T The type of data that the intermediate [BlockStorageDataType] serializes and deserializes.
 * @param serializer The function to serialize data.
 * @param deserializer The function to deserialize data.
 */
class BasicCompositeBlockStorageDataType<S, T>(
    intermediate: BlockStorageDataType<T>,
    private val serializer: (S) -> T,
    private val deserializer: (T) -> S?
) : CompositeBlockStorageDataType<S, T>(intermediate) {

    override fun partSerialize(value: S): T = serializer(value)

    override fun partDeserialize(value: T): S? = deserializer(value)
}

private object UuidDataType : BlockStorageDataType<UUID> {

    private val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$".toRegex()

    override fun serialize(value: UUID): String = value.toString()

    override fun deserialize(value: String): UUID? {
        if (!uuidRegex.matches(value)) return null
        return UUID.fromString(value)
    }
}
package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.*
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * A registry for serializers used by the Bukkit module.
 */
object BukkitSerializerRegistry {

    /**
     * The serializers module for the registry.
     */
    var serializersModule = EmptySerializersModule()
        private set

    init {
        edit {
            contextual(BlockPositionSerializer)
            contextual(LocationSerializer)
            contextual(PlayerSerializer)
            contextual(OfflinePlayerSerializer)
            contextual(UUIDSerializer)
            contextual(NamespacedKeySerializer)
            contextual(VectorSerializer)
            contextual(BlockVectorSerializer)
            contextual(ItemStackSerializer)
        }
    }

    /**
     * Edits the serializers module.
     */
    fun edit(block: SerializersModuleBuilder.() -> Unit) {
        serializersModule = serializersModule.overwriteWith(SerializersModule(block))
    }

    /**
     * Adds a serializer to the registry.
     *
     * @param kClass The class to add the serializer for.
     * @param serializer The serializer to add.
     */
    fun <T : Any> addSerializer(kClass: KClass<T>, serializer: KSerializer<T>) {
        edit {
            contextual(kClass, serializer)
        }
    }

    /**
     * Adds a serializer to the registry.
     *
     * @param serializer The serializer to add.
     * @param T The type of the serializer.
     */
    inline fun <reified T : Any> addSerializer(serializer: KSerializer<T>) {
        addSerializer(T::class, serializer)
    }

    /**
     * Gets a serializer for the given type.
     *
     * @param T The type of the serializer.
     * @return The serializer.
     */
    inline fun <reified T> serializer(): KSerializer<T> = serializersModule.serializer()
}
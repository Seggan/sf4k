package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.typeOf

/**
 * A registry for custom serializers. Used to go around the fact that there is no
 * way to register serializers globally in `kotlinx.serialization`.
 */
object CustomSerializerRegistry {

    private val serializers = mutableMapOf<Class<*>, KSerializer<*>>()

    /**
     * Registers a serializer for the given class.
     *
     * @param clazz The class to register the serializer for.
     * @param serializer The serializer to register.
     */
    fun <T> register(clazz: Class<T>, serializer: KSerializer<T>) {
        serializers[clazz] = serializer
    }


    /**
     * Registers a serializer for the given class.
     *
     * @param T The class to register
     * @param serializer The serializer to register.
     */
    inline fun <reified T> register(serializer: KSerializer<T>) {
        register(T::class.java, serializer)
    }

    /**
     * Gets the serializer for the given class.
     *
     * @param clazz The class to get the serializer for.
     * @return The serializer, or null if none is registered.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: Class<T>): KSerializer<T>? = serializers[clazz] as KSerializer<T>?

    /**
     * Gets the serializer for the given class.
     *
     * @param T The class to get the serializer for.
     * @return The serializer, or null if none is registered.
     */
    inline fun <reified T> get(): KSerializer<T>? = get(T::class.java)

    init {
        register(BlockPositionSerializer)
        register(LocationSerializer)
        register(PlayerSerializer)
        register(OfflinePlayerSerializer)
        register(UUIDSerializer)
    }
}

/**
 * Gets the default serializer for the given type, or the registered one if it exists.
 *
 * @param T The type to get the serializer for.
 * @return The serializer.
 * @throws IllegalStateException If no serializer is found.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> defaultSerializerOrRegistered(): KSerializer<T> {
    return serializerOrNull(typeOf<T>()) as KSerializer<T>?
        ?: CustomSerializerRegistry.get<T>()
        ?: throw IllegalStateException("No serializer found for type ${T::class.java}")
}
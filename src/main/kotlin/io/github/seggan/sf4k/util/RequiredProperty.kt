package io.github.seggan.sf4k.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A property that us required to be set before it can be accessed.
 *
 * @param T the type of the property
 * @param value the initial value of the property
 * @param getter run before getting the property
 * @param setter run before setting the property
 */
class RequiredProperty<T>(
    private var value: T? = null,
    private val getter: (T) -> T = { it },
    private val setter: (T) -> T = { it }
) : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getter(value ?: error("${property.name} must be set"))
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = setter(value)
    }
}
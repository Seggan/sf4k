package io.github.seggan.sf4k.item.builder

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import kotlin.properties.ReadOnlyProperty

/**
 * A property delegate that provides a [SlimefunItemStack] for a [SlimefunItem].
 *
 * @param T The type of the [SlimefunItem] this [ItemProvider] is providing a [SlimefunItemStack] for.
 * @property item The [SlimefunItemStack] to provide.
 * @property slimefunItem The [SlimefunItem] this [ItemProvider] is providing a [SlimefunItemStack] for.
 */
class ItemProvider<T : SlimefunItem>(private val item: SlimefunItemStack, val slimefunItem: T) :
    ReadOnlyProperty<Any?, SlimefunItemStack> {
    override fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): SlimefunItemStack {
        return item
    }
}
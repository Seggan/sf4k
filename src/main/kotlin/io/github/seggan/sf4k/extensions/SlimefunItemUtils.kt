package io.github.seggan.sf4k.extensions

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import org.bukkit.inventory.ItemStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KProperty

/**
 * Checks if the [ItemStack] has Slimefun data.
 */
@OptIn(ExperimentalContracts::class)
fun ItemStack?.isSlimefun(): Boolean {
    contract {
        returns(true) implies (this@isSlimefun != null)
    }
    return SlimefunItem.getByItem(this) != null
}

/**
 * Returns the associated [SlimefunItem] to an item stack, cast to the specified subclass.
 * In case none is found, the return value will be null.
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : SlimefunItem> ItemStack?.getSlimefun(): T? {
    contract {
        returnsNotNull() implies (this@getSlimefun != null)
    }
    return SlimefunItem.getByItem(this) as? T
}

/**
 * Allows [ItemSetting] to be used as a delegate
 */
operator fun <T> ItemSetting<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value

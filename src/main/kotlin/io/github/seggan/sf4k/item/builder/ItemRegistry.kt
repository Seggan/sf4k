package io.github.seggan.sf4k.item.builder

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon

/**
 * A class that allows the use of [buildSlimefunItem]
 *
 * Example:
 * ```kotlin
 * object MyAddonItems : ItemRegistry(MyAddon.instance) {
 *  val MY_ITEM = buildSlimefunItem {
 *      // ...
 *  }
 * }
 * ```
 *
 * @see buildSlimefunItem
 */
abstract class ItemRegistry(
    val addon: SlimefunAddon,
    val prefix: String = addon.javaPlugin.name.uppercase()
)
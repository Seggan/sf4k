package io.github.seggan.sf4k.extensions

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting
import kotlin.reflect.KProperty

operator fun <T> ItemSetting<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value
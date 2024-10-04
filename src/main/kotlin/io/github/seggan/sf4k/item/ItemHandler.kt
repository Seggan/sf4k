package io.github.seggan.sf4k.item

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ItemHandler(val handler: KClass<out ItemHandler>)

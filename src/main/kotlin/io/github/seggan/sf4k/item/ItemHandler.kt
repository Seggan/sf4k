package io.github.seggan.sf4k.item

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler
import kotlin.reflect.KClass

/**
 * Indicates that the annotated function is an [ItemHandler].
 * If you use this in an interface, the interface **must** be either annotated
 * with [JvmDefaultWithoutCompatibility] or `-Xjvm-default=all` be added to the
 * Kotlin compiler arguments.
 *
 * @param handler the class of the [ItemHandler] this function represents
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ItemHandler(val handler: KClass<out ItemHandler>)

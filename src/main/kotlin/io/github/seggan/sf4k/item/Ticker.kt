package io.github.seggan.sf4k.item

import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker

/**
 * Indicates that the annotated function is a [BlockTicker].
 * If you use this in an interface, the interface **must** be either annotated
 * with [JvmDefaultWithoutCompatibility] or `-Xjvm-default=all` be added to the
 * Kotlin compiler arguments.
 *
 * @param async whether the ticker is async or not. Defaults to `false`
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Ticker(val async: Boolean = false)

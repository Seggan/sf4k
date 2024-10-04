package io.github.seggan.sf4k.item

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Ticker(val async: Boolean = false)

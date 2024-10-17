package io.github.seggan.sf4k.util

import kotlin.jvm.internal.Reflection
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.valueParameters

/**
 * Finds a constructor of the given [KClass] with the specified argument types
 *
 * @param args the types of the arguments to be passed
 * @return the found constructor, or `null` if none found
 */
fun <T : Any> KClass<T>.findConstructor(vararg args: KClass<*>?): KFunction<T>? {
    val argTypes = args.map { arg -> arg?.fillArgsWithNothing() }
    return constructors.firstOrNull {
        it.valueParameters.size == args.size &&
                it.valueParameters.zip(argTypes).all { (param, arg) ->
                    if (arg == null) param.type.isMarkedNullable
                    else param.type.isSupertypeOf(arg)
                }
    }
}

/**
 * Find the first constructor that can be passed the given arguments.
 * Does not take into account generic parameters due to type erasure.
 *
 * @param args the arguments to match
 * @return the found constructor, or `null` if none found
 */
fun <T : Any> KClass<T>.findConstructorFromArgs(vararg args: Any?): KFunction<T>? {
    return findConstructor(*args.map { arg -> arg?.let { it::class } }.toTypedArray())
}

private fun KClass<*>.fillArgsWithNothing(): KType {
    return createType(List(typeParameters.size) { nothingProjection })
}
private val nothingProjection = KTypeProjection.invariant(Reflection.nothingType(typeOf<Void>()))
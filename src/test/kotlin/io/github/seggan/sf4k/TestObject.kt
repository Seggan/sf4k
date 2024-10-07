package io.github.seggan.sf4k

import kotlinx.serialization.Serializable

@Serializable
data class TestObject(val a: String, val b: Int, val c: String?)

@Serializable
@JvmInline
value class TestValue(val a: String)
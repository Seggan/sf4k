package io.github.seggan.sf4k.serial

import kotlin.reflect.KClass

class EnumBlockStorageDataType<E : Enum<E>>(enumClass: Class<E>) : BlockStorageDataType<E> {

    constructor(enumClass: KClass<E>) : this(enumClass.java)

    private val constants = enumClass.enumConstants.associateBy(Enum<E>::name)

    override fun serialize(value: E): String = value.name

    override fun deserialize(value: String): E? = constants[value]
}
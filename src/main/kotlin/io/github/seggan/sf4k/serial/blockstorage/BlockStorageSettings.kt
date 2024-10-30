package io.github.seggan.sf4k.serial.blockstorage

/**
 * The settings to use when serializing/deserializing values in block storage.
 *
 * @property encodeByteArrayAsBase64 Whether to encode byte arrays as base64 strings. Defaults to true.
 */
data class BlockStorageSettings(val encodeByteArrayAsBase64: Boolean = true)
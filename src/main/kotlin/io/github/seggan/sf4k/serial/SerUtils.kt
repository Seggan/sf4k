package io.github.seggan.sf4k.serial

private const val BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

internal fun Long.toBase62(): String {
    val sb = StringBuilder()
    var num = this
    while (num > 0) {
        sb.append(BASE62[num.toInt() % 62])
        num /= 62
    }
    return sb.reverse().toString()
}

internal fun CharSequence.fromBase62(): Long = fold(0) { acc, c -> acc * 62 + BASE62.indexOf(c) }

internal fun StringBuilder.nom(): Char {
    val c = this[0]
    deleteCharAt(0)
    return c
}

internal fun StringBuilder.nom(n: Int): String {
    val s = StringBuilder()
    repeat(n) {
        s.append(nom())
    }
    return s.toString()
}

internal inline fun StringBuilder.nomWhile(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    while (true) {
        val c = nom()
        if (predicate(c)) {
            sb.append(c)
        } else {
            break
        }
    }
    return sb.toString()
}
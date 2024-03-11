#sf4k

sf4k is a library for Kotlin that provides a useful base and utilities for writing 
[Slimefun](https://github.com/Slimefun/Slimefun4) addons. [Docs](https://seggan.github.io/sf4k/).

## How to use

sf4k provides `AbstractAddon` as a base class for you to extend from, as Kotlin does not allow
you to extend both `JavaPlugin` and `SlimefunAddon` at the same time.

sf4k also provides a few convenience functions, such as destructuring and operator overloads for
`Location`, and `BlockStorage` serialization interface. See the [docs](https://seggan.github.io/sf4k/)
for more information.

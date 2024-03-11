# sf4k

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.seggan/sf4k)
[![Docs](https://img.shields.io/badge/docs-gh--pages-blue)](https://seggan.github.io/sf4k/)

sf4k is a library for Kotlin that provides a useful base and utilities for writing 
[Slimefun](https://github.com/Slimefun/Slimefun4) addons. 

## Installation

### Gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.seggan:sf4k:[VERSION]")
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.seggan</groupId>
    <artifactId>sf4k</artifactId>
    <version>[VERSION]</version>
</dependency>
```

## How to use

sf4k provides `AbstractAddon` as a base class for you to extend from, as Kotlin does not allow
you to extend both `JavaPlugin` and `SlimefunAddon` at the same time.

sf4k also provides a few convenience functions, such as destructuring and operator overloads for
`Location`, and `BlockStorage` serialization interface. See the [docs](https://seggan.github.io/sf4k/)
for more information.
